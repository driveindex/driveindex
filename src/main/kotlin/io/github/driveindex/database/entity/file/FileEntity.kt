package io.github.driveindex.database.entity.file

import com.fasterxml.jackson.databind.node.ObjectNode
import io.github.driveindex.client.ClientType
import io.github.driveindex.core.util.CanonicalPath
import io.github.driveindex.core.util.Json
import io.github.driveindex.database.converter.CanonicalPathConverter
import io.github.driveindex.database.entity.AttributeEntity
import io.github.driveindex.database.entity.IdEntity
import io.github.driveindex.database.entity.TimeEntity
import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.util.*

/**
 * @author sgpublic
 * @Date 2023/3/29 下午12:54
 */
@Entity
@Table(name = "driveindex_file")
data class FileEntity(
    @Id
    @Column(name = "id")
    override val id: Int = 0,

    @Column(name = "account_id")
    val accountId: Int?,

    @Column(name = "client_type")
    @Enumerated(EnumType.STRING)
    val clientType: ClientType?,

    @Column(name = "name")
    var name: String,

    @Column(name = "parent_id")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    val parentId: Int?,

    @Convert(converter = CanonicalPathConverter::class)
    @Column(name = "path", length = 2048)
    val path: CanonicalPath,

    @Column(name = "path_hash", length = 256)
    val pathHash: String,

    @Column(name = "is_dir")
    val isDir: Boolean,

    @Column(name = "link_target")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    val linkTarget: Int? = null,

    @Column(name = "is_remote")
    val isRemote: Boolean = false,

    @Column(name = "size")
    val size: Long = 0,

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
