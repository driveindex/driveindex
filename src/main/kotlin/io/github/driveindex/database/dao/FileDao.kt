package io.github.driveindex.database.dao

import io.github.driveindex.core.util.CanonicalPath
import io.github.driveindex.database.entity.file.FileEntity
import io.github.driveindex.database.entity.file.attributes.LocalDirAttribute
import org.jetbrains.exposed.v1.core.SqlExpressionBuilder.eq
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.update
import org.jetbrains.exposed.v1.json.extract
import java.util.*

/**
 * @author sgpublic
 * @Date 2023/3/29 下午12:54
 */

fun FileEntity.insertRootForUser(userId: Int) =
    insert {
        it[name] = CanonicalPath.ROOT_PATH
        it[parentId] = null
        it[isRoot] = true
        it.createBy(userId)
        it[attribute] = LocalDirAttribute()
    }

fun FileEntity.rename(fId: UUID, fName: String) =
    update({ id.eq(fId) }) {
        it[name] = fName
    }

fun FileEntity.findByNameAndParentId(fName: String, fParentId: UUID, fCreateBy: Int) = selectAll()
    .where {
        name.eq(fName) and parentId.eq(fParentId) and createBy.eq(fCreateBy)
    }
    .singleOrNull()

fun FileEntity.deleteByAccount(fAccount: Int) =
    deleteWhere {
        isRemote.eq(true) and attribute.extract()
    }
