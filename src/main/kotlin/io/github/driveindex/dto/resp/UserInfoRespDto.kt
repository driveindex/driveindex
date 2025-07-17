package io.github.driveindex.dto.resp

import io.github.driveindex.security.UserRole
import io.swagger.v3.oas.annotations.media.Schema
import io.github.driveindex.security.UserPermission

/**
 * @author sgpublic
 * @Date 2023/2/8 10:12
 */
data class UserInfoRespDto(
    @field:Schema(description = "用户名")
    val username: String,

    @field:Schema(description = "用户昵称")
    val nickname: String?,

    @field:Schema(description = "用户角色", example = "ADMIN")
    val role: UserRole,

    @field:Schema(description = "用户权限")
    val permission: Set<UserPermission>,
)