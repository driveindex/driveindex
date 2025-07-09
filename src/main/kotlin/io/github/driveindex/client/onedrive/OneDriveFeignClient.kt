package io.github.driveindex.client.onedrive

import feign.form.FormProperty
import io.github.driveindex.core.util.log
import io.github.driveindex.database.dao.attributes.OneDriveAccountAttributeDao
import io.github.driveindex.database.entity.account.AccountEntity
import io.github.driveindex.database.entity.account.attributes.OneDriveAccountAttribute
import io.github.driveindex.database.entity.client.ClientEntity
import io.github.driveindex.database.entity.client.attributes.OneDriveClientAttribute
import io.github.driveindex.dto.feign.AzureGraphDtoV2_Me
import io.github.driveindex.dto.feign.AzureGraphDtoV2_Me_Drive_Root_Delta
import io.github.driveindex.dto.feign.AzurePortalDtoV1_Token
import io.github.driveindex.exception.AzureDecodeException
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam


/**
 * @author sgpublic
 * @Date 2022/8/8 15:46
 */



interface AzureAuthClient {
    @PostMapping("/{tenant}/oauth2/v2.0/token", consumes = [
        MediaType.APPLICATION_FORM_URLENCODED_VALUE
    ])
    fun getToken(
        @PathVariable("tenant")
        tenant: String,
        body: TokenGetDto,
    ): AzurePortalDtoV1_Token

    data class TokenGetDto(
        @FormProperty("code")
        var code: String,
        @FormProperty("client_id")
        var clientId: String,
        @FormProperty("client_secret")
        var clientSecret: String,
        @FormProperty("redirect_uri")
        var redirectUri: String,
        @FormProperty("scope")
        var scope: String = Scope.joinToString(" "),
        @FormProperty("grant_type")
        var grantType: String = "authorization_code",
    )

    @PostMapping("/{tenant}/oauth2/v2.0/token", consumes = [
        MediaType.APPLICATION_FORM_URLENCODED_VALUE
    ])
    fun refreshToken(
        @PathVariable("tenant")
        tenant: String,
        body: TokenRefreshDto,
    ): AzurePortalDtoV1_Token

    data class TokenRefreshDto(
        @FormProperty("client_id")
        var clientId: String,
        @FormProperty("client_secret")
        var clientSecret: String,
        @FormProperty("refresh_token")
        var refreshToken: String,
        @FormProperty("grant_type")
        var grantType: String = "refresh_token",
    )

    companion object {
        @JvmStatic
        val Scope: Array<String> = arrayOf(
            "User.Read",
            "Files.ReadWrite",
            "Files.ReadWrite.All",
            "offline_access",
            "Sites.ReadWrite.All",
        )
    }
}

fun AzureAuthClient.getToken(
    tenant: String,
    code: String,
    clientId: String,
    clientSecret: String,
    redirectUri: String,
): AzurePortalDtoV1_Token {
    return getToken(tenant, AzureAuthClient.TokenGetDto(code, clientId, clientSecret, redirectUri))
}
fun AzureAuthClient.refreshToken(
    tenant: String,
    clientId: String,
    clientSecret: String,
    refreshToken: String,
): AzurePortalDtoV1_Token {
    return refreshToken(tenant, AzureAuthClient.TokenRefreshDto(clientId, clientSecret, refreshToken))
}


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
    oneDriveAccountDao: OneDriveAccountAttributeDao,
    account: AccountEntity,
    client: ClientEntity,
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
