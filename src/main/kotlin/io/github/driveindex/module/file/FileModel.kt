package io.github.driveindex.module.file

import io.github.driveindex.database.dao.*
import io.github.driveindex.database.entity.file.FileEntity
import io.github.driveindex.database.entity.file.attributes.LocalLinkAttribute
import io.github.driveindex.dto.req.user.CreateDirReqDto
import io.github.driveindex.exception.FailedResult
import org.springframework.stereotype.Component
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * @author sgpublic
 * @Date 8/5/23 1:03 PM
 */
@OptIn(ExperimentalUuidApi::class)
@Component
class FileModel {
    fun doCreateDir(dto: CreateDirReqDto, userId: Int) {
        TODO()
//        val dir = fileDao.findById(dto.parentId, userId)
//            ?: throw FailedResult.Dir.TargetNotFound
//
//        if (dir.type != FileEntity.Type.LOCAL_DIR) {
//            throw FailedResult.Dir.ModifyRemote
//        }
//
//        val newFile = if (dto.linkTo == null) {
//            FileEntity(
//                name = dto.name,
//                parentId = dir.id,
//                type = FileEntity.Type.LOCAL_DIR,
//                createBy = userId,
//                modifyBy = userId,
//            )
//        } else {
//            FileEntity(
//                name = dto.name,
//                parentId = dir.id,
//                type = FileEntity.Type.LOCAL_MOUNT,
//                createBy = userId,
//                modifyBy = userId,
//            ).also {
//                it.writeAttribute(LocalLinkAttribute(
//                    linkTarget = dto.linkTo
//                ))
//            }
//        }
//
//        fileDao.save(newFile)
    }

    fun doUserDeleteAction(userId: Int) {
        TODO()
//        userDao.deleteById(userId)
//        for (clientsEntity in clientDao.listByUser(userId)) {
//            doClientDeleteAction(clientsEntity.id)
//        }
    }

    fun doClientDeleteAction(clientId: Int) {
        TODO()
//        clientDao.deleteByUUID(clientId)
//        for (accountsEntity in accountDao.listByClient(clientId)) {
//            doAccountDeleteAction(accountsEntity.id)
//        }
    }

    fun doAccountDeleteAction(accountId: Int) {
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

    fun doFileDeleteAction(fileId: Uuid) {
        TODO()
//        fileDao.deleteById(fileId)
    }

    fun doSharedLinkDeleteAction(linkId: Int) {
        TODO()
//        sharedLinkDao.deleteById(linkId)
    }
}
