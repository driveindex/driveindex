package io.github.driveindex.module

import io.github.driveindex.controller.checkUsername
import io.github.driveindex.core.util.MD5_FULL
import io.github.driveindex.core.util.SHA_256
import io.github.driveindex.database.dao.findByUsername
import io.github.driveindex.database.entity.UserEntity
import io.github.driveindex.dto.req.admin.UserCreateRequestDto
import io.github.driveindex.dto.resp.CommonSettingsUserItemRespDto
import io.github.driveindex.exception.FailedResult
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.springframework.stereotype.Component
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Component
class AdminModel {
    fun createUser(dto: UserCreateRequestDto) {
        if (UserEntity.findByUsername(dto.username) != null) {
            throw FailedResult.User.UserFound
        }

        UserEntity.insert {
            it[username] = dto.username.checkUsername()
            val pwdSalt = UUID.randomUUID().toString().MD5_FULL
            it[passwordSalt] = pwdSalt
            it[passwordHash] = "${dto.password}${pwdSalt}".SHA_256
            it[role] = dto.role
            it[enabled] = dto.enabled
        }
    }

    fun getAllUsers(): List<CommonSettingsUserItemRespDto> {
        return UserEntity.run {
            selectAll().map {
                CommonSettingsUserItemRespDto(
                    id = it[id].value,
                    username = it[username],
                    role = it[role],
                    enabled = it[enabled],
                )
            }
        }
    }

//    fun editUser(dto: UserEditReqDto) {
//        val user = UserEntity.findById(dto.id).getOrNull()
//            ?: throw FailedResult.AdminUser.UserNotFound
//        user.run {
//            dto.username?.let {
//                username = it.checkUsername()
//            }
//            dto.password?.let { password = it }
//            dto.role?.let { role = it }
//            dto.enable?.let { enable = it }
//        }
//        UserEntity.save(user)
//    }
}