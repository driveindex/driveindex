package io.github.driveindex.dto.resp.admin

import io.github.driveindex.security.UserRole
import io.swagger.v3.oas.annotations.media.Schema
import kotlinx.serialization.Serializable

/**
 * @author sgpublic
 * @Date 2023/2/8 10:12
 */
@Serializable
data class LoginRespDto(
    @field:Schema(description = "用户名")
    val username: String,

    @field:Schema(description = "用户昵称")
    val nick: String,

    @field:Schema(description = "认证相关信息")
    val auth: Auth,
) {
    @Serializable
    data class Auth (
        @field:Schema(example = "a5c2bca1aaz3...")
        val token: String,

        @field:Schema(description = "用户角色", example = "ADMIN")
        val role: UserRole,
    )
}