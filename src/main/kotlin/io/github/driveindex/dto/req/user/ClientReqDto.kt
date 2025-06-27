package io.github.driveindex.dto.req.user

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.node.ObjectNode
import io.github.driveindex.client.ClientType
import java.util.UUID

data class ClientCreateReqDto(
    @JsonProperty("name")
    val name: String,
    @JsonProperty("type")
    val type: ClientType,
    @JsonProperty("data")
    val data: ObjectNode,
)

data class ClientEditReqDto(
    @JsonProperty("client_id")
    val clientId: UUID,
    @JsonProperty("client_type")
    val clientType: ClientType,
    @JsonProperty("data")
    val data: ObjectNode,
)

data class ClientDeleteReqDto(
    @JsonProperty("client_id")
    val clientId: UUID,
)
