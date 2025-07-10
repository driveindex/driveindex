package io.github.driveindex.dto.req.user


data class CommonSettingsReqDto(
    val username: String? = null,
    val nickname: String? = null,
)

data class AccountDeleteReqDto(
    val accountId: Int
)

data class ClientListReqDto(
    val clientId: Int
)
