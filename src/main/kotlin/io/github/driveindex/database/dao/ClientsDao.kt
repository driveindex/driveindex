package io.github.driveindex.database.dao

import io.github.driveindex.database.entity.client.ClientEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ClientsDao: JpaRepository<ClientEntity, UUID> {
    @Modifying
    @Query("delete ClientEntity where id=:id")
    fun deleteByUUID(id: Int)

    @Query("from ClientEntity where id=:id")
    fun getClient(id: Int): ClientEntity?

    @Query("from ClientEntity where createBy=:user and name=:name")
    fun findByName(user: Int, name: String): ClientEntity?

    @Query("select id from ClientEntity where createBy=:user")
    fun findByUser(user: Int): List<Int>

    @Query("from ClientEntity where createBy=:user")
    fun listByUser(user: Int): List<ClientEntity>

    @Query("from ClientEntity where supportDelta=true")
    fun listIfSupportDelta(): List<ClientEntity>
}