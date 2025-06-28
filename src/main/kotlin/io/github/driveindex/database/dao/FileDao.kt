package io.github.driveindex.database.dao

import io.github.driveindex.core.util.CanonicalPath
import io.github.driveindex.database.entity.file.FileEntity
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
interface FileDao: JpaRepository<FileEntity, Int> {
    @Modifying
    @Query("delete FileEntity where id=:id")
    override fun deleteById(id: Int)

    @Modifying
    @Query("update FileEntity set name=:name where id=:id")
    fun rename(id: UUID, name: String)

    @Query("from FileEntity where pathHash=:pathHash and createBy=:createBy")
    fun findAnyFileByPathIntern(pathHash: String, createBy: Int): FileEntity?

    @Query("from FileEntity where pathHash=:pathHash and createBy=:createBy and isRemote=:isRemote")
    fun findFileByPathIntern(pathHash: String, createBy: Int, isRemote: Boolean): FileEntity?

    @Query("from FileEntity where pathHash=:pathHash and accountId=:account and isRemote=true")
    fun findRemoteFileByPath(pathHash: String, account: Int): FileEntity?

    @Query("from FileEntity where id=:parent")
    fun findOneById(id: Int): FileEntity?

    @Query("from FileEntity where parentId=:parent")
    fun findByParent(parent: Int): List<FileEntity>

    @Query("from FileEntity where accountId=:account")
    fun listByAccount(account: Int): List<FileEntity>

    @Query("from FileEntity where createBy=:user")
    fun listByUser(user: Int): List<FileEntity>
}

/**
 * 返回用户创建的本地目录
 */
fun FileDao.getUserFile(path: CanonicalPath, createBy: Int): FileEntity {
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
fun FileDao.getTopUserFile(path: CanonicalPath, createBy: Int): FileEntity {
    var entity: FileEntity? = findAnyFileByPathIntern(path.pathSha256, createBy)
    while (entity != null && entity.isRemote) {
        entity = findOneById(entity.parentId ?: break)
    }
    return entity?.takeIf { it.linkTarget != null }
        ?: throw FailedResult.Dir.TargetNotFound
}
