package io.github.driveindex.database.entity

import io.github.driveindex.security.UserRole
import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.util.UUID

@Entity
@Table(name = "users")
data class UserEntity(
    @Id
    @Column(name = "id")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    val id: UUID = UUID.randomUUID(),

    @Column(name = "username")
    var username: String,

    @Column(name = "password")
    var password: String,

    @Column(name = "nick")
    var nick: String = "",

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    var role: UserRole = UserRole.USER,

    @Column(name = "enable")
    var enable: Boolean = true,

    @Column(name = "cors_origin")
    var corsOrigin: String = "",
)