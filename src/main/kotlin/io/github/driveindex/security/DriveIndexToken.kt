package io.github.driveindex.security

import com.github.benmanes.caffeine.cache.Caffeine
import io.github.driveindex.core.exception.FailedResult
import io.github.driveindex.database.dao.findById
import io.github.driveindex.database.entity.UserEntity
import org.jetbrains.exposed.v1.core.Expression
import org.jetbrains.exposed.v1.core.ResultRow
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import java.time.Duration

class DriveIndexToken(
    username: String, auths: Collection<GrantedAuthority>? = null
): UsernamePasswordAuthenticationToken(
    username, null, auths?.takeIf { it.isNotEmpty() }
) {
    private val userId: Int by lazy {
        val userId = (super.details as ResultRow)[UserEntity.id].value
        cache.put(userId, super.details as ResultRow)
        return@lazy userId
    }

    override fun getDetails(): ResultRow {
        return cache.get(userId)
            ?: throw FailedResult.Auth.IllegalRequest
    }

    override fun getPrincipal(): String {
        return this[UserEntity.username]
    }

    override fun getCredentials(): String? {
        return null
    }

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return this[UserEntity.role].authorities
    }

    fun hasPermission(permission: UserPermission): Boolean {
        return this[UserEntity.permission].contains(permission)
    }

    operator fun <T: Any> get(expression: Expression<T>): T {
        return details[expression]
    }

    companion object {
        private val cache = Caffeine.newBuilder()
            .refreshAfterWrite(Duration.ofMinutes(1))
            .expireAfterWrite(Duration.ofMinutes(1))
            .build { it: Int ->
                UserEntity.findById(it)
            }
    }
}
