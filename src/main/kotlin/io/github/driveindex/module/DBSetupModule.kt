package io.github.driveindex.module

import io.github.driveindex.Application.Companion.Config
import io.github.driveindex.core.util.MD5
import io.github.driveindex.core.util.SHA_256
import io.github.driveindex.core.util.log
import io.github.driveindex.database.entity.UserEntity
import io.github.driveindex.security.ReadonlyDriveIndexUserDetails
import io.github.driveindex.security.DriveIndexUserDetailsManager
import io.github.driveindex.security.UserRole
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import java.util.*


@Component
class DBSetupModule(
    private val userManager: DriveIndexUserDetailsManager
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
        val pwdSalt = UUID.randomUUID().toString().MD5
        log.info("create default user, username: $newUsername, password: $pwd")
        userManager.createCustomUser(ReadonlyDriveIndexUserDetails {
            it[username] = newUsername
            it[passwordHash] = "${pwd}${pwdSalt}".SHA_256
            it[passwordSalt] = pwdSalt
            it[role] = UserRole.ADMIN
            it[permission] = emptyList()
            it[attribute] = Unit
        })
    }
}