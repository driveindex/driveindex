package io.github.driveindex.module

import io.github.driveindex.core.exception.FailedResult
import io.github.driveindex.core.util.SHA_256
import io.github.driveindex.database.dao.findByUsername
import io.github.driveindex.database.dao.update
import io.github.driveindex.database.entity.UserEntity
import io.github.driveindex.dto.req.user.CommonSettingsReqDto
import io.github.driveindex.dto.resp.UserInfoRespDto
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Component

@Component
class UserModel(
    private val current: Current
) {
    internal fun login(username: String, passwordHash: String): UsernamePasswordAuthenticationToken {
        val entity = transaction {
            UserEntity.findByUsername(username)?.takeIf {
                it[UserEntity.passwordHash] == "${passwordHash}${it[UserEntity.passwordSalt]}".SHA_256
            } ?: throw FailedResult.Auth.WrongPassword
        }

        if (!entity[UserEntity.enabled]) {
            throw FailedResult.Auth.UserDisabled
        }

        return UsernamePasswordAuthenticationToken(
            entity[UserEntity.id].value, null,
            entity[UserEntity.role].authorities
        )
    }

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