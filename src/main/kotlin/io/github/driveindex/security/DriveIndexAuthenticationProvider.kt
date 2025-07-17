package io.github.driveindex.security

import io.github.driveindex.core.exception.FailedResult
import io.github.driveindex.core.util.SHA_256
import io.github.driveindex.database.dao.findByUsername
import io.github.driveindex.database.entity.UserEntity
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.stereotype.Component

@Component
class DriveIndexAuthenticationProvider: AuthenticationProvider {
    class UserException(val result: FailedResult): BadCredentialsException("User not exist or password is invalid")

    @Throws(AuthenticationException::class)
    override fun authenticate(authentication: Authentication): Authentication {
        authentication as UsernamePasswordAuthenticationToken

        val entity = transaction {
            UserEntity.findByUsername(authentication.principal.toString())?.takeIf {
                it[UserEntity.passwordHash] == "${authentication.credentials}${it[UserEntity.passwordSalt]}".SHA_256
            } ?: throw UserException(FailedResult.Auth.WrongPassword)
        }

        if (!entity[UserEntity.enabled]) {
            throw UserException(FailedResult.Auth.UserDisabled)
        }

        return DriveIndexToken(
            authentication.principal.toString(),
            entity[UserEntity.role].authorities
        ).apply {
            details = entity
        }
    }

    override fun supports(authentication: Class<*>): Boolean {
        return UsernamePasswordAuthenticationToken::class.java.isAssignableFrom(authentication)
    }
}