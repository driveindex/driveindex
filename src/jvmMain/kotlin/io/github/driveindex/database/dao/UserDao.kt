package io.github.driveindex.database.dao

import io.github.driveindex.database.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserDao: JpaRepository<UserEntity, UUID> {
    @Modifying
    @Query("delete UserEntity where id=:id")
    fun deleteByUUID(id: UUID)

    @Query("from UserEntity where username=:username")
    fun getUserByUsername(username: String): UserEntity?
}

