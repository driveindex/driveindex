package io.github.driveindex.controller

import com.vaadin.hilla.BrowserCallable
import io.github.driveindex.core.annotation.AllOpen
import io.github.driveindex.core.exception.FailedResult
import io.github.driveindex.drivers.DriverRegistry
import io.github.driveindex.dto.req.user.*
import io.github.driveindex.dto.resp.*
import io.github.driveindex.module.Current
import io.github.driveindex.module.UserModel
import io.github.driveindex.security.SecurityConfig
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.annotation.security.RolesAllowed

/**
 * @author sgpublic
 * @Date 2023/2/7 13:42
 */
@AllOpen
@BrowserCallable
@Tag(name = "用户接口")
@RolesAllowed(SecurityConfig.ROLE_USER)
class UserConfController(
    private val userModel: UserModel,
    private val clientLoader: DriverRegistry,
    private val current: Current,
) {
    @Operation(summary = "修改密码")
    fun setPassword(dto: SetPwdReqDto) = Resp {
        userModel.setPassword(dto)
    }

    @Operation(summary = "常规设置")
    fun getUserInfo() = ObjResp {
        userModel.getUser()
    }

    @Operation(summary = "常规设置")
    fun setCommonSettings(dto: CommonSettingsReqDto) = Resp {
        userModel.updateUser(dto)
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