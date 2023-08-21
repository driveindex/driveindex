package io.github.driveindex.module

import io.github.driveindex.core.util.log
import io.github.driveindex.database.dao.*
import jakarta.persistence.EntityManagerFactory
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import java.lang.IllegalStateException
import java.util.*

/**
 * @author sgpublic
 * @Date 8/5/23 1:03 PM
 */
@Component
class DeletionModule(
    private val userDao: UserDao,
    private val clientsDao: ClientsDao,
    private val accountsDao: AccountsDao,
    private val fileDao: FileDao,
    private val sharedLinkDao: SharedLinkDao,
) {
    @Transactional
    fun doUserDeleteAction(userId: UUID) {
        transaction("删除用户失败") {
            doRealUserDeleteAction(userId)
        }
    }

    private fun doRealUserDeleteAction(userId: UUID) {
        userDao.deleteByUUID(userId)
        for (clientsEntity in clientsDao.listByUser(userId)) {
            doRealClientDeleteAction(clientsEntity.id)
        }
    }

    @Transactional
    fun doClientDeleteAction(clientId: UUID) {
        transaction("删除 client 失败") {
            doRealClientDeleteAction(clientId)
        }
    }

    private fun doRealClientDeleteAction(clientId: UUID) {
        clientsDao.deleteByUUID(clientId)
        for (accountsEntity in accountsDao.listByClient(clientId)) {
            doRealAccountDeleteAction(accountsEntity.id)
        }
    }

    @Transactional
    fun doAccountDeleteAction(accountId: UUID) {
        transaction("删除账号失败") {
            doRealAccountDeleteAction(accountId)
        }
    }

    private fun doRealAccountDeleteAction(accountId: UUID) {
        accountsDao.deleteByUUID(accountId)
        for (accountsEntity in accountsDao.listByClient(accountId)) {
            for (fileEntity in fileDao.listByAccount(accountsEntity.id)) {
                doRealFileDeleteAction(fileEntity.id)
            }
            for (sharedLinkEntity in sharedLinkDao.listByAccount(accountsEntity.id)) {
                doRealSharedLinkDeleteAction(sharedLinkEntity.id)
            }
        }
    }

    @Transactional
    fun doFileDeleteAction(fileId: UUID) {
        transaction("删除文件失败") {
            doRealFileDeleteAction(fileId)
        }
    }

    private fun doRealFileDeleteAction(fileId: UUID) {
        fileDao.deleteByUUID(fileId)
    }

    @Transactional
    fun doSharedLinkDeleteAction(linkId: UUID) {
        transaction("删除分享链接失败") {
            doRealSharedLinkDeleteAction(linkId)
        }
    }

    private fun doRealSharedLinkDeleteAction(linkId: UUID) {
        sharedLinkDao.deleteByUUID(linkId)
    }

    private fun transaction(errorMessage: String, block: () -> Unit) {
        try {
            block()
        } catch (e: Exception) {
            throw IllegalStateException(errorMessage, e)
        }
    }
}
