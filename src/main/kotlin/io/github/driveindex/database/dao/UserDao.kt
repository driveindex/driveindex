package io.github.driveindex.database.dao

import io.github.driveindex.database.entity.UserEntity
import org.jetbrains.exposed.v1.jdbc.selectAll

fun UserEntity.findByUsername(uUsername: String) = selectAll()
    .where {
        username.eq(uUsername)
    }
    .singleOrNull()
