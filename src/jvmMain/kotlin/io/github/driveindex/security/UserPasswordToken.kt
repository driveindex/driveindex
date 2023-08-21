package io.github.driveindex.security

import io.github.driveindex.Application
import io.github.driveindex.core.ConfigManager
import io.github.driveindex.core.util.asDate
import io.github.driveindex.core.util.toJwtTag
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import java.security.Key
import java.util.*

class UserPasswordToken(
    username: String, password: String, auths: Collection<GrantedAuthority>? = null
): UsernamePasswordAuthenticationToken(
    username, password, auths?.takeIf { it.isNotEmpty() }
) {
    override fun getPrincipal(): String {
        return super.getPrincipal() as String
    }

    override fun getCredentials(): String? {
        return super.getCredentials() as String?
    }

    fun newAuthed(role: UserRole): UserPasswordToken {
        if (credentials == null) {
            throw IllegalStateException("token has already authed")
        }
        return UserPasswordToken(
            principal, credentials!!, role.getGrantedAuthority()
        ).also {
            it.details = (System.currentTimeMillis() / 1000 * 1000).let { now ->
                return@let Details(now, (it.credentials as String).toJwtTag(now))
            }
        }
    }

    fun createJwtToken(): String {
        if (details == null) {
            throw IllegalStateException("token has not been authed yet")
        }
        val details = this.details as Details
        Jwts.claims().let { claims ->
            claims.issuer = Application.APPLICATION_BASE_NAME
            claims[SecurityConfig.JWT_USERNAME] = principal
            claims[SecurityConfig.JWT_TAG] = details.tag
            return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(details.now.asDate)
                .setExpiration(Date(details.now + ConfigManager.TokenExpired * 1000))
                .signWith(secretKey)
                .compact()
        }
    }

    data class Details(
        val now: Long,
        val tag: String
    )

    companion object {
        @JvmStatic
        private val secretKey: Key = Keys.hmacShaKeyFor(ConfigManager.getTokenSecurityKey())
    }
}