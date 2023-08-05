package io.github.driveindex.database.entity

import io.github.driveindex.client.ClientType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.util.*

@Entity
@Table(name = "clients")
data class ClientsEntity(
    @Id
    @Column(name = "client_id")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    val id: UUID = UUID.randomUUID(),

    @Column(name = "name")
    var name: String,

    @Column(name = "type")
    val type: ClientType,

    @Column(name = "support_delta")
    val supportDelta: Boolean = false,

    @Column(name = "create_at")
    val createAt: Long = System.currentTimeMillis(),

    @Column(name = "create_by")
    val createBy: UUID,

    @Column(name = "modify_at")
    var modifyAt: Long = System.currentTimeMillis(),
)