package io.github.driveindex.dto.resp


data class AccountDto(
    val id: Int,
    val displayName: String,
    val userPrincipalName: String,
    val createAt: Long,
    val modifyAt: Long,
)

data class AccountCreateRespDto(
    val redirectUrl: String,
)
