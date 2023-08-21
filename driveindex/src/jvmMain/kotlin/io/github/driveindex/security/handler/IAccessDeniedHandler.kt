package io.github.driveindex.security.handler

import io.github.driveindex.exception.FailedResult
import io.github.driveindex.exception.write
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import java.io.IOException

/**
 * @author sgpublic
 * @Date 2023/2/8 9:56
 */
@Component
class IAccessDeniedHandler : AccessDeniedHandler {
    @Throws(IOException::class)
    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException
    ) {
        response.write(FailedResult.Auth.AccessDenied)
    }
}
