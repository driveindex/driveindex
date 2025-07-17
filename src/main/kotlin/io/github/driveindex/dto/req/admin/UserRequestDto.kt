package io.github.driveindex.dto.req.admin

import io.github.driveindex.security.UserPermission
import io.github.driveindex.security.UserRole
import java.util.*

/**
 * @author sgpublic
 * @Date 8/5/23 12:07 PM
 */

data class UserCreateRequestDto(
    val username: String,
    val password: String,
    val nickname: String? = null,
    val role: UserRole = UserRole.USER,
    val permission: Set<UserPermission>,
    val enabled: Boolean = true,
)

data class UserDeleteRequestDto(
    val userId: UUID,
)