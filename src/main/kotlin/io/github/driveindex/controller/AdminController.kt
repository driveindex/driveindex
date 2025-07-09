package io.github.driveindex.controller

import com.vaadin.hilla.BrowserCallable
import io.github.driveindex.annotation.AllOpen
import io.github.driveindex.dto.req.admin.UserCreateRequestDto
import io.github.driveindex.dto.req.admin.UserDeleteRequestDto
import io.github.driveindex.dto.resp.FullSettingsRespDto
import io.github.driveindex.dto.resp.ListResp
import io.github.driveindex.dto.resp.Resp
import io.github.driveindex.module.AdminModel
import io.github.driveindex.module.file.FileModel
import io.github.driveindex.security.SecurityConfig
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.annotation.security.RolesAllowed
import org.springframework.scheduling.annotation.Async
import javax.annotation.Nonnull

@Tag(name = "管理页接口")
@AllOpen
@BrowserCallable
@RolesAllowed(SecurityConfig.ROLE_ADMIN)
class AdminController(
    private val adminModel: AdminModel,
    private val fileModel: FileModel,
) {
    @Async
    @Nonnull
    fun createUser(dto: UserCreateRequestDto) = Resp {
        adminModel.createUser(dto)
    }

    @Async
    @Nonnull
    fun getAllUsers() = ListResp {
        adminModel.getAllUsers()
    }

    @Async
    @Nonnull
    fun editUser(dto: FullSettingsRespDto) = Resp {
        TODO()
//        adminModel.editUser(dto)
    }

    @Async
    @Nonnull
    fun deleteUsers(dto: UserDeleteRequestDto) = Resp {
        TODO()
//        val user = UserEntity.findById(dto.userId).getOrNull()
//            ?: throw FailedResult.AdminUser.UserNotFound
//        fileModel.doUserDeleteAction(user.id)
    }
}