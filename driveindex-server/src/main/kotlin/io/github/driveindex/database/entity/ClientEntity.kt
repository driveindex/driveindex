package io.github.driveindex.database.entity

import io.github.driveindex.Application
import io.github.driveindex.database.entity.AttributeEntity.Companion.attribute
import io.github.driveindex.drivers.api.ClientType
import io.github.driveindex.drivers.attributes.ClientAttribute
import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
object ClientEntity: IntIdTable("${Application.BASE_NAME_LOWER}_client"),
    EnabledEntity, VersionControlEntity, AttributeEntity<ClientAttribute> {
    val name = varchar("name", length = 64)
    val type = enumerationByName<ClientType>("type", length = 32)
    override val enabled = enabled()
    override val createAt = createAt()
    override val createBy = createBy()
    override val modifyAt = modifyAt()
    override val modifyBy = modifyBy()
    override val attribute: Column<ClientAttribute> = attribute()
}