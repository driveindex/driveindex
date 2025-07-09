package io.github.driveindex.module

import io.github.driveindex.Application.Companion.Config
import io.github.driveindex.core.util.CanonicalPath
import io.github.driveindex.core.util.MD5
import io.github.driveindex.core.util.SHA_256
import io.github.driveindex.core.util.log
import io.github.driveindex.database.entity.UserEntity
import io.github.driveindex.database.entity.file.FileEntity
import io.github.driveindex.security.UserPermission
import io.github.driveindex.security.UserRole
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import java.util.UUID
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


@Component
class DBSetupModule {
    @EventListener
    fun onApplicationEvent(event: ContextRefreshedEvent) {
        createInitialUser()
    }

    private fun createInitialUser() = transaction {
        if (UserEntity.selectAll().count() > 0) {
            return@transaction
        }
        val newUsername = Config.app.defaultUsername
        val pwd = Config.app.defaultPassword
        val pwdSalt = UUID.randomUUID().toString().MD5
        log.info("创建默认管理员账户，用户名：$newUsername，密码：$pwd")
        val userId = UserEntity.insert {
            it[username] = newUsername
            it[passwordHash] = "${pwd}${pwdSalt}".SHA_256
            it[passwordSalt] = pwdSalt
            it[role] = UserRole.ADMIN
            it[permission] = emptyList()
        }[UserEntity.id].value
        FileEntity.insert {
            it[name] = CanonicalPath.ROOT_PATH
            it[parentId] = null
            it[isRoot] = true
            it[createBy] = userId
            it[modifyBy] = userId
        }

        commit()
    }
}