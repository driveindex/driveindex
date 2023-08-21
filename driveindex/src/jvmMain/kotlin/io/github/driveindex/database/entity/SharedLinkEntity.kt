package io.github.driveindex.database.entity

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
@Table(name = "shared")
data class SharedLinkEntity(
    @Id
    @Column(name = "id")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    val id: UUID = UUID.randomUUID(),

    @Column(name = "root_target")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    val rootTarget: UUID,

    @Column(name = "parent_account")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    val parentAccount: UUID,

    @Column(name = "expired_time")
    val expireTime: Long = -1,
)
