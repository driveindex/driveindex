package io.github.driveindex.core.utils

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.jdbc.JdbcTransaction
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

fun <T: Table, RT> transaction(table: T, statement: T.(JdbcTransaction) -> RT): RT {
    return transaction {
        table.statement(this)
    }
}