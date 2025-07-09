package io.github.driveindex.dto.resp

import java.util.UUID
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo


data class AccountsDto(
    val id: UUID,
    val displayName: String,
    val userPrincipalName: String,
    val createAt: Long,
    val modifyAt: Long?,
    val detail: Detail,
): RespResultData {
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
    @JsonSubTypes(
        JsonSubTypes.Type(value = OneDriveAccountDetail::class, name = "onedrive"),
    )
    sealed interface Detail

    data class OneDriveAccountDetail(
            val azureUserId: String,
    ): Detail
}

data class AccountCreateRespDto(
    val redirectUrl: String,
): RespResultData
