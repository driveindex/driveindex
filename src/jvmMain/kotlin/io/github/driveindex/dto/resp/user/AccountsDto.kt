package io.github.driveindex.dto.resp.user

import io.github.driveindex.core.util.KUUID
import kotlinx.serialization.Serializable

@Serializable
data class AccountsDto<T: AccountDetail>(
    val id: KUUID,
    val displayName: String,
    val userPrincipalName: String,
    val createAt: Long,
    val modifyAt: Long?,
    val detail: T,
)

@Serializable
sealed class AccountDetail

@Serializable
data class OneDriveAccountDetail(
    val azureUserId: String,
): AccountDetail()