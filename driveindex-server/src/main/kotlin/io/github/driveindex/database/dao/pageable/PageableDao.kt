package io.github.driveindex.database.dao.pageable

import org.jetbrains.exposed.v1.core.Expression
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.jdbc.Query

data class Pageable(
    val size: Int,
    val index: Int,
    val sort: LinkedHashMap<Expression<*>, SortOrder>,
)

data class Page(
    val totalSize: Long,
    val result: List<ResultRow>,
)

fun Query.paged(pageable: Pageable?): Page {
    val totalCount = count()
    val query = apply {
        pageable?.let { pageable ->
            limit(pageable.size)
                .offset(pageable.size.toLong() * pageable.index)
                .apply {
                    for ((col, order) in pageable.sort) {
                        orderBy(col, order)
                    }
                }
        }
    }.map { it }
    return Page(
        totalSize = totalCount,
        result = query
    )
}