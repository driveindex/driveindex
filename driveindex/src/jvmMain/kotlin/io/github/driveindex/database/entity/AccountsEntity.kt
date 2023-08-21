package io.github.driveindex.database.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.util.*

@Entity
@Table(name = "accounts")
data class AccountsEntity(
    @Id
    @Column(name = "id")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    val id: UUID = UUID.randomUUID(),

    @Column(name = "client_id")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    val parentClientId: UUID,

    @Column(name = "display_name")
    var displayName: String,

    @Column(name = "delta_tick")
    var deltaTick: Int = 5 * 60 * 1000,

    @Column(name = "last_success_delta")
    var lastSuccessDelta: Long = 0,

    @Column(name = "user_principal_name")
    val userPrincipalName: String,

    @Column(name = "expired")
    var accountExpired: Boolean = false,

    @Column(name = "create_at")
    val createAt: Long = System.currentTimeMillis(),

    @Column(name = "create_by")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    val createBy: UUID,

    @Column(name = "modify_at")
    var modifyAt: Long = System.currentTimeMillis(),
)