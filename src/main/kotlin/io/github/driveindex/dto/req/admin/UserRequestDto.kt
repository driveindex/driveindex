package io.github.driveindex.dto.req.admin

import com.fasterxml.jackson.annotation.JsonProperty
import io.github.driveindex.security.UserRole
import jakarta.annotation.Nonnull
import java.util.UUID

/**
 * @author sgpublic
 * @Date 8/5/23 12:07 PM
 */

data class UserCreateRequestDto(
    @Nonnull
    val username: String,
    @Nonnull
    val password: String,
    @Nonnull
    val nick: String = "",
    @Nonnull
    val role: UserRole = UserRole.USER,
    @Nonnull
    val enable: Boolean = true,
)

data class UserDeleteRequestDto(
    @JsonProperty("user_id")
    val userId: UUID,
)