package io.github.driveindex.database.entity.account

import com.fasterxml.jackson.databind.node.ObjectNode
import io.github.driveindex.core.util.Json
import io.github.driveindex.database.entity.AttributeEntity
import io.github.driveindex.database.entity.IdEntity
import io.github.driveindex.database.entity.TimeEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "driveindex_account")
data class AccountEntity(
    @Id
    @Column(name = "id")
    override val id: Int = 0,

    @Column(name = "client_id")
    val parentClientId: Int,

    @Column(name = "display_name")
    var displayName: String,

    @Column(name = "user_principal_name")
    val userPrincipalName: String,

    @Column(name = "expired")
    var accountExpired: Boolean = false,

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