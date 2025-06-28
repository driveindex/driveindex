package io.github.driveindex.client

import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import io.github.driveindex.Application.Companion.Bean
import io.github.driveindex.client.onedrive.OneDriveAction
import io.github.driveindex.core.util.CanonicalPath
import io.github.driveindex.database.dao.AccountsDao
import io.github.driveindex.database.dao.ClientsDao
import io.github.driveindex.database.dao.FileDao
import io.github.driveindex.database.entity.AccountEntity
import io.github.driveindex.database.entity.ClientEntity
import io.github.driveindex.database.entity.account.AccountEntity
import io.github.driveindex.database.entity.client.ClientEntity
import io.github.driveindex.dto.resp.AccountCreateRespDto
import io.github.driveindex.exception.FailedResult
import io.github.driveindex.module.Current
import jakarta.annotation.PostConstruct
import java.util.*
import kotlin.reflect.KClass

interface ClientAction {
    fun loginUri(clientId: Int, redirectUri: String): AccountCreateRespDto
    fun loginRequest(params: ObjectNode)

    val type: ClientType
    fun onConstruct() { }

    fun create(name: String, params: ObjectNode)
    fun edit(params: ObjectNode, clientId: Int)


    fun listFile(path: CanonicalPath, accountId: Int): ArrayNode
    fun downloadFile(path: CanonicalPath, accountId: Int): String

    fun needDelta(accountId: Int): Boolean
    fun delta(accountId: Int)
}

abstract class AbsClientAction(
    final override val type: ClientType
): ClientAction {
    protected val current: Current by lazy { Current::class.Bean }
    protected val fileDao: FileDao by lazy { FileDao::class.Bean }
    protected val clientDao: ClientsDao by lazy { ClientsDao::class.Bean }
    protected val accountDao: AccountsDao by lazy { AccountsDao::class.Bean }
    protected fun getClient(id: Int): ClientEntity {
        val client = clientDao.getClient(id)
            ?: throw FailedResult.Client.NotFound
        if (client.type != type) {
            throw FailedResult.Client.TypeNotMatch
        }
        return client
    }
    protected fun getAccount(id: UUID): AccountEntity {
        val account = accountDao.getAccount(id)
            ?: throw FailedResult.Account.NotFound
        getClient(account.parentClientId)
        return account
    }
}

enum class ClientType(
    private val target: KClass<out ClientAction>
): ClientAction {
    OneDrive(OneDriveAction::class);

    private val action: ClientAction by lazy { target.Bean }
    override val type: ClientType get() = action.type

    override fun loginUri(clientId: UUID, redirectUri: String): AccountCreateRespDto {
        return action.loginUri(clientId, redirectUri)
    }

    override fun loginRequest(params: ObjectNode) {
        action.loginRequest(params)
    }

    override fun create(name: String, params: ObjectNode) {
        action.create(name, params)
    }

    override fun edit(params: ObjectNode, clientId: UUID) {
        action.edit(params, clientId)
    }

    override fun listFile(path: CanonicalPath, accountId: UUID): ArrayNode {
        return action.listFile(path, accountId)
    }
    override fun downloadFile(path: CanonicalPath, accountId: UUID): String {
        return action.downloadFile(path, accountId)
    }

    override fun needDelta(accountId: UUID): Boolean {
        return action.needDelta(accountId)
    }
    override fun delta(accountId: UUID) {
        action.delta(accountId)
    }

    @PostConstruct
    override fun onConstruct() {
        action.onConstruct()
    }
}
