package io.github.driveindex.security

import io.github.driveindex.database.dao.*
import io.github.driveindex.database.entity.UserEntity
import io.github.driveindex.database.entity.file.FileEntity
import io.github.driveindex.module.Current
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component

@Component
class DriveIndexUserDetailsManager(
    private val current: Current,
): AbstractUserDetailsManager<DriveIndexUserDetails> {
    override fun createCustomUser(user: DriveIndexUserDetails) {
        transaction {
            val userId = UserEntity.new(
                uUsername = user[UserEntity.username],
                uPasswordHash = user[UserEntity.passwordHash],
                uPasswordSalt = user[UserEntity.passwordSalt],
                uRole = user[UserEntity.role],
                uPermission = user[UserEntity.permission],
                uAttribute = user[UserEntity.attribute],
            )[UserEntity.id].value
            FileEntity.insertRootForUser(userId)
        }
    }

    override fun updateCustomUser(user: DriveIndexUserDetails) {
        transaction {
            UserEntity.update(
                uUsername = user[UserEntity.username],
                uRole = user[UserEntity.role],
                uPermission = user[UserEntity.permission],
                uAttribute = user[UserEntity.attribute],
            )
        }
    }

    override fun deleteUser(username: String) {
        transaction {
            val user = loadUserByUsername(username)
            UserEntity.deleteById(user[UserEntity.id].value)
        }
    }

    override fun changePassword(oldPassword: String, newPassword: String) {
        if (oldPassword != current.User.password) {
            throw BadCredentialsException("password mismatched")
        }
        transaction {
            UserEntity.updatePassword(current.User[UserEntity.id].value, newPassword)
        }
    }

    override fun userExists(username: String): Boolean {
        return UserEntity.findByUsername(username) != null
    }

    override fun loadUserByUsername(username: String) = transaction {
        val user = UserEntity.findByUsername(username)
            ?: throw UsernameNotFoundException("User $username not found")
        return@transaction ReadonlyDriveIndexUserDetails(user)
    }
}