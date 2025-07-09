package io.github.driveindex.dto.req.admin

import io.github.driveindex.security.UserRole
import java.util.*

/**
 * @author sgpublic
 * @Date 8/5/23 12:07 PM
 */

data class UserCreateRequestDto(
    val username: String,
    val password: String,
    val nick: String = "",
    val role: UserRole = UserRole.USER,
    val enabled: Boolean = true,
)

data class UserDeleteRequestDto(
    val userId: UUID,
)