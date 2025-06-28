package io.github.driveindex.client.onedrive

import io.github.driveindex.core.util.log
import io.github.driveindex.database.dao.attributes.OneDriveAttributeDao
import io.github.driveindex.database.entity.account.AccountEntity
import io.github.driveindex.database.entity.account.attributes.OneDriveAccountAttribute
import io.github.driveindex.database.entity.client.ClientEntity
import io.github.driveindex.database.entity.client.attributes.OneDriveClientAttribute
import io.github.driveindex.database.entity.readAttribute
import io.github.driveindex.database.entity.writeAttribute
import io.github.driveindex.dto.feign.AzureGraphDtoV2_Me
import io.github.driveindex.dto.feign.AzureGraphDtoV2_Me_Drive_Root_Delta
import io.github.driveindex.exception.AzureDecodeException
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam

/**
 * @author sgpublic
 * @Date 2023/8/13 12:20
 */
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
    oneDriveAccountDao: OneDriveAttributeDao,
    account: AccountEntity,
    client: ClientEntity,
    block: AzureGraphClient.(String) -> T,
): T {
    val accountAttribute: OneDriveAccountAttribute = account.readAttribute()
    val clientAttribute: OneDriveClientAttribute = client.readAttribute()

    var refreshActionInvoked = false
    val refreshAction = {
        val newToken = try {
            clientAttribute.endPoint.Auth.refreshToken(
                clientAttribute.tenantId, clientAttribute.clientId,
                clientAttribute.clientSecret, accountAttribute.refreshToken
            )
        } catch (e: AzureDecodeException) {
            log.warn("刷新 Azure 账号 token 失败", e)
            throw e
        }
        accountAttribute.accessToken = newToken.accessToken
        accountAttribute.refreshToken = newToken.refreshToken
        accountAttribute.tokenExpire = newToken.expires
        accountAttribute.tokenType = newToken.tokenType
        account.writeAttribute(accountAttribute)
        oneDriveAccountDao.save(account)
        refreshActionInvoked = true
    }
    return try {
        if (accountAttribute.tokenExpire < System.currentTimeMillis()) {
            refreshAction.invoke()
        }
        block.invoke(this, accountAttribute.accessToken)
    } catch (e: AzureDecodeException) {
        if (e.code != "InvalidAuthenticationToken" || refreshActionInvoked) {
            throw e
        }
        refreshAction.invoke()
        block.invoke(this, accountAttribute.accessToken)
    }
}
