package io.github.driveindex.dto.resp.admin

import io.github.driveindex.core.util.KUUID
import io.github.driveindex.security.UserRole
import kotlinx.serialization.Serializable

@Serializable
data class CommonSettingsRespDto(
    val nick: String,
    val corsOrigin: String,
)

@Serializable
data class CommonSettingsUserItemRespDto(
    val id: KUUID,
    val username: String,
    val nick: String,
    val role: UserRole,
    val enable: Boolean,
    val corsOrigin: String,
)

@Serializable
data class FullSettingsRespDto(
    val id: KUUID,
    val username: String? = null,
    val password: String? = null,
    val nick: String? = null,
    val corsOrigin: String? = null,
    val role: UserRole? = null,
    val enable: Boolean? = null,
)
