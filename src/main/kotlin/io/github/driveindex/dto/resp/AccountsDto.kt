package io.github.driveindex.dto.resp

import java.util.UUID
import com.fasterxml.jackson.annotation.JsonProperty


data class AccountsDto(
    @JsonProperty("id")
    val id: UUID,
    @JsonProperty("display_name")
    val displayName: String,
    @JsonProperty("user_principal_name")
    val userPrincipalName: String,
    @JsonProperty("create_at")
    val createAt: Long,
    @JsonProperty("modify_at")
    val modifyAt: Long?,
    @JsonProperty("detail")
    val detail: Detail,
): RespResultData {
    sealed interface Detail

    data class OneDriveAccountDetail(
            @JsonProperty("azure_user_id")
            val azureUserId: String,
    ): Detail
}

data class AccountCreateRespDto(
    @JsonProperty("redirect_url")
    val redirectUrl: String,
): RespResultData
