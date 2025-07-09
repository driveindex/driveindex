package io.github.driveindex.database.dao.attributes

import io.github.driveindex.client.onedrive.OneDriveEndpoint
import io.github.driveindex.database.entity.account.AccountEntity
import io.github.driveindex.database.entity.client.ClientEntity
import io.github.driveindex.database.entity.file.FileEntity
import org.springframework.stereotype.Repository
import java.util.*

interface OneDriveAccountAttributeDao: AccountAttributeDao {
//    @Query("select account from driveindex_account account" +
//            " where " +
//            "account.type = 'OneDrive' and " +
//            "account.attribute->>'azure_client_id' = :azureId", nativeQuery = true)
    fun findByAzureId(azureId: String): AccountEntity?

//    @Query("update OneDriveAccountEntity set deltaToken=:token where id=:id")
    fun setDeltaTokenById(id: UUID, token: String?)
}

interface OneDriveClientAttributeDao: ClientAttributeDao {
//    @Query("from ClientEntity where clientId=:id")
    fun getClient(id: Int): ClientEntity
//    @Query("from OneDriveClientEntity where" +
//            " id in :ids" +
//            " and clientId=:azureClientId" +
//            " and clientSecret=:azureClientSecret" +
//            " and endPoint=:endPoint" +
//            " and tenantId=:tenantId")
    fun findClient(
        ids: List<UUID>, azureClientId: String, azureClientSecret: String,
        endPoint: OneDriveEndpoint, tenantId: String,
    ): ClientEntity?
}

interface OneDriveFileAttributeDao: FileAttributeDao {
//    @Modifying
//    @Query("delete OneDriveFileEntity where id=:id")
    fun deleteByUUID(id: UUID)
//    @Query("from OneDriveFileEntity where id=:id")
    fun getFile(id: UUID): FileEntity?
//    @Query("from OneDriveFileEntity where fileId=:fileId and accountId=:accountId")
    fun findByAzureFileId(fileId: String, accountId: UUID): FileEntity?
}