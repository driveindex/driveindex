package io.github.driveindex.dto.req.user

import java.util.*


class CommonSettingsReqDto()

data class AccountDeleteReqDto(
    val accountId: UUID
)

data class ClientListReqDto(
    val clientId: UUID
)
