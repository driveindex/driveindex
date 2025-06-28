package io.github.driveindex.controller

import io.github.driveindex.client.ClientType
import io.github.driveindex.core.util.SHA1
import io.github.driveindex.database.dao.AccountsDao
import io.github.driveindex.database.dao.ClientsDao
import io.github.driveindex.database.dao.attributes.OneDriveAttributeDao
import io.github.driveindex.database.dao.attributes.OneDriveClientDao
import io.github.driveindex.dto.req.user.*
import io.github.driveindex.dto.resp.*
import io.github.driveindex.exception.FailedResult
import io.github.driveindex.module.Current
import io.github.driveindex.module.DeletionModule
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import java.util.UUID

/**
 * @author sgpublic
 * @Date 2023/2/7 13:42
 */
@RestController
@Tag(name = "用户接口")
class UserConfController(
    private val current: Current,
    private val clientsDao: ClientsDao,
    private val accountsDao: AccountsDao,

    private val onedriveClientDao: OneDriveClientDao,
    private val onedriveAccountDao: OneDriveAttributeDao,

    private val deletionModule: DeletionModule,
) {
    private val passwordRegex = "^(?![^a-zA-Z]+\$)(?!D+\$).{8,16}\$".toRegex()
    @Operation(summary = "修改密码")
    @GetMapping("/api/user/password")
    fun setPassword(@RequestBody dto: SetPwdReqDto) {
        val config = current.User
        if (config.password != dto.oldPwd.SHA1) {
            throw FailedResult.UserSettings.PasswordNotMatched
        }
        if (config.password == dto.newPwd.SHA1) {
            throw FailedResult.UserSettings.PasswordMatched
        }
        if (!passwordRegex.matches(dto.newPwd)) {
            throw FailedResult.UserSettings.PasswordFormat
        }
        current.User = config.also {
            it.password = dto.newPwd.SHA1
        }
    }

    @Operation(summary = "常规设置")
    @GetMapping("/api/user/common")
    fun getCommonSettings(): CommonSettingsRespDto {
        val config = current.User
        return CommonSettingsRespDto()
    }

    @Operation(summary = "常规设置")
    @PostMapping("/api/user/common")
    fun setCommonSettings(
        @RequestBody dto: CommonSettingsReqDto
    ) {
        current.User = current.User.also {

        }
    }


    @Operation(summary = "枚举 Client 配置")
    @GetMapping("/api/user/client")
    fun listClients(): List<ClientsDto> {
        val list: ArrayList<ClientsDto> = ArrayList()
        for (entity in clientsDao.listByUser(current.User.id)) {
            list.add(ClientsDto(
                id = entity.clientId,
                name = entity.name,
                type = entity.type,
                createAt = entity.createAt,
                modifyAt = entity.modifyAt,
                detail = when (entity.type) {
                    ClientType.OneDrive ->
                        onedriveClientDao.getClient(entity.clientId).let {
                            return@let ClientsDto.OneDriveClientDetail(
                                clientId = it.clientId,
                                tenantId = it.tenantId,
                                endPoint = it.endPoint,
                            )
                        }
                }
            ))
        }
        return list
    }

    @Operation(summary = "枚举 Client 下登录的账号")
    @GetMapping("/api/user/account")
    fun listAccount(@RequestParam("client_id") clientId: UUID): List<AccountsDto> {
        val client = clientsDao.getClient(clientId)
            ?: throw FailedResult.Client.NotFound
        val list: ArrayList<AccountsDto> = ArrayList()
        for (entity in accountsDao.listByClient(client.clientId)) {
            list.add(AccountsDto(
                id = entity.id,
                displayName = entity.displayName,
                userPrincipalName = entity.userPrincipalName,
                createAt = entity.createAt,
                modifyAt = entity.modifyAt,
                detail = when (client.type) {
                    ClientType.OneDrive ->
                        onedriveAccountDao.getOneDriveAccount(entity.id).let {
                            return@let AccountsDto.OneDriveAccountDetail(
                                azureUserId = it.azureUserId,
                            )
                        }
                }
            ))
        }
        return list
    }

    @Operation(summary = "删除 Client 下登录的账号")
    @PostMapping("/api/user/account/delete")
    fun deleteAccount(@RequestBody dto: AccountDeleteReqDto) {
        deletionModule.doAccountDeleteAction(dto.accountId)
    }

    @Operation(summary = "创建 Client")
    @PostMapping("/api/user/client")
    fun createClient(@RequestBody dto: ClientCreateReqDto) {
        if (dto.name.isBlank()) {
            throw FailedResult.MissingBody("name", "String")
        }
        dto.type.create(dto.name, dto.data)
    }

    @Operation(summary = "修改 Client")
    @PostMapping("/api/user/client/edit")
    fun editClient(@RequestBody dto: ClientEditReqDto) {
        dto.clientType.edit(dto.data, dto.clientId)
    }

    @Operation(summary = "删除 Client")
    @PostMapping("/api/user/client/delete")
    fun deleteClient(@RequestBody dto: ClientDeleteReqDto) {
        val client = clientsDao.getClient(dto.clientId)
            ?: throw FailedResult.Client.NotFound
        deletionModule.doClientDeleteAction(client.clientId)
    }
}

fun String.checkNick(): String {
    if (length > 50) {
        throw FailedResult.User.NickInvalid
    }
    return this
}

fun String.checkUsername(): String {
    val username = lowercase()
    if (!username.matches("^(?!.*\\.{2,})(?!.*[áàâäãåæéèêëíìîïóòôöõøúùûüýÿ&=<>+,!])(?!.*[.])[a-zA-Z0-9]+\$".toRegex())) {
        throw FailedResult.User.UserInvalid
    } else if (username.startsWith("!")) {
        throw FailedResult.User.UserInvalid
    } else if (username.length > 32) {
        throw FailedResult.User.UserInvalid
    }
    return username
}