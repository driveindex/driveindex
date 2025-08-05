package io.github.driveindex.module

import io.github.driveindex.controller.checkUsername
import io.github.driveindex.core.exception.FailedResult
import io.github.driveindex.utils.CanonicalPath
import io.github.driveindex.utils.MD5_FULL
import io.github.driveindex.utils.SHA_256
import io.github.driveindex.database.dao.findByUsername
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
            val newUser = UserEntity.insert {
                it[username] = dto.username.checkUsername()
                it[passwordHash] = "${dto.password}${pwdSalt}".SHA_256
                it[passwordSalt] = pwdSalt
                it[role] = dto.role
                it[permission] = dto.permission
                it[attribute] = UserAttribute(
                    nickname = dto.nickname
                )
            }
            FileEntity.insert {
                it[name] = CanonicalPath.ROOT_PATH
                it[parentId] = null
                it[isRoot] = true
                it[isDir] = true
                it.createBy(newUser[UserEntity.id].value)
                it[attribute] = LocalDirAttribute()
            }
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