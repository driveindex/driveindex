package io.github.driveindex.security.handler

import io.github.driveindex.core.util.log
import io.github.driveindex.exception.FailedResult
import io.github.driveindex.exception.write
import io.github.driveindex.security.IAuthenticationProvider
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.stereotype.Component

/**
 * @author sgpublic
 * @Date 2023/2/7 16:05
 */
@Component
class IAuthenticationFailureHandler: AuthenticationFailureHandler {
    override fun onAuthenticationFailure(
        request: HttpServletRequest,
        response: HttpServletResponse,
        exception: AuthenticationException
    ) {
        log.debug("登录失败", exception)
        when (exception) {
            is IAuthenticationProvider.UserException -> FailedResult.Auth.WrongPassword
            is AuthenticationServiceException -> FailedResult.UnsupportedRequest
            else -> null
        }?.also {
            response.status = HttpStatus.UNAUTHORIZED.value()
            response.write(it)
            return
        }.let {
            response.status = HttpStatus.INTERNAL_SERVER_ERROR.value()
            response.write(FailedResult.InternalServerError)
        }
    }
}