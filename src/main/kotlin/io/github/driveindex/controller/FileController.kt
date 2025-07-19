package io.github.driveindex.controller

import com.vaadin.hilla.BrowserCallable
import io.github.driveindex.core.annotation.AllOpen
import io.github.driveindex.dto.req.user.CreateDirReqDto
import io.github.driveindex.dto.req.user.DeleteDirReqDto
import io.github.driveindex.dto.req.user.ListDirReqDto
import io.github.driveindex.dto.req.user.RenameDirReqDto
import io.github.driveindex.dto.resp.FileListRespDto
import io.github.driveindex.dto.resp.ObjResp
import io.github.driveindex.dto.resp.Resp
import io.github.driveindex.module.Current
import io.github.driveindex.module.file.FileModel
import io.github.driveindex.security.SecurityConfig
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.annotation.security.RolesAllowed
import org.springframework.security.access.prepost.PreAuthorize
import kotlin.uuid.ExperimentalUuidApi

/**
 * @author sgpublic
 * @Date 2023/3/29 上午10:50
 */
@AllOpen
@OptIn(ExperimentalUuidApi::class)
@BrowserCallable
@Tag(name = "用户文件接口")
@PreAuthorize(SecurityConfig.ROLE_USER)
class FileController(
    private val current: Current,
    private val fileModel: FileModel,
) {
    @RolesAllowed(SecurityConfig.ROLE_USER)
    fun createDir(dto: CreateDirReqDto) = Resp {
        fileModel.createDir(dto)
    }

    @RolesAllowed(SecurityConfig.ROLE_USER)
    fun listFile(dto: ListDirReqDto) = ObjResp {
        fileModel.listDir(dto)
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