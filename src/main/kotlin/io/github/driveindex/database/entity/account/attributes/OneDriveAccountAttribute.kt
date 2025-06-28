package io.github.driveindex.database.entity.account.attributes

import com.fasterxml.jackson.annotation.JsonProperty

data class OneDriveAccountAttribute(
    @JsonProperty("azure_user_id")
    val azureUserId: String,

    @JsonProperty("token_type")
    var tokenType: String,

    @JsonProperty("access_token")
    var accessToken: String,

    @JsonProperty("token_expire")
    var tokenExpire: Long,

    @JsonProperty("refresh_token")
    var refreshToken: String,

    @JsonProperty("delta_token")
    var deltaToken: String? = null,
): AccountAttribute
