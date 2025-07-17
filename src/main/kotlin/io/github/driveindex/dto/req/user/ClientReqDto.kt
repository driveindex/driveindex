package io.github.driveindex.dto.req.user

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.node.ObjectNode
import io.github.driveindex.core.client.ClientType
import java.util.UUID

data class ClientCreateReqDto(
    @param:JsonProperty("name")
    val name: String,
    @param:JsonProperty("type")
    val type: ClientType,
    @param:JsonProperty("data")
    val data: ObjectNode,
)

data class ClientEditReqDto(
    @param:JsonProperty("client_id")
    val clientId: UUID,
    @param:JsonProperty("client_type")
    val clientType: ClientType,
    @param:JsonProperty("data")
    val data: ObjectNode,
)

data class ClientDeleteReqDto(
    @param:JsonProperty("client_id")
    val clientId: UUID,
)
