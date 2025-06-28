package io.github.driveindex.dto.resp

import java.util.UUID
import io.github.driveindex.security.UserRole
import com.fasterxml.jackson.annotation.JsonProperty

class CommonSettingsRespDto(): RespResultData

data class CommonSettingsUserItemRespDto(
    @JsonProperty("id")
    val id: UUID,
    @JsonProperty("username")
    val username: String,
    @JsonProperty("role")
    val role: UserRole,
    @JsonProperty("enable")
    val enable: Boolean,
): RespResultData

data class FullSettingsRespDto(
    @JsonProperty("id")
    val id: UUID,
    @JsonProperty("username")
    val username: String? = null,
    @JsonProperty("password")
    val password: String? = null,
    @JsonProperty("role")
    val role: UserRole? = null,
    @JsonProperty("enable")
    val enable: Boolean? = null,
): RespResultData
