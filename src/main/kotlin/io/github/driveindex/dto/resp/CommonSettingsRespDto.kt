package io.github.driveindex.dto.resp

import io.github.driveindex.security.UserRole

class CommonSettingsRespDto(): RespResultData

data class CommonSettingsUserItemRespDto(
    val id: Int,
    val username: String,
    val role: UserRole,
    val enabled: Boolean,
): RespResultData

data class FullSettingsRespDto(
    val id: Int,
    val username: String? = null,
    val password: String? = null,
    val role: UserRole? = null,
    val enabled: Boolean? = null,
): RespResultData
