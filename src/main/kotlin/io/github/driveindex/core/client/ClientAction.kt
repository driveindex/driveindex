package io.github.driveindex.core.client

import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import io.github.driveindex.Application.Companion.Bean
import io.github.driveindex.core.client.onedrive.OneDriveAction
import io.github.driveindex.core.util.CanonicalPath
import io.github.driveindex.database.dao.findById
import io.github.driveindex.database.dao.getClient
import io.github.driveindex.database.entity.account.AccountEntity
import io.github.driveindex.database.entity.client.ClientEntity
import io.github.driveindex.dto.resp.AccountCreateRespDto
import io.github.driveindex.core.exception.FailedResult
import io.github.driveindex.module.Current
import jakarta.annotation.PostConstruct
import org.jetbrains.exposed.v1.core.ResultRow
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
    protected fun getClient(id: Int): ResultRow {
        val client = ClientEntity.getClient(id, type)
            ?: throw FailedResult.Client.NotFound
        if (client[ClientEntity.type] != type) {
            throw FailedResult.Client.TypeNotMatch
        }
        return client
    }
    protected fun getAccount(id: Int, block: AccountEntity.(entity: ResultRow) -> Unit) {
        val account = AccountEntity.findById(id)
            ?: throw FailedResult.Account.NotFound
        getClient(account[AccountEntity.clientId])
        AccountEntity.block(account)
    }
}

enum class ClientType(
    private val target: KClass<out ClientAction>
): ClientAction {
    OneDrive(OneDriveAction::class);

    private val action: ClientAction by lazy { target.Bean }
    override val type: ClientType get() = action.type

    override fun loginUri(clientId: Int, redirectUri: String): AccountCreateRespDto {
        return action.loginUri(clientId, redirectUri)
    }

    override fun loginRequest(params: ObjectNode) {
        action.loginRequest(params)
    }

    override fun create(name: String, params: ObjectNode) {
        action.create(name, params)
    }

    override fun edit(params: ObjectNode, clientId: Int) {
        action.edit(params, clientId)
    }

    override fun listFile(path: CanonicalPath, accountId: Int): ArrayNode {
        return action.listFile(path, accountId)
    }
    override fun downloadFile(path: CanonicalPath, accountId: Int): String {
        return action.downloadFile(path, accountId)
    }

    override fun needDelta(accountId: Int): Boolean {
        return action.needDelta(accountId)
    }
    override fun delta(accountId: Int) {
        action.delta(accountId)
    }

    @PostConstruct
    override fun onConstruct() {
        action.onConstruct()
    }
}
