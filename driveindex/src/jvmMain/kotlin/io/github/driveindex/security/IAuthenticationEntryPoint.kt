package io.github.driveindex.security

import io.github.driveindex.core.util.log
import io.github.driveindex.exception.FailedResult
import io.github.driveindex.exception.write
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

/**
 * @author sgpublic
 * @Date 2023/2/8 9:58
 */
@Component
class IAuthenticationEntryPoint : AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        log.debug("登录失败", authException)
        if (authException is IAuthenticationProvider.UserException) {
            response.write(authException.result)
        } else {
            response.write(FailedResult.AnonymousDenied)
        }
    }
}