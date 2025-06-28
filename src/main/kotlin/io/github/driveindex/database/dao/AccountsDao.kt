package io.github.driveindex.database.dao

import io.github.driveindex.database.entity.account.AccountEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AccountsDao: JpaRepository<AccountEntity, Int> {
    @Modifying
    @Query("delete AccountEntity where id=:id")
    override fun deleteById(id: Int)

    @Query("from AccountEntity where id=:accountId")
    fun getAccount(accountId: Int): AccountEntity?

    @Query("from AccountEntity where parentClientId=:clientId and displayName=:name")
    fun findByName(clientId: String, name: String): AccountEntity?

    @Query("from AccountEntity where parentClientId=:clientId")
    fun findByClient(clientId: String): List<AccountEntity>

    @Query("select id from AccountEntity where parentClientId=:clientId")
    fun selectIdByClient(clientId: String): List<UUID>

    @Query("from AccountEntity where parentClientId=:clientId")
    fun listByClient(clientId: String): List<AccountEntity>
}