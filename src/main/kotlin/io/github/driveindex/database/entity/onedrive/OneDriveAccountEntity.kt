package io.github.driveindex.database.entity.onedrive

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.util.*

@Entity
@Table(name = "account_onedrive")
data class OneDriveAccountEntity(
    @Id
    @Column(name = "id")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    val id: UUID,

    @Column(name = "azure_user_id")
    val azureUserId: String,

    @Column(name = "token_type")
    var tokenType: String,

    @Column(name = "access_token", length = 2048)
    var accessToken: String,

    @Column(name = "token_expire")
    var tokenExpire: Long,

    @Column(name = "refresh_token", length = 1024)
    var refreshToken: String,

    @Column(name = "delta_token")
    var deltaToken: String? = null,
)