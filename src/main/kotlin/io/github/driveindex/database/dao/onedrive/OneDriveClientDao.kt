package io.github.driveindex.database.dao.onedrive

import io.github.driveindex.database.entity.onedrive.OneDriveClientEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface OneDriveClientDao: JpaRepository<OneDriveClientEntity, UUID> {
    @Query("from OneDriveClientEntity where id=:id")
    fun getClient(id: UUID): OneDriveClientEntity
    @Query("from OneDriveClientEntity where" +
        " id in :ids" +
        " and clientId=:azureClientId" +
        " and clientSecret=:azureClientSecret" +
        " and endPoint=:endPoint" +
        " and tenantId=:tenantId")
    fun findClient(
        ids: List<UUID>, azureClientId: String, azureClientSecret: String,
        endPoint: OneDriveClientEntity.EndPoint, tenantId: String,
    ): OneDriveClientEntity?
}