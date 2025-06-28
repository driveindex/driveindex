package io.github.driveindex.database.entity

import com.fasterxml.jackson.databind.node.ObjectNode
import io.github.driveindex.core.util.Json
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.util.*

/**
 * @author sgpublic
 * @Date 8/5/23 1:42 PM
 */
@Entity
@Table(name = "driveindex_shared_link")
data class SharedLinkEntity(
    @Id
    @Column(name = "id")
    override val id: Int = 0,

    @Column(name = "root_target")
    val rootTarget: Int,

    @Column(name = "password")
    val password: String,

    @Column(name = "parent_account")
    val parentAccount: Int,

    @Column(name = "expired_time")
    val expireTime: Long = -1,

    @Column(name = "attribute")
    override val attribute: ObjectNode = Json.newObjectNode<Any>(),
): IdEntity, AttributeEntity
