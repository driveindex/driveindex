package io.github.driveindex.dto.resp

import java.util.UUID
import io.github.driveindex.security.UserRole
import com.fasterxml.jackson.annotation.JsonProperty

data class CommonSettingsRespDto(
    @JsonProperty("nick")
    val nick: String,
    @JsonProperty("cors_origin")
    val corsOrigin: String,
): RespResultData

data class CommonSettingsUserItemRespDto(
    @JsonProperty("id")
    val id: UUID,
    @JsonProperty("username")
    val username: String,
    @JsonProperty("nick")
    val nick: String,
    @JsonProperty("role")
    val role: UserRole,
    @JsonProperty("enable")
    val enable: Boolean,
    @JsonProperty("cors_origin")
    val corsOrigin: String,
): RespResultData

data class FullSettingsRespDto(
    @JsonProperty("id")
    val id: UUID,
    @JsonProperty("username")
    val username: String? = null,
    @JsonProperty("password")
    val password: String? = null,
    @JsonProperty("nick")
    val nick: String? = null,
    @JsonProperty("cors_origin")
    val corsOrigin: String? = null,
    @JsonProperty("role")
    val role: UserRole? = null,
    @JsonProperty("enable")
    val enable: Boolean? = null,
): RespResultData
