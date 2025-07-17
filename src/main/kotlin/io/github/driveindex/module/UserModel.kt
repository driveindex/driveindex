package io.github.driveindex.module

import io.github.driveindex.database.dao.update
import io.github.driveindex.database.entity.UserEntity
import io.github.driveindex.dto.req.user.CommonSettingsReqDto
import io.github.driveindex.dto.resp.UserInfoRespDto
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.springframework.stereotype.Component

@Component
class UserModel(
    private val current: Current
) {
    fun getUser(): UserInfoRespDto {
        val user = current.AuthedUser
        return UserInfoRespDto(
            username = user[UserEntity.username],
            nickname = user[UserEntity.attribute].nickname,
            role = user[UserEntity.role],
            permission = user[UserEntity.permission]
        )
    }

    fun updateUser(dto: CommonSettingsReqDto) {
        transaction {
            UserEntity.update(
                dto.username ?: current.AuthedUser[UserEntity.username],
                current.AuthedUser[UserEntity.role],
                current.AuthedUser[UserEntity.permission],
                current.AuthedUser[UserEntity.attribute].also { attribute ->
                    attribute.nickname = dto.nickname
                },
            )
        }
    }
}