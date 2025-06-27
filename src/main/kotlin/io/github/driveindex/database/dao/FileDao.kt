package io.github.driveindex.database.dao

import io.github.driveindex.core.util.CanonicalPath
import io.github.driveindex.database.entity.FileEntity
import io.github.driveindex.exception.FailedResult
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

/**
 * @author sgpublic
 * @Date 2023/3/29 下午12:54
 */
@Repository
interface FileDao: JpaRepository<FileEntity, UUID> {
    @Modifying
    @Query("delete FileEntity where id=:id")
    fun deleteByUUID(id: UUID)

    @Modifying
    @Query("update FileEntity set name=:name where id=:id")
    fun rename(id: UUID, name: String)

    @Query("from FileEntity where pathHash=:pathHash and createBy=:createBy")
    fun findAnyFileByPathIntern(pathHash: String, createBy: UUID): FileEntity?

    @Query("from FileEntity where pathHash=:pathHash and createBy=:createBy and isRemote=:isRemote")
    fun findFileByPathIntern(pathHash: String, createBy: UUID, isRemote: Boolean): FileEntity?

    @Query("from FileEntity where pathHash=:pathHash and accountId=:account and isRemote=true")
    fun findRemoteFileByPath(pathHash: String, account: UUID): FileEntity?

    @Query("from FileEntity where id=:parent")
    fun findByUUID(parent: UUID): FileEntity?

    @Query("from FileEntity where parentId=:parent")
    fun findByParent(parent: UUID): List<FileEntity>

    @Query("from FileEntity where accountId=:account")
    fun listByAccount(account: UUID): List<FileEntity>

    @Query("from FileEntity where createBy=:user")
    fun listByUser(user: UUID): List<FileEntity>
}

/**
 * 返回用户创建的本地目录
 */
fun FileDao.getUserFile(path: CanonicalPath, createBy: UUID): FileEntity {
    val target: FileEntity = findFileByPathIntern(path.pathSha256, createBy, false)
        ?: throw FailedResult.Dir.TargetNotFound
    if (path != target.path) {
        throw FailedResult.Dir.ModifyRemote
    }
    return target
}

/**
 * 根据指定路径，向上寻找由用户创建的软连接
 */
fun FileDao.getTopUserFile(path: CanonicalPath, createBy: UUID): FileEntity {
    var entity: FileEntity? = findAnyFileByPathIntern(path.pathSha256, createBy)
    while (entity != null && entity.isRemote) {
        entity = findByUUID(entity.parentId ?: break)
    }
    return entity?.takeIf { it.linkTarget != null }
        ?: throw FailedResult.Dir.TargetNotFound
}
