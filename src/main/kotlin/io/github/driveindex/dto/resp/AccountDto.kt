package io.github.driveindex.dto.resp

import io.github.driveindex.dto.resp.account.AccountDetail


data class AccountDto(
    val id: Int,
    val displayName: String,
    val userPrincipalName: String,
    val createAt: Long,
    val modifyAt: Long,
    val detail: AccountDetail,
)

data class AccountCreateRespDto(
    val redirectUrl: String,
)
