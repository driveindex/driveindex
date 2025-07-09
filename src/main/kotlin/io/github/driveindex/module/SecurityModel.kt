package io.github.driveindex.module

import io.github.driveindex.database.entity.UserEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.lang.IllegalArgumentException

@Component
class Current {
    val User: UserEntity
        get() = SecurityContextHolder.getContext().authentication.details as UserEntity
}

@Component
class MutableCurrent(): Current() {
    override var User: UserEntity
        get() = super.User
        set(value) {
            if (User.id != value.id) {
                throw IllegalArgumentException("不允许修改 ID")
            }
//            user.save(value)
        }
}