package io.github.driveindex.controller

import com.vaadin.hilla.BrowserCallable
import io.github.driveindex.core.annotation.AllOpen
import io.github.driveindex.dto.resp.Resp
import io.github.driveindex.security.SecurityConfig
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.annotation.security.RolesAllowed

@AllOpen
@BrowserCallable
@Tag(name = "登录接口")
class LoginController {
    /**
     * 利用 SpringSecurity 检查登录是否有效
     * @return 若登录有效直接返回 code 200
     */
    @RolesAllowed(SecurityConfig.ROLE_USER)
    @Operation(
        summary = "token 有效性检查",
        description = "检查 token 是否有效",
    )
    fun checkToken() = Resp {  }
}