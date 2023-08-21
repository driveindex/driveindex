package io.github.driveindex.dto.req.admin

import io.github.driveindex.core.util.KUUID
import io.github.driveindex.security.UserRole
import kotlinx.serialization.Serializable

/**
 * @author sgpublic
 * @Date 8/5/23 12:07 PM
 */

@Serializable
data class UserCreateRequestDto(
    val username: String,
    val password: String,
    val nick: String = "",
    val role: UserRole = UserRole.USER,
    val enable: Boolean = true,
)

@Serializable
data class UserDeleteRequestDto(
    val userId: KUUID,
)