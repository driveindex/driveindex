package io.github.driveindex.controller

import com.vaadin.hilla.BrowserCallable
import io.github.driveindex.annotation.AllOpen
import io.github.driveindex.core.util.runIfNotNull
import io.github.driveindex.database.entity.UserEntity
import io.github.driveindex.dto.req.user.*
import io.github.driveindex.dto.resp.*
import io.github.driveindex.exception.FailedResult
import io.github.driveindex.module.MutableCurrent
import io.github.driveindex.module.file.FileModel
import io.github.driveindex.security.ReadonlyDriveIndexUserDetails
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.RequestBody

/**
 * @author sgpublic
 * @Date 2023/2/7 13:42
 */
@AllOpen
@BrowserCallable
@Tag(name = "用户接口")
class UserConfController(
    private val current: MutableCurrent,
    private val fileModel: FileModel,
) {
    @Operation(summary = "修改密码")
    fun setPassword(dto: SetPwdReqDto) = Resp {
        TODO()
//        val config = current.User
//        if (config.passwordHash != "${dto.oldPwd}${config.passwordSalt}".SHA_256) {
//            throw FailedResult.UserSettings.PasswordNotMatched
//        }
//        if (config.password == dto.newPwd.SHA1) {
//            throw FailedResult.UserSettings.PasswordMatched
//        }
//        if (!passwordRegex.matches(dto.newPwd)) {
//            throw FailedResult.UserSettings.PasswordFormat
//        }
//        current.User = config.also {
//            it.password = dto.newPwd.SHA1
//        }
    }

    @Operation(summary = "常规设置")
    fun getUserInfo() = ObjResp {
        val user = current.User
        return@ObjResp UserInfoRespDto(
            username = user[UserEntity.username],
            nickname = user[UserEntity.attribute].nickname,
            role = user[UserEntity.role],
            permission = user[UserEntity.permission]
        )
    }

    @Operation(summary = "常规设置")
    fun setCommonSettings(
        dto: CommonSettingsReqDto
    ) = Resp {
        current.User = (current.User as ReadonlyDriveIndexUserDetails).asMutable { user ->
            dto.username.runIfNotNull {
                user[username] = it
            }
            dto.nickname.runIfNotNull {
                user[attribute].nickname = it.checkNick()
            }
        }
    }


    @Operation(summary = "枚举 Client 配置")
    fun listClients() = ListResp<ClientDto> {
        TODO()
//        val list: ArrayList<ClientDto> = ArrayList()
//        for (entity in clientDao.listByUser(current.User.id)) {
//            list.add(ClientDto(
//                id = entity.id,
//                name = entity.name,
//                type = entity.type,
//                createAt = entity.createAt,
//                modifyAt = entity.modifyAt,
//                detail = when (entity.type) {
//                    ClientType.OneDrive -> onedriveClientDao.getClient(entity.id)
//                        .useAttribute { entity, attr: OneDriveClientAttribute ->
//                            return@useAttribute OneDriveClientDetail(
//                                clientId = attr.clientId,
//                                tenantId = attr.tenantId,
//                                endPoint = attr.endPoint,
//                            )
//                        }
//                }
//            ))
//        }
//        return list
    }

    @Operation(summary = "枚举 Client 下登录的账号")
    fun listAccount(clientId: Int) = ListResp<AccountDto> {
        TODO()
//        val client = clientDao.getClient(clientId)
//            ?: throw FailedResult.Client.NotFound
//        val list: ArrayList<AccountsDto> = ArrayList()
//        for (entity in accountDao.listByClient(client.clientId)) {
//            list.add(AccountsDto(
//                id = entity.id,
//                displayName = entity.name,
//                userPrincipalName = entity.userPrincipalName,
//                createAt = entity.createAt,
//                modifyAt = entity.modifyAt,
//                detail = when (client.type) {
//                    ClientType.OneDrive ->
//                        onedriveAccountDao.getOneDriveAccount(entity.id).let {
//                            return@let AccountsDto.OneDriveAccountDetail(
//                                azureUserId = it.azureUserId,
//                            )
//                        }
//                }
//            ))
//        }
//        return list
    }

    @Operation(summary = "删除 Client 下登录的账号")
    fun deleteAccount(dto: AccountDeleteReqDto) = Resp {
        TODO()
//        fileModel.doAccountDeleteAction(dto.accountId)
    }

    @Operation(summary = "创建 Client")
    fun createClient(@RequestBody dto: ClientCreateReqDto) = Resp {
        if (dto.name.isBlank()) {
            throw FailedResult.MissingBody("name", "String")
        }
        dto.type.create(dto.name, dto.data)
    }

    @Operation(summary = "修改 Client")
    fun editClient(@RequestBody dto: ClientEditReqDto) = Resp {
//        dto.clientType.edit(dto.data, dto.clientId)
    }

    @Operation(summary = "删除 Client")
    fun deleteClient(dto: ClientDeleteReqDto) = Resp {
        TODO()
//        val client = clientDao.getClient(dto.clientId)
//            ?: throw FailedResult.Client.NotFound
//        fileModel.doClientDeleteAction(client.clientId)
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