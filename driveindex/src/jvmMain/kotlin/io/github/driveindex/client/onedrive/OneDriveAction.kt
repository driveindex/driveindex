package io.github.driveindex.client.onedrive

import io.github.driveindex.client.AbsClientAction
import io.github.driveindex.client.ClientType
import io.github.driveindex.core.ConfigManager
import io.github.driveindex.core.util.*
import io.github.driveindex.database.dao.onedrive.OneDriveAccountDao
import io.github.driveindex.database.dao.onedrive.OneDriveClientDao
import io.github.driveindex.database.dao.onedrive.OneDriveFileDao
import io.github.driveindex.database.entity.AccountsEntity
import io.github.driveindex.database.entity.ClientsEntity
import io.github.driveindex.database.entity.FileEntity
import io.github.driveindex.database.entity.onedrive.OneDriveAccountEntity
import io.github.driveindex.database.entity.onedrive.OneDriveClientEntity
import io.github.driveindex.database.entity.onedrive.OneDriveFileEntity
import io.github.driveindex.dto.feign.AzureGraphDtoV2_Me_Drive_Root_Delta
import io.github.driveindex.dto.resp.RespResult
import io.github.driveindex.dto.resp.SampleResult
import io.github.driveindex.dto.resp.resp
import io.github.driveindex.exception.FailedResult
import io.github.driveindex.feigh.onedrive.AzureAuthClient
import io.github.driveindex.feigh.onedrive.getToken
import io.github.driveindex.feigh.onedrive.withCheckedToken
import jakarta.transaction.Transactional
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import org.aspectj.weaver.tools.cache.SimpleCacheFactory
import org.springframework.web.bind.annotation.*
import java.net.URLEncoder
import java.util.*

@RestController
class OneDriveAction(
    private val oneDriveClientDao: OneDriveClientDao,
    private val oneDriveAccountDao: OneDriveAccountDao,
    private val oneDriveFileDao: OneDriveFileDao,
): AbsClientAction(ClientType.OneDrive) {
    @GetMapping("/api/user/login/url/onedrive")
    override fun loginUri(
        @RequestParam("client_id", required = true) clientId: UUID,
        @RequestParam("redirect_uri", required = true) redirectUri: String
    ): RespResult<String> {
        val client = getClient(clientId)
        oneDriveClientDao.getClient(clientId).let { entity ->
            val state = linkedMapOf<String, Any>(
                "id" to clientId,
                "ts" to System.currentTimeMillis(),
                "type" to client.type,
                "redirect_uri" to redirectUri,
            )
            state["sign"] = "${state.joinToSortedString("&")}${ConfigManager.TokenSecurityKey}".MD5_FULL
            return ("${entity.endPoint.LoginHosts}/${entity.tenantId}/oauth2/v2.0/authorize?" +
                "client_id=${entity.clientId}" +
                "&response_type=code" +
                "&redirect_uri=${URLEncoder.encode(redirectUri, Charsets.UTF_8)}" +
                "&response_mode=query" +
                "&scope=${AzureAuthClient.Scope.joinToString("%20")}" +
                "&state=${state.joinToString("&").BASE64}").resp()
        }
    }

    @Transactional
    @PostMapping("/api/user/login/request/onedrive")
    override fun loginRequest(@RequestBody params: JsonObject): RespResult<Unit> {
        val dto = JsonGlobal.decodeFromJsonElement<AccountLoginOneDrive>(params)
        val param = dto.state.ORIGIN_BASE64
            .split("&")
            .fold(mutableMapOf<String, String>()) { map, param ->
                param.split("=").takeIf {
                    it.size == 2
                }?.let {
                    map[it[0]] = it[1]
                }
                return@fold map
            }
        val ts = param["ts"]?.toLongOrNull()
            ?: throw FailedResult.Auth.IllegalRequest
        if (ts + 60L * 2 * 1000 < System.currentTimeMillis()) {
            throw FailedResult.Auth.AuthTimeout
        }

        val clientId = try {
            UUID.fromString(param["id"])
        } catch (e: Exception) {
            throw FailedResult.Auth.IllegalRequest
        }
        val type = try {
            ClientType.valueOf(param["type"]!!)
        } catch (e: Exception) {
            throw FailedResult.Auth.IllegalRequest
        }
        val redirectUri = param["redirect_uri"]?.takeIf { it.isNotBlank() }
            ?: throw FailedResult.Auth.IllegalRequest
        val originSign = param["sign"]
            ?: throw FailedResult.Auth.IllegalRequest
        val sign = "${linkedMapOf<String, Any>(
            "id" to clientId,
            "ts" to ts,
            "type" to type,
            "redirect_uri" to redirectUri,
        ).joinToSortedString("&")}${
            ConfigManager.TokenSecurityKey
        }".MD5_FULL
        if (originSign != sign) {
            throw FailedResult.Auth.IllegalRequest
        }

        val client = clientDao.getClient(clientId)
            ?: throw FailedResult.Auth.IllegalRequest
        param["type"]?.let type@{
            return@type ClientType.valueOf(it)
        }?.takeIf {
            client.type == it
        } ?: throw FailedResult.Auth.IllegalRequest
        val onedriveClient = oneDriveClientDao.getClient(client.id)

        val token = onedriveClient.endPoint.Auth.getToken(
            onedriveClient.tenantId,
            dto.code,
            onedriveClient.clientId,
            onedriveClient.clientSecret,
            redirectUri
        )

        val me = onedriveClient.endPoint.Graph.Me("${token.tokenType} ${token.accessToken}")

        // 允许账号失效后重新登录
        oneDriveAccountDao.findByAzureId(
            accountDao.selectIdByClient(client.id), me.id
        )?.apply {
            tokenType = token.tokenType
            accessToken = token.accessToken
            refreshToken = token.refreshToken
            tokenExpire = token.expires
            oneDriveAccountDao.save(this)

            accountDao.getAccount(id)?.let {
                it.accountExpired = false
                accountDao.save(it)
            }

            return SampleResult
        }

        // TODO 重名时自动重命名
        accountDao.findByName(client.id, me.displayName)?.let {
            throw FailedResult.Client.DuplicateAccountName(it.displayName, it.id)
        }

        val entity = AccountsEntity(
            parentClientId = client.id,
            displayName = me.displayName,
            createBy = current.User.id,
            userPrincipalName = me.userPrincipalName,
        )
        accountDao.save(entity)
        oneDriveAccountDao.save(
            OneDriveAccountEntity(
                id = entity.id,
                azureUserId = me.id,
                tokenType = token.tokenType,
                accessToken = token.accessToken,
                refreshToken = token.refreshToken,
                tokenExpire = token.expires,
            )
        )

        return SampleResult
    }

    @Transactional
    override fun create(name: String, params: JsonObject) {
        val creation: ClientCreateOneDrive = JsonGlobal.decodeFromJsonElement(params)

        if (creation.tenantId.isBlank()) {
            throw FailedResult.MissingBody("tenant_id", "String")
        }
        if (creation.azureClientId.isBlank()) {
            throw FailedResult.MissingBody("azure_client_id", "String")
        }
        if (creation.azureClientSecret.isBlank()) {
            throw FailedResult.MissingBody("azure_client_secret", "String")
        }

        val user = current.User.id
        clientDao.findByName(user, name)?.let {
            throw FailedResult.Client.DuplicateClientName
        }

        oneDriveClientDao.findClient(
            clientDao.findByUser(user),
            creation.azureClientId,
            creation.azureClientSecret,
            creation.endPoint,
            creation.tenantId,
        )?.let {
            throw FailedResult.Client.DuplicateClientInfo(name, it.id)
        }

        val client = ClientsEntity(
            name = name,
            type = ClientType.OneDrive,
            createBy = user,
            supportDelta = creation.endPoint.supportDelta,
        )
        clientDao.save(client)
        oneDriveClientDao.save(
            OneDriveClientEntity(
                id = client.id,
                clientId = creation.azureClientId,
                clientSecret = creation.azureClientSecret,
                tenantId = creation.tenantId,
                endPoint = creation.endPoint,
            )
        )
    }

    @Transactional
    override fun edit(params: JsonObject, clientId: UUID) {
        getClient(clientId)
        val edition: ClientEditOneDrive = JsonGlobal.decodeFromJsonElement(params)

        clientDao.getClient(clientId)?.also {
            edition.name?.let { name ->
                if (it.name == name) {
                    return@also
                }
                if (name.isBlank()) {
                    throw FailedResult.MissingBody("name", "String")
                }
                it.name = name
            }
            clientDao.save(it)
        } ?: throw FailedResult.Client.NotFound
        oneDriveClientDao.getClient(clientId).also {
            edition.clientSecret?.let { secret ->
                if (it.clientSecret == secret) {
                    return@also
                }
                if (secret.isBlank()) {
                    throw FailedResult.MissingBody("client_secret", "String")
                }
                it.clientSecret = secret
            }
            oneDriveClientDao.save(it)
        }
    }

    override fun listFile(path: CanonicalPath, accountId: UUID): RespResult<JsonArray> {
        val account = getAccount(accountId)
        TODO()
    }

    override fun downloadFile(path: CanonicalPath, accountId: UUID): RespResult<String> {
        TODO()
    }

    override fun needDelta(accountId: UUID): Boolean {
        val account = accountDao.getAccount(accountId) ?: return false
        if (System.currentTimeMillis() < account.lastSuccessDelta + account.deltaTick) {
            return false
        }
        return !account.accountExpired
    }

    @Transactional
    override fun delta(accountId: UUID) {
        log.info("delta track 开始！account id：$accountId")
        val account = accountDao.getAccount(accountId) ?: return
        val client = oneDriveClientDao.getClient(account.parentClientId)
        val endPoint = client.endPoint

        val oneDriveAccount = oneDriveAccountDao.getOneDriveAccount(accountId)
        var delta = AzureGraphDtoV2_Me_Drive_Root_Delta(
            "token=${oneDriveAccount.deltaToken}".takeIf {
                oneDriveAccount.deltaToken != null
            }, null, listOf()
        )
        do {
            delta = endPoint.Graph.withCheckedToken(
                oneDriveAccountDao, oneDriveAccount, client
            ) { token ->
                if (delta.nextToken.isBlank()) {
                    log.debug("新的 delta")
                    Me_Drive_Root_Delta(token)
                } else {
                    log.debug("下一个 delta：${delta.nextToken}")
                    Me_Drive_Root_Delta(token, delta.nextToken)
                }
            }
            for (item in delta.value) {
                if (item.name.isBlank() || (item.file != null && item.file.hashes == null)) {
                    continue
                }
                val duplicateCheck = oneDriveFileDao.findByAzureFileId(item.id, account.id)
                if (duplicateCheck != null && item.deleted != null) {
                    log.info("delta 删除文件：${duplicateCheck}")
                    fileDao.deleteByUUID(duplicateCheck.id)
                    oneDriveFileDao.deleteByUUID(duplicateCheck.id)
                    continue
                }
                val parent = oneDriveFileDao.findByAzureFileId(
                    item.parentReference.id, account.id
                )
                val newId = duplicateCheck?.id ?: UUID.randomUUID()
                if (item.folder != null) {
                    log.info("delta 新增或修改文件夹：${item}")
                } else {
                    log.info("delta 新增或修改文件：${item}")
                }
                val path = if (parent?.id != null) {
                    fileDao.findByUUID(parent.id)!!
                        .path.append(item.name)
                } else {
                    CanonicalPath.ROOT
                }
                fileDao.save(FileEntity(
                    id = newId,
                    accountId = account.id,
                    name = item.name,
                    parentId = parent?.id,
                    isDir = item.folder != null,
                    createBy = null,
                    path = path,
                    pathHash = path.pathSha256,
                    clientType = type,
                    isRemote = true,
                    size = item.size,
                ))
                oneDriveFileDao.save(OneDriveFileEntity(
                    id = newId,
                    accountId = account.id,
                    fileId = item.id,
                    webUrl = item.webUrl,
                    mimeType = item.file?.mimeType ?: "directory",
                    quickXorHash = item.file?.hashes?.quickXorHash,
                    sha1Hash = item.file?.hashes?.sha1Hash,
                    sha256Hash = item.file?.hashes?.sha256Hash,
                ))
            }
        } while (delta.deltaToken == null)
        oneDriveAccount.deltaToken = delta.deltaToken
        oneDriveAccountDao.save(oneDriveAccount)
        account.lastSuccessDelta = System.currentTimeMillis()
        accountDao.save(account)
        log.info("delta track 结束！account id：$accountId")
    }

    @Serializable
    data class AccountLoginOneDrive(
        val code: String,
        val state: String,
    )

    @Serializable
    data class ClientCreateOneDrive(
        val azureClientId: String,
        val azureClientSecret: String,
        val endPoint: OneDriveClientEntity.EndPoint = OneDriveClientEntity.EndPoint.Global,
        val tenantId: String = "common",
    )

    @Serializable
    data class ClientEditOneDrive(
        val name: String?,
        val clientSecret: String?,
    )
}


/**
 * 扩展：转为 Microsoft Graph 接口中需要的路径参数。
 * <br/>- 若当前路径为根目录，则返回空文本；
 * <br/>- 若当前路径为不根目录，则在首位添加英文冒号返回，即：":${CanonicalPath#getPath()}:"。
 * @return 转换后的文本
 */
fun CanonicalPath.toAzureCanonicalizePath(): String {
    val canonicalizePath = SimpleCacheFactory.path
    return if (CanonicalPath.ROOT_PATH == canonicalizePath) "" else ":$canonicalizePath:"
}