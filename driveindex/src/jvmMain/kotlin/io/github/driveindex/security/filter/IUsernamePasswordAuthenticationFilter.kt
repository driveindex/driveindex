package io.github.driveindex.security.filter

import io.github.driveindex.dto.req.auth.LoginReqDto
import io.github.driveindex.security.IAuthenticationProvider
import io.github.driveindex.security.UserPasswordToken
import io.github.driveindex.security.handler.IAuthenticationFailureHandler
import io.github.driveindex.security.handler.IAuthenticationSuccessHandler
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import io.github.driveindex.core.util.JsonGlobal
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.util.matcher.RequestMatcher
import org.springframework.stereotype.Component
import java.io.IOException
import java.nio.charset.StandardCharsets

/**
 * @author sgpublic
 * @Date 2023/2/7 15:30
 */
@Component
class IUsernamePasswordAuthenticationFilter(
    onSuccess: IAuthenticationSuccessHandler,
    onFailed: IAuthenticationFailureHandler,
    provider: IAuthenticationProvider
): AbstractAuthenticationProcessingFilter(
    object : RequestMatcher {
        private val url = "/api/login"
        private val method = HttpMethod.POST.name()
        override fun matches(request: HttpServletRequest): Boolean {
            return method == request.method && url.contentEquals(request.requestURI)
        }

        override fun matcher(request: HttpServletRequest): RequestMatcher.MatchResult? {
            return if (matches(request)) RequestMatcher.MatchResult.match() else RequestMatcher.MatchResult.notMatch()
        }
    },
    ProviderManager(provider)
) {
    init {
        super.setAuthenticationSuccessHandler(onSuccess)
        super.setAuthenticationFailureHandler(onFailed)
    }

    @Throws(AuthenticationException::class, IOException::class)
    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse?): Authentication? {
        if ("POST" != request.method) {
            throw AuthenticationServiceException("Authentication method not supported: " + request.method)
        }
        val content = String(request.inputStream.readAllBytes(), StandardCharsets.UTF_8)
        val dto: LoginReqDto = JsonGlobal.decodeFromString(content)
        val token = UserPasswordToken(dto.username, dto.password)
        return authenticationManager.authenticate(token)
    }
}