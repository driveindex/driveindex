package io.github.driveindex.controller

import io.github.driveindex.dto.req.auth.LoginReqDto
import io.github.driveindex.dto.resp.RespResult
import io.github.driveindex.dto.resp.SampleResult
import io.github.driveindex.dto.resp.admin.LoginRespDto
import io.github.driveindex.security.SecurityConfig
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "登录接口")
class LoginController {
    /**
     * 利用 SpringSecurity 检查登录是否有效
     * @return 若登录有效直接返回 code 200
     */
    @Operation(
        summary = "token 有效性检查",
        description = "检查 token 是否有效",
        security = [SecurityRequirement(name = SecurityConfig.Header)]
    )
    @GetMapping("/api/token_state")
    fun checkToken(): SampleResult {
        return SampleResult
    }

    @Operation(summary = "用户登陆", description = "使用密码登陆")
    @PostMapping("/api/login")
    fun login(
        @RequestBody dto: LoginReqDto
    ): RespResult<LoginRespDto> {
        throw BadCredentialsException("use authentication provider instead.")
    }
}