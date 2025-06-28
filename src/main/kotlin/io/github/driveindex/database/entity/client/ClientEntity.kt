package io.github.driveindex.database.entity.client

import com.fasterxml.jackson.databind.node.ObjectNode
import io.github.driveindex.client.ClientType
import io.github.driveindex.core.util.Json
import io.github.driveindex.database.entity.AttributeEntity
import io.github.driveindex.database.entity.IdEntity
import io.github.driveindex.database.entity.TimeEntity
import jakarta.persistence.*

@Entity
@Table(name = "driveindex_client")
data class ClientEntity(
    @Id
    @Column(name = "id")
    override val id: Int = 0,

    @Column(name = "name")
    var name: String,

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    val type: ClientType,

    @Column(name = "support_delta")
    val supportDelta: Boolean = false,

    @Column(name = "create_at")
    override val createAt: Long = System.currentTimeMillis(),

    @Column(name = "create_by")
    override val createBy: Int,

    @Column(name = "modify_at")
    override val modifyAt: Long = System.currentTimeMillis(),

    @Column(name = "modify_by")
    override val modifyBy: Int,

    @Column(name = "attribute")
    override val attribute: ObjectNode = Json.newObjectNode<Any>(),
): IdEntity, TimeEntity, AttributeEntity