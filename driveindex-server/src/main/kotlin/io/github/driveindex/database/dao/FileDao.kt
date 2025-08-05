package io.github.driveindex.database.dao

import io.github.driveindex.database.dao.pageable.Pageable
import io.github.driveindex.database.dao.pageable.paged
import io.github.driveindex.database.entity.file.FileEntity
import org.jetbrains.exposed.v1.core.SqlExpressionBuilder.eq
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.update
import org.jetbrains.exposed.v1.json.extract
import java.util.*

/**
 * @author sgpublic
 * @Date 2023/3/29 下午12:54
 */

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

fun FileEntity.findRootByUserId(userId: Int) = selectAll()
    .where {
        isRoot.eq(true) and createBy.eq(userId)
    }
    .single()

fun FileEntity.listByParent(
    dirId: UUID, fileCreateBy: Int,
) = selectAll()
    .where {
        parentId.eq(dirId) and createBy.eq(fileCreateBy)
    }
