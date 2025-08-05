package io.github.driveindex.database.entity.client

import io.github.driveindex.Application
import io.github.driveindex.core.client.ClientType
import io.github.driveindex.database.entity.AttributeEntity
import io.github.driveindex.database.entity.AttributeEntity.Companion.attribute
import io.github.driveindex.database.entity.EnabledEntity
import io.github.driveindex.database.entity.VersionControlEntity
import io.github.driveindex.drives.attributes.ClientAttribute
import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable

object ClientEntity: IntIdTable("${Application.BASE_NAME_LOWER}_client"),
    EnabledEntity, VersionControlEntity, AttributeEntity<ClientAttribute> {
    val name = varchar("name", length = 64)
    val type = enumerationByName<ClientType>("type", length = 16)
    override val enabled = enabled()
    override val createAt = createAt()
    override val createBy = createBy()
    override val modifyAt = modifyAt()
    override val modifyBy = modifyBy()
    override val attribute: Column<ClientAttribute> = attribute()
}