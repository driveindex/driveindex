package io.github.driveindex.module.file

import io.github.driveindex.core.exception.FailedResult
import io.github.driveindex.utils.CanonicalPath
import io.github.driveindex.utils.asPath
import io.github.driveindex.core.utils.castOrNull
import io.github.driveindex.database.dao.findById
import io.github.driveindex.database.dao.findByNameAndParentId
import io.github.driveindex.database.dao.findRootByUserId
import io.github.driveindex.database.dao.listByParent
import io.github.driveindex.database.dao.pageable.Pageable
import io.github.driveindex.database.dao.pageable.paged
import io.github.driveindex.database.entity.file.FileEntity
import io.github.driveindex.database.entity.file.attributes.LocalDirAttribute
import io.github.driveindex.database.entity.file.attributes.LocalMountAttribute
import io.github.driveindex.database.entity.file.attributes.RemoteFileAttribute
import io.github.driveindex.dto.req.user.CreateDirReqDto
import io.github.driveindex.dto.req.user.GetDirReqSort
import io.github.driveindex.dto.req.user.ListDirReqDto
import io.github.driveindex.dto.resp.FileListRespDto
import io.github.driveindex.module.Current
import org.jetbrains.exposed.v1.core.Expression
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.springframework.stereotype.Component
import java.util.*
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * @author sgpublic
 * @Date 8/5/23 1:03 PM
 */
@OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
@Component
class FileModel(
    private val current: Current,
) {
    fun listDir(dto: ListDirReqDto): FileListRespDto = transaction {
        val currentDir = findRealPath(dto.path.asPath())
        if (!currentDir[FileEntity.isDir]) {
            throw FailedResult.Dir.NotADir
        }

        val findByParent = FileEntity.listByParent(currentDir[FileEntity.id].value, current.userId)
            .paged(Pageable(
                size = dto.pageSize,
                index = dto.pageIndex,
                sort = linkedMapOf(dto.sortBy.matchColumn() to if (dto.asc) SortOrder.ASC else SortOrder.DESC)
            ))

        val content = LinkedList<FileListRespDto.FileItem>()
        for (item in findByParent.result) {
            content.add(FileListRespDto.FileItem(
                name = item[FileEntity.name],
                id = item[FileEntity.id].value,
                createAt = item[FileEntity.createAt].epochSeconds,
                modifyAt = item[FileEntity.modifyAt].epochSeconds,
                isDir = item[FileEntity.isDir],
                isMount = item[FileEntity.isMount],
                isRemote = item[FileEntity.isRemote],
                detail = item[FileEntity.attribute]
                    .castOrNull { attr: RemoteFileAttribute ->
                        attr.toRespDtoDetail()
                    }
            ))
        }

        return@transaction FileListRespDto(
            contentSize = findByParent.totalSize,
            content = content
        )
    }

    fun GetDirReqSort.matchColumn(): Expression<*> {
        return when (this) {
            GetDirReqSort.NAME -> FileEntity.name
            GetDirReqSort.CREATE_TIME -> FileEntity.createAt
            GetDirReqSort.MODIFIED_TIME -> FileEntity.modifyAt
        }
    }

    fun findRealPath(path: CanonicalPath): ResultRow {
        var parentFile = FileEntity.findRootByUserId(current.userId)
        for (dirName in path) {
            if (parentFile[FileEntity.isMount]) {
                val mountId = (parentFile[FileEntity.attribute] as LocalMountAttribute).mountTarget
                parentFile = FileEntity.findById(mountId, current.userId)
                    ?: throw FailedResult.Dir.TargetNotFound
            }

            parentFile = FileEntity.findByNameAndParentId(dirName, parentFile[FileEntity.id].value, current.userId)
                ?: throw FailedResult.Dir.TargetNotFound
        }
        return parentFile
    }

    fun createDir(dto: CreateDirReqDto) = transaction {
        val dir = FileEntity.findById(dto.parentId, current.userId)
            ?: throw FailedResult.Dir.TargetNotFound

        if (dir[FileEntity.isRemote]) {
            // TODO: support manage remote
            throw FailedResult.Dir.ModifyRemote
        }

        if (FileEntity.findByNameAndParentId(dto.name, dto.parentId, current.userId) != null) {
            throw FailedResult.Dir.TargetExist
        }

        if (dto.mountTo == null) {
            FileEntity.insert {
                it[name] = dto.name
                it[parentId] = dto.parentId
                it.createBy(current.userId)
                it[isDir] = true
                it[attribute] = LocalDirAttribute()
            }
            return@transaction
        }

        FileEntity.findById(dto.mountTo, current.userId)
            ?: throw FailedResult.Dir.TargetNotFound
        FileEntity.insert {
            it[name] = dto.name
            it[parentId] = dto.parentId
            it.createBy(current.userId)
            it[isMount] = true
            it[attribute] = LocalMountAttribute(
                mountTarget = dto.mountTo
            )
        }
    }

    fun deleteFileByUser(userId: Int) {
        TODO()
//        userDao.deleteById(userId)
//        for (clientsEntity in clientDao.listByUser(userId)) {
//            doClientDeleteAction(clientsEntity.id)
//        }
    }

    fun deleteFileByClient(clientId: Int) {
        TODO()
//        clientDao.deleteByUUID(clientId)
//        for (accountsEntity in accountDao.listByClient(clientId)) {
//            doAccountDeleteAction(accountsEntity.id)
//        }
    }

    fun deleteFileByAccount(accountId: Int) {
        TODO()
//        accountDao.deleteById(accountId)
//        for (accountsEntity in accountDao.listByClient(accountId)) {
//            for (fileEntity in fileDao.listByAccount(accountsEntity.id)) {
//                doFileDeleteAction(fileEntity.id)
//            }
//            for (sharedLinkEntity in sharedLinkDao.listByAccount(accountsEntity.id)) {
//                doSharedLinkDeleteAction(sharedLinkEntity.id)
//            }
//        }
    }

    fun deleteFile(fileId: Uuid) {
        TODO()
//        fileDao.deleteById(fileId)
    }
}
