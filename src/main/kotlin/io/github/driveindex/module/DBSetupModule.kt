package io.github.driveindex.module

import io.github.driveindex.Application.Companion.Config
import io.github.driveindex.core.util.CanonicalPath
import io.github.driveindex.core.util.MD5
import io.github.driveindex.core.util.SHA_256
import io.github.driveindex.core.util.log
import io.github.driveindex.database.dao.autoTransaction
import io.github.driveindex.database.entity.UserEntity
import io.github.driveindex.database.entity.file.FileEntity
import io.github.driveindex.database.entity.file.attributes.LocalDirAttribute
import io.github.driveindex.security.UserRole
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import java.util.*


@Component
class DBSetupModule {
    @EventListener
    fun onApplicationEvent(event: ContextRefreshedEvent) {
        createInitialUser()
    }

    private fun createInitialUser() = autoTransaction {
        if (UserEntity.selectAll().count() > 0) {
            return@autoTransaction
        }
        val newUsername = Config.app.defaultUsername
        val pwd = Config.app.defaultPassword
        val pwdSalt = UUID.randomUUID().toString().MD5
        log.info("create default user, username: $newUsername, password: $pwd")
        val userId = UserEntity.insert {
            it[username] = newUsername
            it[passwordHash] = "${pwd}${pwdSalt}".SHA_256
            it[passwordSalt] = pwdSalt
            it[role] = UserRole.ADMIN
            it[permission] = emptyList()
            it[attribute] = Unit
        }[UserEntity.id].value
        FileEntity.insert {
            it[name] = CanonicalPath.ROOT_PATH
            it[parentId] = null
            it[isRoot] = true
            it.createBy(userId)
            it[attribute] = LocalDirAttribute()
        }
    }
}