package io.github.driveindex.database.dao

import io.github.driveindex.database.entity.UserAttribute
import io.github.driveindex.database.entity.UserEntity
import io.github.driveindex.security.UserPermission
import io.github.driveindex.security.UserRole
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.update

fun UserEntity.update(
    uUsername: String,
    uRole: UserRole,
    uPermission: Set<UserPermission>,
    uAttribute: UserAttribute,
) = insert {
    it[username] = uUsername
    it[role] = uRole
    it[permission] = uPermission
    it[attribute] = uAttribute
}

fun UserEntity.findByUsername(uUsername: String) = selectAll()
    .where {
        username.eq(uUsername)
    }
    .singleOrNull()

fun UserEntity.updatePassword(userId: Int, newPassword: String, newSalt: String) =
    update({
        id.eq(userId)
    }) {
        it[passwordHash] = newPassword
        it[passwordSalt] = newSalt
    }
