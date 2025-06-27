package io.github.driveindex.dto.req.user

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID


data class CommonSettingsReqDto(
    @JsonProperty("nick")
    val nick: String? = null,
    @JsonProperty("cors_origin")
    val corsOrigin: String? = null,
)

data class AccountDeleteReqDto(
    @JsonProperty("account_id")
    val accountId: UUID
)

data class ClientListReqDto(
    @JsonProperty("client_id")
    val clientId: UUID
)
