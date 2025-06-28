package io.github.driveindex.database.dao.attributes

import io.github.driveindex.client.onedrive.OneDriveEndpoint
import io.github.driveindex.database.entity.account.AccountEntity
import io.github.driveindex.database.entity.client.ClientEntity
import io.github.driveindex.database.entity.file.FileEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface OneDriveAttributeDao: JpaRepository<AccountEntity, UUID> {
    @Query("select account from driveindex_account account where account.id in :ids and account.attribute->>'azure_client_id' = :azureId", nativeQuery = true)
    fun findByAzureId(ids: List<String>, azureId: String): AccountEntity?

    @Query("update OneDriveAccountEntity set deltaToken=:token where id=:id")
    fun setDeltaTokenById(id: UUID, token: String?)
}

@Repository
interface OneDriveClientDao: JpaRepository<ClientEntity, UUID> {
    @Query("from OneDriveClientEntity where clientId=:id")
    fun getClient(id: String): ClientEntity
    @Query("from OneDriveClientEntity where" +
            " id in :ids" +
            " and clientId=:azureClientId" +
            " and clientSecret=:azureClientSecret" +
            " and endPoint=:endPoint" +
            " and tenantId=:tenantId")
    fun findClient(
        ids: List<UUID>, azureClientId: String, azureClientSecret: String,
        endPoint: OneDriveEndpoint, tenantId: String,
    ): ClientEntity?
}

@Repository
interface OneDriveFileDao: JpaRepository<FileEntity, UUID> {
    @Modifying
    @Query("delete OneDriveFileEntity where id=:id")
    fun deleteByUUID(id: UUID)
    @Query("from OneDriveFileEntity where id=:id")
    fun getFile(id: UUID): FileEntity?
    @Query("from OneDriveFileEntity where fileId=:fileId and accountId=:accountId")
    fun findByAzureFileId(fileId: String, accountId: UUID): FileEntity?
}