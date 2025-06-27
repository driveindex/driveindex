package io.github.driveindex.dto.resp

import io.github.driveindex.client.ClientType
import java.util.UUID
import io.github.driveindex.database.entity.onedrive.OneDriveClientEntity
import com.fasterxml.jackson.annotation.JsonProperty

data class ClientsDto(
    @JsonProperty("id")
    val id: UUID,
    @JsonProperty("name")
    var name: String,
    @JsonProperty("type")
    val type: ClientType,
    @JsonProperty("create_at")
    val createAt: Long,
    @JsonProperty("modify_at")
    val modifyAt: Long?,
    @JsonProperty("detail")
    val detail: Detail,
): RespResultData {
    sealed interface Detail: RespResultData

    data class OneDriveClientDetail(
            @JsonProperty("client_id")
            val clientId: String,
            @JsonProperty("tenant_id")
            val tenantId: String,
            @JsonProperty("end_point")
            val endPoint: OneDriveClientEntity.EndPoint,
    ): Detail
}
