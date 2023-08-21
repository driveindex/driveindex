package io.github.driveindex.security.filter

import io.github.driveindex.Application
import io.github.driveindex.core.ConfigManager
import io.github.driveindex.core.util.log
import io.github.driveindex.core.util.toJwtTag
import io.github.driveindex.database.dao.UserDao
import io.github.driveindex.exception.FailedResult
import io.github.driveindex.exception.write
import io.github.driveindex.security.SecurityConfig
import io.github.driveindex.security.UserPasswordToken
import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtParser
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.GenericFilterBean
import java.security.Key
import java.util.*


/**
 * @author sgpublic
 * @Date 2023/2/8 9:28
 */
@Component
class JwtTokenAuthenticationFilter(
    private val user: UserDao
): GenericFilterBean() {
    private val parser: JwtParser by lazy {
        val secretKey: Key = Keys.hmacShaKeyFor(ConfigManager.getTokenSecurityKey())
        Jwts.parserBuilder().setSigningKey(secretKey).build()
    }

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        request as HttpServletRequest
        response as HttpServletResponse

        request.requestURI.let {
            if (it.startsWith("/api/login") ||
                !(it.startsWith("/api/admin") ||
                        it.startsWith("/api/user") ||
                        it.startsWith("/api/check_token"))) {
                chain.doFilter(request, response)
                return
            }
        }
        val token = (request.getHeader(SecurityConfig.Header) ?: "").takeIf {
            it.isNotBlank() && it.startsWith("Bearer ")
        }?.substring(7)
        if (token == null) {
            chain.doFilter(request, response)
            return
        }
        val claims: Claims
        try {
            claims = parser.parseClaimsJws(token).body
        } catch (e: FailedResult) {
            response.write(e)
            return
        } catch (e: Exception) {
            log.debug("jwt 认证错误", e)
            response.write(FailedResult.Auth.ExpiredToken)
            return
        }
        if (claims.expiration.before(Date())) {
            log.debug("token 过期")
        } else if (claims.issuer != Application.APPLICATION_BASE_NAME) {
            log.debug("未知的 token 签发者")
        } else {
            onValidToken(claims)?.let {
                SecurityContextHolder.getContext().authentication = it
                chain.doFilter(request, response)
                return
            }
        }
    }

    private fun onValidToken(claims: Claims): UserPasswordToken? {
        val username: String = (claims[SecurityConfig.JWT_USERNAME] as String?)
            ?: throw IllegalArgumentException("no username found in jwt token")
        val entity = user.getUserByUsername(username)
            ?: throw IllegalStateException("user not found: $username")

        if (!entity.enable) {
            throw FailedResult.Auth.UserDisabled
        }

        val tag: String = (claims[SecurityConfig.JWT_TAG] as String?)
            ?: throw IllegalArgumentException("no tag found in jwt token")
        if (entity.password.toJwtTag(claims.issuedAt.time) != tag) {
            log.debug("密码被修改，强制 token 失效")
            return null
        }
        return UserPasswordToken(
            entity.username, entity.password,
            entity.role.getGrantedAuthority()
        ).also {
            it.details = entity
        }
    }
}