package io.github.driveindex.security

import io.github.driveindex.core.exception.FailedResult
import io.github.driveindex.module.UserModel
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.stereotype.Component

@Component
class DriveIndexAuthenticationProvider(
    private val userModel: UserModel,
): AuthenticationProvider {
    class UserException(val result: FailedResult): BadCredentialsException(result.message)

    @Throws(AuthenticationException::class)
    override fun authenticate(authentication: Authentication): Authentication {
        authentication as UsernamePasswordAuthenticationToken
        try {
            return userModel.login(
                authentication.principal.toString(),
                authentication.credentials.toString()
            )
        } catch (e: FailedResult) {
            throw UserException(e)
        }
    }

    override fun supports(authentication: Class<*>): Boolean {
        return UsernamePasswordAuthenticationToken::class.java.isAssignableFrom(authentication)
    }
}