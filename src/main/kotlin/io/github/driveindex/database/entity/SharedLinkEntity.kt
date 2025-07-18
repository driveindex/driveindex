package io.github.driveindex.database.entity

import io.github.driveindex.Application
import io.github.driveindex.database.entity.AttributeEntity.Companion.attribute
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.dao.id.UUIDTable

/**
 * @author sgpublic
 * @Date 8/5/23 1:42 PM
 */
object SharedLinkEntity: UUIDTable("${Application.BASE_NAME_LOWER}_shared_link"),
    EnabledEntity, VersionControlEntity, AttributeEntity<SharedLinkEntity.Attribute> {
    val target = uuid(name = "target")
    val needPassword = bool(name = "need_pwd").default(false)
    val expireAt = long(name = "expired_at").nullable()
    override val enabled = enabled()
    override val createAt = createAt()
    override val createBy = createBy()
    override val modifyAt = modifyAt()
    override val modifyBy = modifyBy()
    override val attribute: Column<Attribute> = attribute()

    @Serializable
    data class Attribute(
        @SerialName("password")
        val password: String = "",
    )
}
