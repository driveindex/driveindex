package io.github.driveindex.dto.req.auth

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

/**
 * @author sgpublic
 * @Date 2023/2/7 15:33
 */
data class LoginReqDto(
    @field:Schema(description = "用户密码", required = true)
    @param:JsonProperty("username")
    val username: String,

    @field:Schema(description = "用户员密码", required = true)
    @param:JsonProperty("password")
    val password: String,
)