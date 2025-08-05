package io.github.driveindex.database.dao

import io.github.driveindex.database.entity.SharedLinkEntity
import org.jetbrains.exposed.v1.core.SqlExpressionBuilder.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere

/**
 * @author sgpublic
 * @Date 8/5/23 1:42 PM
 */
fun SharedLinkEntity.deleteByAccount(fAccount: Int) =
    deleteWhere {
        createBy.eq(fAccount)
    }
