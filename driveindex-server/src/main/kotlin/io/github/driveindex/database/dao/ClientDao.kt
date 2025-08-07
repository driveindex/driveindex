package io.github.driveindex.database.dao

import io.github.driveindex.database.entity.ClientEntity
import io.github.driveindex.drivers.api.ClientType
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll

fun ClientEntity.getClient(cId: Int, cType: ClientType) = selectAll()
    .where {
        id.eq(cId) and type.eq(cType)
    }
    .singleOrNull()

fun ClientEntity.findByName(cName: String, cUser: Int) = selectAll()
    .where {
        createBy.eq(cUser) and name.eq(cName)
    }
    .singleOrNull()

fun ClientEntity.listIdByUser(cUser: Int) = select(id)
    .where {
        createBy.eq(cUser)
    }

fun ClientEntity.listByUser(cUser: Int) = selectAll()
    .where {
        createBy.eq(cUser)
    }
