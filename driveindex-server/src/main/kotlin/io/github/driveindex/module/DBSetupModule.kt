package io.github.driveindex.module

import io.github.driveindex.Application.Companion.Config
import io.github.driveindex.utils.SHA_256
import io.github.driveindex.core.util.log
import io.github.driveindex.database.entity.UserEntity
import io.github.driveindex.dto.req.admin.UserCreateRequestDto
import io.github.driveindex.security.UserPermission
import io.github.driveindex.security.UserRole
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component


@Component
class DBSetupModule(
    private val admin: AdminModel,
) {
    @EventListener(ContextRefreshedEvent::class)
    fun onApplicationEvent() {
        createInitialUser()
    }

    private fun createInitialUser() {
        val userCount = transaction {
            UserEntity.selectAll().count()
        }
        if (userCount > 0) {
            return
        }
        val newUsername = Config.app.defaultUsername
        val pwd = Config.app.defaultPassword
        log.info("create default user, username: $newUsername, password: $pwd")
        admin.createUser(UserCreateRequestDto(
            username = newUsername,
            password = pwd.SHA_256,
            nickname = null,
            role = UserRole.ADMIN,
            permission = UserPermission.GROUP_ADMIN,
            enabled = true,
        ))
    }
}