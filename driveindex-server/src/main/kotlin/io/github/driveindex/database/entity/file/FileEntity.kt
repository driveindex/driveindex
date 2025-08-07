package io.github.driveindex.database.entity.file

import io.github.driveindex.Application
import io.github.driveindex.database.entity.AttributeEntity
import io.github.driveindex.database.entity.AttributeEntity.Companion.attribute
import io.github.driveindex.database.entity.VersionControlEntity
import io.github.driveindex.database.entity.file.attributes.FileAttribute
import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.dao.id.UUIDTable
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi

/**
 * @author sgpublic
 * @Date 2023/3/29 下午12:54
 */
@OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
object FileEntity: UUIDTable("${Application.BASE_NAME_LOWER}_file"),
    VersionControlEntity, AttributeEntity<FileAttribute> {
    val name = varchar("name", length = 256)
    val parentId = uuid("parent_id").nullable()
    val isRemote = bool("is_remote").default(false)
    val isMount = bool("is_mount").default(false)
    val isRoot = bool("is_root").default(false)
    val isDir = bool("is_dir").default(false)
    override val createAt = createAt()
    override val createBy = createBy()
    override val modifyAt = modifyAt()
    override val modifyBy = modifyBy()
    override val attribute: Column<FileAttribute> = attribute()
}
