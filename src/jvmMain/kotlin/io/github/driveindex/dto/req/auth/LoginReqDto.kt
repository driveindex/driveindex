package io.github.driveindex.dto.req.auth

import io.swagger.v3.oas.annotations.media.Schema
import kotlinx.serialization.Serializable

/**
 * @author sgpublic
 * @Date 2023/2/7 15:33
 */
@Serializable
data class LoginReqDto(
    @field:Schema(description = "用户密码", required = true)
    val username: String,

    @field:Schema(description = "用户员密码", required = true)
    val password: String,
)