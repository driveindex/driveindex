package io.github.driveindex.database.dao

import io.github.driveindex.database.entity.SharedLinkEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

/**
 * @author sgpublic
 * @Date 8/5/23 1:42 PM
 */
@Repository
interface SharedLinkDao: JpaRepository<SharedLinkEntity, UUID> {
    @Modifying
    @Query("delete SharedLinkEntity where id=:id")
    fun deleteByUUID(id: UUID)
    @Query("from SharedLinkEntity where parentAccount=:accountId")
    fun listByAccount(accountId: UUID): List<SharedLinkEntity>
}