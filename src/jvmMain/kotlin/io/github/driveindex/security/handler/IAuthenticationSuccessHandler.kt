package io.github.driveindex.security.handler

import io.github.driveindex.dto.resp.admin.LoginRespDto
import io.github.driveindex.dto.resp.write
import io.github.driveindex.database.dao.UserDao
import io.github.driveindex.security.UserPasswordToken
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component

/**
 * @author sgpublic
 * @Date 2023/2/7 15:41
 */
@Component
class IAuthenticationSuccessHandler(
    private val user: UserDao
): AuthenticationSuccessHandler {
    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        authentication as UserPasswordToken

        val username = authentication.principal
        val entity = user.getUserByUsername(username)
            ?: throw IllegalStateException("user not found: $username")

        response.write(LoginRespDto.serializer(), LoginRespDto(
            username = entity.username,
            nick = entity.nick,
            auth = LoginRespDto.Auth(
                token = authentication.createJwtToken(),
                role = entity.role,
            )
        ))
    }
}