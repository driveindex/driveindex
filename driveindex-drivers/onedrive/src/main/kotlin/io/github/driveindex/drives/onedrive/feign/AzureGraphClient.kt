package io.github.driveindex.drives.onedrive.feign

import io.github.driveindex.drives.onedrive.core.AzureGraphDtoV2_Me
import io.github.driveindex.drives.onedrive.core.AzureGraphDtoV2_Me_Drive_Root_Delta
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam


interface AzureGraphClient {
    @GetMapping("/v1.0/me")
    fun Me(@RequestHeader("Authorization") token: String): AzureGraphDtoV2_Me

    @GetMapping("/v1.0/me/drive/root/delta")
    fun Me_Drive_Root_Delta(
        @RequestHeader("Authorization") token: String,
        @RequestParam("token") deltaToken: String,
    ): AzureGraphDtoV2_Me_Drive_Root_Delta

    @GetMapping("/v1.0/me/drive/root/delta")
    fun Me_Drive_Root_Delta(
        @RequestHeader("Authorization") token: String,
    ): AzureGraphDtoV2_Me_Drive_Root_Delta
}

fun <T> AzureGraphClient.withCheckedToken(
//    oneDriveAccountDao: OneDriveAccountAttributeDao,
//    account: AccountEntity,
//    client: ClientEntity,
    block: AzureGraphClient.(String) -> T,
): T {
    TODO()
//    val accountAttribute: OneDriveAccountAttribute = account.readAttribute()
//    val clientAttribute: OneDriveClientAttribute = client.readAttribute()
//
//    var refreshActionInvoked = false
//    val refreshAction = {
//        val newToken = try {
//            clientAttribute.endPoint.Auth.refreshToken(
//                clientAttribute.tenantId, clientAttribute.clientId,
//                clientAttribute.clientSecret, accountAttribute.refreshToken
//            )
//        } catch (e: AzureDecodeException) {
//            log.warn("刷新 Azure 账号 token 失败", e)
//            throw e
//        }
//        accountAttribute.accessToken = newToken.accessToken
//        accountAttribute.refreshToken = newToken.refreshToken
//        accountAttribute.tokenExpire = newToken.expires
//        accountAttribute.tokenType = newToken.tokenType
//        account.writeAttribute(accountAttribute)
//        oneDriveAccountDao.save(account)
//        refreshActionInvoked = true
//    }
//    return try {
//        if (accountAttribute.tokenExpire < System.currentTimeMillis()) {
//            refreshAction.invoke()
//        }
//        block.invoke(this, accountAttribute.accessToken)
//    } catch (e: AzureDecodeException) {
//        if (e.code != "InvalidAuthenticationToken" || refreshActionInvoked) {
//            throw e
//        }
//        refreshAction.invoke()
//        block.invoke(this, accountAttribute.accessToken)
//    }
}
