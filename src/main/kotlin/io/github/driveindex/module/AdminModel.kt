package io.github.driveindex.module

import io.github.driveindex.controller.checkUsername
import io.github.driveindex.core.exception.FailedResult
import io.github.driveindex.core.util.CanonicalPath
import io.github.driveindex.core.util.MD5_FULL
import io.github.driveindex.core.util.SHA_256
import io.github.driveindex.database.dao.findByUsername
import io.github.driveindex.database.dao.insertRootForUser
import io.github.driveindex.database.dao.new
import io.github.driveindex.database.entity.UserAttribute
import io.github.driveindex.database.entity.UserEntity
import io.github.driveindex.database.entity.file.FileEntity
import io.github.driveindex.database.entity.file.attributes.LocalDirAttribute
import io.github.driveindex.dto.req.admin.UserCreateRequestDto
import io.github.driveindex.dto.resp.CommonSettingsUserItemRespDto
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.springframework.stereotype.Component
import java.util.*

@Component
class AdminModel(
    private val current: Current,
) {
    fun createUser(dto: UserCreateRequestDto) {
        transaction {
            if (UserEntity.findByUsername(dto.username) != null) {
                throw FailedResult.User.UserExist
            }

            val pwdSalt = UUID.randomUUID().toString().MD5_FULL
            val newUser = UserEntity.new(
                uUsername = dto.username.checkUsername(),
                uPasswordHash = "${dto.password}${pwdSalt}".SHA_256,
                uPasswordSalt = pwdSalt,
                uRole = dto.role,
                uPermission = dto.permission,
                uAttribute = UserAttribute(
                    nickname = dto.nickname
                )
            )
            FileEntity.insertRootForUser(newUser[UserEntity.id].value)
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