package io.github.driveindex.database.dao

import io.github.driveindex.database.entity.account.AccountEntity
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll

fun AccountEntity.findByName(aClientId: Int, aName: String) = selectAll()
    .where {
        clientId.eq(aClientId) and
                name.eq(aName)
    }
    .singleOrNull()

fun AccountEntity.listByClient(aClientId: Int) = selectAll()
    .where {
        clientId.eq(aClientId)
    }

fun AccountEntity.listIdByClient(aClientId: Int) = select(id)
    .where {
        clientId.eq(aClientId) and
                enabled.eq(true)
    }
