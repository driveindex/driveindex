package io.github.driveindex.dto.resp

import io.github.driveindex.security.UserRole
import io.swagger.v3.oas.annotations.media.Schema
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * @author sgpublic
 * @Date 2023/2/8 10:12
 */
data class LoginRespDto(
    @field:Schema(description = "用户名")
    @param:JsonProperty("username")
    val username: String,

    @field:Schema(description = "用户昵称")
    @param:JsonProperty("nick")
    val nick: String,

    @field:Schema(description = "认证相关信息")
    @param:JsonProperty("auth")
    val auth: Auth,
): RespResultData {
    data class Auth (
        @field:Schema(example = "a5c2bca1aaz3...")
        @param:JsonProperty("token")
        val token: String,

        @field:Schema(description = "用户角色", example = "ADMIN")
        @param:JsonProperty("role")
        val role: UserRole,
    )
}