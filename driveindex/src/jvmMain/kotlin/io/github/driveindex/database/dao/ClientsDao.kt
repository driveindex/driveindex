package io.github.driveindex.database.dao

import io.github.driveindex.database.entity.ClientsEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ClientsDao: JpaRepository<ClientsEntity, UUID> {
    @Modifying
    @Query("delete ClientsEntity where id=:id")
    fun deleteByUUID(id: UUID)

    @Query("from ClientsEntity where id=:id")
    fun getClient(id: UUID): ClientsEntity?

    @Query("from ClientsEntity where createBy=:user and name=:name")
    fun findByName(user: UUID, name: String): ClientsEntity?

    @Query("select id from ClientsEntity where createBy=:user")
    fun findByUser(user: UUID): List<UUID>

    @Query("from ClientsEntity where createBy=:user")
    fun listByUser(user: UUID): List<ClientsEntity>

    @Query("from ClientsEntity where supportDelta=true")
    fun listIfSupportDelta(): List<ClientsEntity>
}