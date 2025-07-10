package io.github.driveindex.database.entity

import io.github.driveindex.Application
import io.github.driveindex.database.entity.AttributeEntity.Companion.attribute
import io.github.driveindex.security.UserPermission
import io.github.driveindex.security.UserRole
import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.EnumerationNameColumnType
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable

object UserEntity: IntIdTable("${Application.BASE_NAME_LOWER}_user"),
    EnabledEntity, AttributeEntity<Unit> {
    val username = varchar("username", length = 64)
    val passwordHash = text("pwd_hash")
    val passwordSalt = text("pwd_salt")
    val role = enumerationByName("role", 16, UserRole::class)
    override val enabled = enabled()
    val permission = array("permission", EnumerationNameColumnType(UserPermission::class, 32))
    override val attribute: Column<Unit> = attribute()
}