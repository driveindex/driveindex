package io.github.driveindex.database.entity

import io.github.driveindex.core.util.JsonKt
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.CurrentTimestamp
import org.jetbrains.exposed.v1.datetime.timestamp
import org.jetbrains.exposed.v1.json.jsonb

interface IdEntity<IdT: Any> {
    val id: IdT
}

interface AttributeEntity<T: Any> {
    val attribute: Column<T>
    companion object {
        inline fun <reified AttT: Any> Table.attribute() = jsonb<AttT>("attribute", JsonKt)
    }
}

interface VersionControlEntity {
    val createAt: Column<Instant>
    fun Table.createAt() = timestamp("create_at").defaultExpression(CurrentTimestamp)

    val createBy: Column<Int>
    fun Table.createBy() = integer("create_by")

    val modifyAt: Column<Instant>
    fun Table.modifyAt() = timestamp("modify_at").clientDefault { Clock.System.now() }

    val modifyBy: Column<Int>
    fun Table.modifyBy() = integer("modify_by")
}

interface EnabledEntity {
    val enabled: Column<Boolean>
    fun Table.enabled() = bool("enabled").default(false)
}
