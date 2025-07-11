package io.github.driveindex.security

import com.github.benmanes.caffeine.cache.Caffeine
import io.github.driveindex.database.dao.clone
import io.github.driveindex.database.dao.findById
import io.github.driveindex.database.entity.UserEntity
import kotlinx.coroutines.future.await
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.v1.core.Expression
import org.jetbrains.exposed.v1.core.ResultRow
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.Duration
import java.util.HashSet

abstract class DriveIndexUserDetails: UserDetails {
    protected abstract val details: ResultRow

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return details[UserEntity.role].authorities
    }

    override fun getPassword(): String {
        return details[UserEntity.passwordHash]
    }

    override fun getUsername(): String {
        return details[UserEntity.username]
    }

    val permissions: Set<UserPermission> by lazy {
        HashSet(details[UserEntity.permission])
    }

    operator fun <T> get(expression: Expression<T>): T {
        return details[expression]
    }
}

class ReadonlyDriveIndexUserDetails(
    private val initRow: ResultRow
): DriveIndexUserDetails() {
    private val userId: Int = initRow[UserEntity.id].value

    init {
        cache.put(userId, initRow)
    }

    override val details: ResultRow
        get() = cache.get(initRow[UserEntity.id].value)!!

    val asMutable: MutableDriveIndexUserDetails get() {
        runBlocking {
            cache.refresh(userId).await()
        }
        return MutableDriveIndexUserDetails(details.clone())
    }

    fun asMutable(block: UserEntity.(MutableDriveIndexUserDetails) -> Unit): MutableDriveIndexUserDetails {
        val mutable = asMutable
        UserEntity.block(mutable)
        return mutable
    }

    companion object {
        private val cache = Caffeine.newBuilder()
            .refreshAfterWrite(Duration.ofMinutes(1))
            .expireAfterWrite(Duration.ofMinutes(1))
            .build<Int, ResultRow?> { key ->
                UserEntity.findById(key)
            }
    }
}

class MutableDriveIndexUserDetails(
    override val details: ResultRow
): DriveIndexUserDetails() {
    constructor(
        block: UserEntity.(ResultRow) -> Unit
    ): this(
        ResultRow.createAndFillDefaults(UserEntity.columns)
    ) {
        UserEntity.block(details)
    }

    operator fun <T> set(expression: Expression<T>, value: T) {
        details[expression] = value
    }
}
