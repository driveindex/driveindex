package io.github.driveindex.dto.req.user

import io.github.driveindex.client.ClientType
import io.github.driveindex.core.util.KUUID
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class ClientCreateReqDto(
    val name: String,
    val type: ClientType,
    val data: JsonObject,
)

@Serializable
data class ClientEditReqDto(
    val clientId: KUUID,
    val clientType: ClientType,
    val data: JsonObject,
)

@Serializable
data class ClientDeleteReqDto(
    val clientId: KUUID,
)
