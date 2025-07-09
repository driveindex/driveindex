package io.github.driveindex.database.dao

import io.github.driveindex.database.entity.VersionControlEntity
import org.jetbrains.exposed.v1.core.Count
import org.jetbrains.exposed.v1.core.Op
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.SqlExpressionBuilder.eq
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.dao.id.IdTable
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.selectAll
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

private fun <IdT: Any, T: IdTable<IdT>> T.whereId(itemId: IdT, itemCreateBy: Int?): Op<Boolean> {
    return if (this is VersionControlEntity && itemCreateBy != null) {
        id.eq(itemId) and createBy.eq(itemCreateBy)
    } else {
        id.eq(itemId)
    }
}

@OptIn(ExperimentalContracts::class)
inline fun <T: Table> T.use(row: ResultRow, block: T.(entity: ResultRow) -> Unit) {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    block(row)
}

fun <T: Any> IdTable<T>.deleteById(itemId: T, itemCreateBy: Int? = null) =
    deleteWhere {
        whereId(itemId, itemCreateBy)
    }

fun <T: Any> IdTable<T>.findById(itemId: T, itemCreateBy: Int? = null) = selectAll()
    .where {
        whereId(itemId, itemCreateBy)
    }
    .singleOrNull()
