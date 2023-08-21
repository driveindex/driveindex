package io.github.driveindex.security

import io.github.driveindex.exception.FailedResult
import io.github.driveindex.database.dao.UserDao
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.stereotype.Component
import java.util.*

/**
 * @author sgpublic
 * @Date 2023/2/7 15:55
 */
@Component
class IAuthenticationProvider(
    private val user: UserDao
): AuthenticationProvider {
    class UserException(val result: FailedResult): BadCredentialsException("用户不存在或密码错误")

    @Throws(AuthenticationException::class)
    override fun authenticate(authentication: Authentication): Authentication? {
        authentication as UserPasswordToken

        val entity = user.getUserByUsername(authentication.principal)?.takeIf {
                Objects.equals(it.password, authentication.credentials)
        } ?: throw UserException(FailedResult.Auth.WrongPassword)

        if (!entity.enable) {
            throw UserException(FailedResult.Auth.UserDisabled)
        }

        return authentication.newAuthed(entity.role)
    }

    override fun supports(authentication: Class<*>): Boolean {
        return authentication == UserPasswordToken::class.java
    }
}