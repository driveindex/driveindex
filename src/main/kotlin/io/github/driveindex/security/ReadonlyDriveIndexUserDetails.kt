package io.github.driveindex.security

import io.github.driveindex.database.dao.clone
import io.github.driveindex.database.entity.UserEntity
import org.jetbrains.exposed.v1.core.Expression
import org.jetbrains.exposed.v1.core.ResultRow
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.HashSet

sealed class DriveIndexUserDetails(
    protected val details: ResultRow
): UserDetails {
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

class ReadonlyDriveIndexUserDetails(details: ResultRow): DriveIndexUserDetails(details) {
    constructor(): this(ResultRow.createAndFillDefaults(UserEntity.columns))
    constructor(init: UserEntity.(ResultRow) -> Unit): this() {
        UserEntity.init(details)
    }

    val asMutable: MutableDriveIndexUserDetails get() {
        return MutableDriveIndexUserDetails(details.clone())
    }

    fun asMutable(block: UserEntity.(MutableDriveIndexUserDetails) -> Unit): MutableDriveIndexUserDetails {
        val mutable = asMutable
        UserEntity.block(mutable)
        return mutable
    }
}

class MutableDriveIndexUserDetails internal constructor(details: ResultRow): DriveIndexUserDetails(details) {
    operator fun <T> set(expression: Expression<T>, value: T) {
        details[expression] = value
    }
}


