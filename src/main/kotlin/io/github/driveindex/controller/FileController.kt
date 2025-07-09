package io.github.driveindex.controller

import com.vaadin.hilla.BrowserCallable
import io.github.driveindex.annotation.AllOpen
import io.github.driveindex.client.ClientType
import io.github.driveindex.database.entity.file.FileEntity
import io.github.driveindex.database.entity.file.attributes.RemoteFileAttribute
import io.github.driveindex.dto.req.user.CreateDirReqDto
import io.github.driveindex.dto.req.user.DeleteDirReqDto
import io.github.driveindex.dto.req.user.ListDirReqDto
import io.github.driveindex.dto.req.user.RenameDirReqDto
import io.github.driveindex.dto.resp.FileListRespDto
import io.github.driveindex.dto.resp.ObjResp
import io.github.driveindex.dto.resp.Resp
import io.github.driveindex.exception.FailedResult
import io.github.driveindex.module.Current
import io.github.driveindex.module.file.FileModel
import io.github.driveindex.security.SecurityConfig
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.annotation.security.RolesAllowed
import org.springframework.web.bind.annotation.*
import java.util.*
import kotlin.uuid.ExperimentalUuidApi

/**
 * @author sgpublic
 * @Date 2023/3/29 上午10:50
 */
@AllOpen
@OptIn(ExperimentalUuidApi::class)
//@BrowserCallable
@Tag(name = "用户文件接口")
class FileController(
    private val current: Current,
    private val fileModel: FileModel,
) {
    @RolesAllowed(SecurityConfig.ROLE_USER)
    fun createDir(dto: CreateDirReqDto) = Resp {
        TODO()
//        fileModel.doCreateDir(dto, current.User.id)
    }

    @RolesAllowed(SecurityConfig.ROLE_USER)
    fun listFile(dto: ListDirReqDto) = ObjResp<FileListRespDto> {
        TODO()
//        val currentDir = fileDao.findById(dto.pathId, current.User.id)
//            ?: throw FailedResult.Dir.TargetNotFound
//        if (!currentDir.type.isDir()) {
//            throw FailedResult.Dir.NotADir
//        }
//
//        val findByParent: List<FileEntity> = if (currentDir.type == FileEntity.Type.REMOTE_DIR) {
//            currentDir.useAttribute { entity, attr: RemoteFileAttribute ->
//                val account = accountDao.getAccount(attr.accountId)
//                    ?.takeIf { it.createBy == current.User.id }
//                    ?: throw FailedResult.Account.NotFound
//                val findRemoteFileByPath = fileDao.findRemoteFileByPath(path.pathSha256, account.id)
//                    ?: throw FailedResult.Dir.TargetNotFound
//                if (!findRemoteFileByPath.isDir) {
//                    throw FailedResult.Dir.NotADir
//                }
//                fileDao.findByParentId(findRemoteFileByPath.id, current.User.id)
//            }
//        } else {
//            val findUserFileByPath = fileDao.findByParentId(currentDir.id, current.User.id)
//            if (findUserFileByPath != null) {
//                if (findUserFileByPath.linkTarget != null) {
//                    val linkTarget = fileDao.findById(findUserFileByPath.linkTarget)
//                        ?: throw FailedResult.Dir.TargetNotFound
//                    if (!linkTarget.isDir) {
//                        throw FailedResult.Dir.NotADir
//                    }
//                    // ^ findByParent
//                    fileDao.findByParent(findUserFileByPath.linkTarget)
//                } else {
//                    // ^ findByParent
//                    fileDao.findByParent(findUserFileByPath.id)
//                }
//            } else {
//                val top = fileDao.getTopUserFile(path, current.User.id)
//                val linkTarget = fileDao.findById(top.linkTarget!!)
//                    ?: throw FailedResult.Dir.TargetNotFound
//                if (!linkTarget.isDir) {
//                    throw FailedResult.Dir.NotADir
//                }
//                val targetPath = linkTarget.path.append(path.subPath(top.path.length))
//                val inlinkTarget = fileDao.findRemoteFileByPath(targetPath.pathSha256, linkTarget.accountId!!)
//                    ?: throw FailedResult.Dir.TargetNotFound
//                // ^ findByParent
//                fileDao.findByParent(inlinkTarget.id)
//            }
//        }
//
//        FileListRespDto(
//            contentSize = findByParent.size,
//            content = findByParent.sortedBy {
//                it.name
//            }.map {
//                return@map FileListRespDto.Item(
//                    name = it.name,
//                    createAt = it.createAt,
//                    modifyAt = it.modifyAt,
//                    isDir = it.isDir,
//                    isLink = it.linkTarget != null,
//                    type = it.clientType,
//                    detail = when (it.clientType!!) {
//                        ClientType.OneDrive ->
//                            oneDriveFileDao.getReferenceById(it.id).let onedrive@{ byId ->
//                                return@onedrive FileListRespDto.Item.OneDrive(
//                                    webUrl = byId.webUrl,
//                                    mimeType = byId.mimeType,
//                                )
//                            }
//                    }
//                )
//            }
//        )
    }

    @RolesAllowed(SecurityConfig.ROLE_USER)
    fun deleteItem(dto: DeleteDirReqDto) = Resp {
        TODO()
//        val file = fileDao.getUserFile(dto.path, current.User.id)
//        fileModel.doFileDeleteAction(file.id)
    }

    @RolesAllowed(SecurityConfig.ROLE_USER)
    fun renameItem(dto: RenameDirReqDto) = Resp {
        TODO()
//        val file = fileDao.getUserFile(dto.path, current.User.id)
//        fileDao.rename(file.id, dto.name)
    }
}