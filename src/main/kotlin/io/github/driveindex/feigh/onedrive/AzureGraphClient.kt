package io.github.driveindex.feigh.onedrive

import io.github.driveindex.core.util.log
import io.github.driveindex.database.dao.onedrive.OneDriveAccountDao
import io.github.driveindex.database.entity.onedrive.OneDriveAccountEntity
import io.github.driveindex.database.entity.onedrive.OneDriveClientEntity
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
    oneDriveAccountDao: OneDriveAccountDao,
    target: OneDriveAccountEntity,
    oneDriveClientEntity: OneDriveClientEntity,
    block: AzureGraphClient.(String) -> T,
): T {
    var refreshActionInvoked = false
    val refreshAction = {
        val newToken = try {
            oneDriveClientEntity.endPoint.Auth.refreshToken(
                oneDriveClientEntity.tenantId, oneDriveClientEntity.clientId,
                oneDriveClientEntity.clientSecret, target.refreshToken
            )
        } catch (e: AzureDecodeException) {
            log.warn("刷新 Azure 账号 token 失败", e)
            throw e
        }
        target.accessToken = newToken.accessToken
        target.refreshToken = newToken.refreshToken
        target.tokenExpire = newToken.expires
        target.tokenType = newToken.tokenType
        oneDriveAccountDao.save(target)
        refreshActionInvoked = true
    }
    return try {
        if (target.tokenExpire < System.currentTimeMillis()) {
            refreshAction.invoke()
        }
        block.invoke(this, target.accessToken)
    } catch (e: AzureDecodeException) {
        if (e.code != "InvalidAuthenticationToken" || refreshActionInvoked) {
            throw e
        }
        refreshAction.invoke()
        block.invoke(this, target.accessToken)
    }
}
