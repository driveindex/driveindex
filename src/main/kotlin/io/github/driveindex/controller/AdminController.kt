package io.github.driveindex.controller

import com.vaadin.hilla.BrowserCallable
import io.github.driveindex.database.dao.UserDao
import io.github.driveindex.database.entity.UserEntity
import io.github.driveindex.dto.req.admin.UserCreateRequestDto
import io.github.driveindex.dto.req.admin.UserDeleteRequestDto
import io.github.driveindex.dto.resp.CommonSettingsUserItemRespDto
import io.github.driveindex.dto.resp.FullSettingsRespDto
import io.github.driveindex.dto.resp.ListResp
import io.github.driveindex.dto.resp.Resp
import io.github.driveindex.exception.FailedResult
import io.github.driveindex.module.DeletionModule
import io.github.driveindex.security.SecurityConfig
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.annotation.security.RolesAllowed
import org.springframework.scheduling.annotation.Async
import org.springframework.web.bind.annotation.RequestBody
import javax.annotation.Nonnull
import kotlin.jvm.optionals.getOrNull

@Tag(name = "管理页接口")
@BrowserCallable
@RolesAllowed(SecurityConfig.ROLE_ADMIN)
class AdminController(
    private val userDao: UserDao,

    private val deletionModule: DeletionModule,
) {
    @Async
    @Nonnull
    fun createUser(@RequestBody dto: UserCreateRequestDto) = Resp {
        if (userDao.getUserByUsername(dto.username) != null) {
            throw FailedResult.User.UserFound
        }

        userDao.save(UserEntity(
            username = dto.username.checkUsername(),
            password = dto.password,
            nick = dto.nick.checkNick(),
            role = dto.role,
            enable = dto.enable,
        ))
    }

    @Async
    @Nonnull
    fun getUsers() = ListResp {
        userDao.findAll().map {
            CommonSettingsUserItemRespDto(
                id = it.id,
                username = it.username,
                nick = it.nick,
                role = it.role,
                enable = it.enable,
                corsOrigin = it.corsOrigin,
            )
        }
    }

    @Async
    @Nonnull
    fun editUser(@RequestBody dto: FullSettingsRespDto) = Resp {
        val user = userDao.findById(dto.id).getOrNull()
            ?: throw FailedResult.AdminUser.UserNotFound
        user.run {
            dto.nick?.let {
                nick = it.checkNick()
            }
            dto.username?.let {
                username = it.checkUsername()
            }
            dto.password?.let { password = it }
            dto.role?.let { role = it }
            dto.enable?.let { enable = it }
            dto.corsOrigin?.let { corsOrigin = it }
        }
        userDao.save(user)
    }

    @Async
    @Nonnull
    fun deleteUsers(@RequestBody dto: UserDeleteRequestDto) = Resp {
        val user = userDao.findById(dto.userId).getOrNull()
            ?: throw FailedResult.AdminUser.UserNotFound
        deletionModule.doUserDeleteAction(user.id)
    }
}