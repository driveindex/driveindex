package io.github.driveindex.dto.req.user

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID


class CommonSettingsReqDto()

data class AccountDeleteReqDto(
    @JsonProperty("account_id")
    val accountId: UUID
)

data class ClientListReqDto(
    @JsonProperty("client_id")
    val clientId: UUID
)
