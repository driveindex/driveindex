package io.github.driveindex.controller

import io.github.driveindex.database.dao.UserDao
import io.github.driveindex.database.entity.UserEntity
import io.github.driveindex.dto.req.admin.UserCreateRequestDto
import io.github.driveindex.dto.req.admin.UserDeleteRequestDto
import io.github.driveindex.dto.resp.RespResult
import io.github.driveindex.dto.resp.SampleResult
import io.github.driveindex.dto.resp.admin.CommonSettingsUserItemRespDto
import io.github.driveindex.dto.resp.admin.FullSettingsRespDto
import io.github.driveindex.dto.resp.resp
import io.github.driveindex.exception.FailedResult
import io.github.driveindex.module.DeletionModule
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import kotlin.jvm.optionals.getOrNull

@RestController
@Tag(name = "管理页接口")
class AdminController(
    private val userDao: UserDao,

    private val deletionModule: DeletionModule,
) {
    @PostMapping("/api/admin/user/create")
    fun createUser(@RequestBody dto: UserCreateRequestDto): SampleResult {
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
        return SampleResult
    }

    @GetMapping("/api/admin/user")
    fun getUsers(): RespResult<List<CommonSettingsUserItemRespDto>> {
        return userDao.findAll().map {
            CommonSettingsUserItemRespDto(
                id = it.id,
                username = it.username,
                nick = it.nick,
                role = it.role,
                enable = it.enable,
                corsOrigin = it.corsOrigin,
            )
        }.resp()
    }

    @PostMapping("/api/admin/user/edit")
    fun editUser(@RequestBody dto: FullSettingsRespDto) {
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

    @PostMapping("/api/admin/user/delete")
    fun deleteUsers(@RequestBody dto: UserDeleteRequestDto): SampleResult {
        val user = userDao.findById(dto.userId).getOrNull()
            ?: throw FailedResult.AdminUser.UserNotFound
        deletionModule.doUserDeleteAction(user.id)
        return SampleResult
    }
}