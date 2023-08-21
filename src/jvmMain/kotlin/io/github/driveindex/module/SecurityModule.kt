package io.github.driveindex.module

import io.github.driveindex.database.dao.UserDao
import io.github.driveindex.database.entity.UserEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.lang.IllegalArgumentException

@Component
class Current(
    private val user: UserDao,
) {
    var User: UserEntity
        get() {
            return SecurityContextHolder.getContext().authentication.details as UserEntity
        }
        set(value) {
            if (User.id != value.id) {
                throw IllegalArgumentException("不允许修改 ID")
            }
            user.save(value)
        }
}