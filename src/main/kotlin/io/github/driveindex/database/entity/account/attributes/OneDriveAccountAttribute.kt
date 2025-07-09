package io.github.driveindex.database.entity.account.attributes

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("onedrive")
data class OneDriveAccountAttribute(
    @SerialName("token_type")
    var tokenType: String,

    @SerialName("access_token")
    var accessToken: String,

    @SerialName("token_expire")
    var tokenExpire: Long,

    @SerialName("refresh_token")
    var refreshToken: String,

    @SerialName("delta_token")
    var deltaToken: String? = null,
): AccountAttribute
