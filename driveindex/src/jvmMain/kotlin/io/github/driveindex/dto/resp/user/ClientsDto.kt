package io.github.driveindex.dto.resp.user

import io.github.driveindex.client.ClientType
import io.github.driveindex.core.util.KUUID
import io.github.driveindex.database.entity.onedrive.OneDriveClientEntity
import kotlinx.serialization.Serializable

@Serializable
data class ClientsDto<T: ClientDetail>(
    val id: KUUID,
    var name: String,
    val type: ClientType,
    val createAt: Long,
    val modifyAt: Long?,
    val detail: T,
)

@Serializable
sealed class ClientDetail

@Serializable
data class OneDriveClientDetail(
    val clientId: String,
    val tenantId: String,
    val endPoint: OneDriveClientEntity.EndPoint,
): ClientDetail()