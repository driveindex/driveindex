package io.github.driveindex.database.entity

import io.github.driveindex.client.ClientType
import io.github.driveindex.core.util.CanonicalPath
import io.github.driveindex.database.converter.CanonicalPathConverter
import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.util.*

/**
 * @author sgpublic
 * @Date 2023/3/29 下午12:54
 */
@Entity
@Table(name = "files")
data class FileEntity(
    @Id
    @Column(name = "id")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    val id: UUID = UUID.randomUUID(),

    @Column(name = "account_id")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    val accountId: UUID?,

    @Column(name = "client_type")
    @Enumerated(EnumType.STRING)
    val clientType: ClientType?,

    @Column(name = "create_by")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    val createBy: UUID?,

    @Column(name = "create_at")
    val createAt: Long = System.currentTimeMillis(),

    @Column(name = "modify_at")
    val modifyAt: Long = System.currentTimeMillis(),

    @Column(name = "name")
    var name: String,

    @Column(name = "parent_id")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    val parentId: UUID?,

    @Convert(converter = CanonicalPathConverter::class)
    @Column(name = "path", length = 2048)
    val path: CanonicalPath,

    @Column(name = "path_hash", length = 256)
    val pathHash: String,

    @Column(name = "is_dir")
    val isDir: Boolean,

    @Column(name = "link_target")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    val linkTarget: UUID? = null,

    @Column(name = "is_remote")
    val isRemote: Boolean = false,

    @Column(name = "size")
    val size: Long = 0,
)