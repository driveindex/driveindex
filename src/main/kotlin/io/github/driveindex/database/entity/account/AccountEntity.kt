package io.github.driveindex.database.entity.account

import io.github.driveindex.Application
import io.github.driveindex.database.entity.AttributeEntity
import io.github.driveindex.database.entity.AttributeEntity.Companion.attribute
import io.github.driveindex.database.entity.EnabledEntity
import io.github.driveindex.database.entity.VersionControlEntity
import io.github.driveindex.database.entity.account.attributes.AccountAttribute
import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable

object AccountEntity: IntIdTable("${Application.BASE_NAME_LOWER}_account"),
    EnabledEntity, VersionControlEntity, AttributeEntity<AccountAttribute> {
    val name = varchar("name", length = 64)
    val clientId = integer(name = "client_id")
    val expired = bool("expired").default(false)
    override val enabled = enabled()
    override val createAt = createAt()
    override val createBy = createBy()
    override val modifyAt = modifyAt()
    override val modifyBy = modifyBy()
    override val attribute: Column<AccountAttribute> = attribute()
}
