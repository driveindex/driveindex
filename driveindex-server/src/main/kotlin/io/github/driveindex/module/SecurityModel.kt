package io.github.driveindex.module

import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.LoadingCache
import io.github.driveindex.database.dao.findById
import io.github.driveindex.database.entity.UserEntity
import io.github.driveindex.security.UserPermission
import org.jetbrains.exposed.v1.core.Expression
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Component
import java.time.Duration
import kotlin.text.toIntOrNull

@Component
class Current {
    val user: DriveIndexToken? get() {
        val auth = SecurityContextHolder.getContext()
            .authentication as JwtAuthenticationToken
        val userId = auth.name
        return cache.get(userId)
    }

    val authedUser: DriveIndexToken get() = user!!

    val userId: Int get() = authedUser[UserEntity.id].value

    private val cache: LoadingCache<String, DriveIndexToken?> = Caffeine.newBuilder()
        .refreshAfterWrite(Duration.ofMinutes(1))
        .expireAfterWrite(Duration.ofMinutes(1))
        .build { rawId: String ->
            rawId.toIntOrNull()?.let {
                transaction {
                    UserEntity.findById(it)
                }
            }?.let {
                DriveIndexToken(it)
            }
        }

    class DriveIndexToken(
        private val result: ResultRow
    ) {
        operator fun <T: Any> get(expression: Expression<T>): T {
            return result[expression]
        }

        fun hasPermission(permission: UserPermission): Boolean {
            return this[UserEntity.permission].contains(permission)
        }
    }
}
