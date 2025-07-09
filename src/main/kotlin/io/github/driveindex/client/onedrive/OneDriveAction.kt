package io.github.driveindex.client.onedrive

import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import io.github.driveindex.Application.Companion.Config
import io.github.driveindex.client.AbsClientAction
import io.github.driveindex.client.ClientType
import io.github.driveindex.core.util.*
import io.github.driveindex.database.dao.use
import io.github.driveindex.database.entity.client.ClientEntity
import io.github.driveindex.database.entity.client.attributes.OneDriveClientAttribute
import io.github.driveindex.dto.resp.AccountCreateRespDto
import io.github.driveindex.exception.FailedResult
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.RequestBody
import java.net.URLEncoder

@Component
class OneDriveAction: AbsClientAction(ClientType.OneDrive) {
    override fun loginUri(
        clientId: Int, redirectUri: String
    ): AccountCreateRespDto {
        ClientEntity.use(getClient(clientId)) {
            val state = linkedMapOf<String, Any>(
                "id" to clientId,
                "ts" to System.currentTimeMillis(),
                "type" to it[type],
                "redirect_uri" to redirectUri,
            )
            state["sign"] = "${state.joinToSortedString("&")}${Config.token.jwtSecurity}".MD5_FULL
            val attr = it[attribute] as OneDriveClientAttribute
            val loginUrl = attr.endPoint.loginUrl("/${attr.tenantId}/oauth2/v2.0/authorize",
                "client_id" to attr.clientId,
                "response_type" to "code",
                "redirect_uri" to URLEncoder.encode(redirectUri, Charsets.UTF_8),
                "response_mode" to "query",
                "scope" to AzureAuthClient.Scope.joinToString("%20"),
                "state" to state.joinToString("&").BASE64)
            return AccountCreateRespDto(
                redirectUrl = loginUrl,
            )
        }
    }

    override fun loginRequest(@RequestBody params: ObjectNode) {
        val dto: AccountLoginOneDrive = Json.treeToValue(params)
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
            param["id"]!!.toInt()
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
            Config.token.jwtSecurity
        }".MD5_FULL
        if (originSign != sign) {
            throw FailedResult.Auth.IllegalRequest
        }

        TODO()
//        val client = ClientEntity.getClient(clientId, type)
//            ?: throw FailedResult.Auth.IllegalRequest
//        client.withAttribute { entity, attr: OneDriveClientAttribute ->
//            param["type"]?.let type@{
//                return@type ClientType.valueOf(it)
//            }?.takeIf {
//                client.type == it
//            } ?: throw FailedResult.Auth.IllegalRequest
//
//            val token = attr.endPoint.Auth.getToken(
//                attr.tenantId,
//                dto.code,
//                attr.clientId,
//                attr.clientSecret,
//                redirectUri
//            )
//
//            val me = attr.endPoint.Graph.Me("${token.tokenType} ${token.accessToken}")
//
//            // 允许账号失效后重新登录
//            oneDriveAccountDao.findByAzureId(
//                me.id
//            )?.withAttribute { entity, attr: OneDriveAccountAttribute ->
//                attr.tokenType = token.tokenType
//                attr.accessToken = token.accessToken
//                attr.refreshToken = token.refreshToken
//                attr.tokenExpire = token.expires
//                entity.writeAttribute(attr)
//                oneDriveAccountDao.save(entity)
//
//                accountDao.getAccount(entity.id)?.let {
//                    it.expired = false
//                    accountDao.save(it)
//                }
//            }
//
//            // TODO 重名时自动重命名
//            accountDao.findByName(attr.clientId, me.displayName)?.let {
//                throw FailedResult.Client.DuplicateAccountName(it.name, it.id)
//            }
//
//            val entity = AccountEntity(
//                clientId = client.clientId,
//                displayName = me.displayName,
//                createBy = current.User.id,
//            )
//            accountDao.save(entity)
//            oneDriveAccountDao.save(
//                OneDriveAccountEntity(
//                    id = entity.id,
//                    azureUserId = me.id,
//                    tokenType = token.tokenType,
//                    accessToken = token.accessToken,
//                    refreshToken = token.refreshToken,
//                    tokenExpire = token.expires,
//                )
//            )
//        }
    }

    override fun create(name: String, params: ObjectNode) {
        TODO()
//        val creation: ClientCreateOneDrive = Json.treeToValue(params)
//
//        if (creation.tenantId.isBlank()) {
//            creation.tenantId = "common"
//        }
//        if (creation.azureClientId.isBlank()) {
//            throw FailedResult.MissingBody("azure_client_id", "String")
//        }
//        if (creation.azureClientSecret.isBlank()) {
//            throw FailedResult.MissingBody("azure_client_secret", "String")
//        }
//
//        val user = current.User.id
//        clientDao.findByName(user, name)?.let {
//            throw FailedResult.Client.DuplicateClientName
//        }
//
//        oneDriveClientDao.findClient(
//            clientDao.findByUser(user),
//            creation.azureClientId,
//            creation.azureClientSecret,
//            creation.endPoint,
//            creation.tenantId,
//        )?.let {
//            throw FailedResult.Client.DuplicateClientInfo(name, it.id)
//        }
//
//        val client = ClientEntity(
//            name = name,
//            type = ClientType.OneDrive,
//            createBy = user,
//            supportDelta = creation.endPoint.supportDelta,
//        )
//        clientDao.save(client)
//        oneDriveClientDao.save(
//            OneDriveClientEntity(
//                id = client.clientId,
//                clientId = creation.azureClientId,
//                clientSecret = creation.azureClientSecret,
//                tenantId = creation.tenantId,
//                endPoint = creation.endPoint,
//            )
//        )
    }

    override fun edit(params: ObjectNode, clientId: Int) {
        TODO()
//        getClient(clientId)
//        val edition: ClientEditOneDrive = Json.treeToValue(params)
//
//        clientDao.getClient(clientId)?.also {
//            edition.name?.let { name ->
//                if (it.name == name) {
//                    return@also
//                }
//                if (name.isBlank()) {
//                    throw FailedResult.MissingBody("name", "String")
//                }
//                it.name = name
//            }
//            clientDao.save(it)
//        } ?: throw FailedResult.Client.NotFound
//        oneDriveClientDao.getClient(clientId).also {
//            edition.clientSecret?.let { secret ->
//                if (it.clientSecret == secret) {
//                    return@also
//                }
//                if (secret.isBlank()) {
//                    throw FailedResult.MissingBody("client_secret", "String")
//                }
//                it.clientSecret = secret
//            }
//            oneDriveClientDao.save(it)
//        }
    }

    override fun listFile(path: CanonicalPath, accountId: Int): ArrayNode {
//        val account = getAccount(accountId)
        TODO()
    }

    override fun downloadFile(path: CanonicalPath, accountId: Int): String {
        TODO()
    }

    override fun needDelta(accountId: Int): Boolean {
        TODO()
//        val account = accountDao.getAccount(accountId) ?: return false
//        if (System.currentTimeMillis() < account.lastSuccessDelta + account.deltaTick) {
//            return false
//        }
//        return !account.expired
    }

    override fun delta(accountId: Int) {
        TODO()
//        log.info("delta track 开始！account id：$accountId")
//        val account = accountDao.getAccount(accountId) ?: return
//        val client = oneDriveClientDao.getClient(account.clientId)
//        val endPoint = client.endPoint
//
//        val oneDriveAccount = oneDriveAccountDao.getOneDriveAccount(accountId)
//        var delta = AzureGraphDtoV2_Me_Drive_Root_Delta(
//            "token=${oneDriveAccount.deltaToken}".takeIf {
//                oneDriveAccount.deltaToken != null
//            }, null, listOf()
//        )
//        do {
//            delta = endPoint.Graph.withCheckedToken(
//                oneDriveAccountDao,
//                oneDriveAccount,
//                client
//            ) { token ->
//                if (delta.nextToken.isBlank()) {
//                    log.debug("新的 delta")
//                    Me_Drive_Root_Delta(token)
//                } else {
//                    log.debug("下一个 delta：${delta.nextToken}")
//                    Me_Drive_Root_Delta(token, delta.nextToken)
//                }
//            }
//            for (item in delta.value) {
//                if (item.name.isBlank() || (item.file != null && item.file.hashes == null)) {
//                    continue
//                }
//                val duplicateCheck = oneDriveFileDao.findByAzureFileId(item.id, account.id)
//                if (duplicateCheck != null && item.deleted != null) {
//                    log.info("delta 删除文件：${duplicateCheck}")
//                    fileDao.deleteById(duplicateCheck.id)
//                    oneDriveFileDao.deleteByUUID(duplicateCheck.id)
//                    continue
//                }
//                val parent = oneDriveFileDao.findByAzureFileId(
//                    item.parentReference?.id ?: continue, account.id
//                )
//                val newId = duplicateCheck?.id ?: UUID.randomUUID()
//                if (item.folder != null) {
//                    log.info("delta 新增或修改文件夹：${item}")
//                } else {
//                    log.info("delta 新增或修改文件：${item}")
//                }
//                val path = if (parent?.id != null) {
//                    fileDao.findById(parent.id)!!
//                        .path.append(item.name)
//                } else {
//                    CanonicalPath.ROOT
//                }
//                fileDao.save(
//                    FileEntity(
//                        id = newId,
//                        accountId = account.id,
//                        name = item.name,
//                        parentId = parent?.id,
//                        isDir = item.folder != null,
//                        createBy = null,
//                        path = path,
//                        pathHash = path.pathSha256,
//                        clientType = type,
//                        isRemote = true,
//                        size = item.size,
//                    )
//                )
//                oneDriveFileDao.save(OneDriveFileEntity(
//                    id = newId,
//                    accountId = account.id,
//                    fileId = item.id,
//                    webUrl = item.webUrl ?: continue,
//                    mimeType = item.file?.mimeType ?: "directory",
//                    quickXorHash = item.file?.hashes?.quickXorHash,
//                    sha1Hash = item.file?.hashes?.sha1Hash,
//                    sha256Hash = item.file?.hashes?.sha256Hash,
//                ))
//            }
//        } while (delta.deltaToken == null)
//        oneDriveAccount.deltaToken = delta.deltaToken
//        oneDriveAccountDao.save(oneDriveAccount)
//        account.lastSuccessDelta = System.currentTimeMillis()
//        accountDao.save(account)
//        log.info("delta track 结束！account id：$accountId")
    }

    data class AccountLoginOneDrive(
        val code: String,
        val state: String,
    )

    data class ClientCreateOneDrive(
        val azureClientId: String,
        val azureClientSecret: String,
        val endPoint: OneDriveEndpoint = OneDriveEndpoint.Global,
        var tenantId: String = "common",
    )

    data class ClientEditOneDrive(
        val name: String?,
        val clientSecret: String?,
    )
}


/**
 * 扩展：转为 Microsoft Graph 接口中需要的路径参数。
 * <br/>- 若当前路径为根目录，则返回空文本；
 * <br/>- 若当前路径为不根目录，则在首尾添加英文冒号返回，即：":${CanonicalPath#getPath()}:"。
 * @return 转换后的文本
 */
fun CanonicalPath.toAzureCanonicalizePath(): String {
    return if (CanonicalPath.ROOT_PATH == path) "" else ":$path:"
}