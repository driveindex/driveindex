package io.github.driveindex.client

import io.github.driveindex.Application.Companion.Bean
import io.github.driveindex.client.onedrive.OneDriveAction
import io.github.driveindex.core.util.CanonicalPath
import io.github.driveindex.core.util.KUUID
import io.github.driveindex.database.dao.AccountsDao
import io.github.driveindex.database.dao.ClientsDao
import io.github.driveindex.database.dao.FileDao
import io.github.driveindex.database.entity.AccountsEntity
import io.github.driveindex.database.entity.ClientsEntity
import io.github.driveindex.dto.resp.RespResult
import io.github.driveindex.exception.FailedResult
import io.github.driveindex.module.Current
import jakarta.annotation.PostConstruct
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import java.util.*
import kotlin.reflect.KClass

interface ClientAction {
    fun loginUri(clientId: KUUID, redirectUri: String): RespResult<String>
    fun loginRequest(params: JsonObject): RespResult<Unit>

    val type: ClientType
    fun onConstruct() { }

    fun create(name: String, params: JsonObject)
    fun edit(params: JsonObject, clientId: KUUID)


    fun listFile(path: CanonicalPath, accountId: UUID): RespResult<JsonArray>
    fun downloadFile(path: CanonicalPath, accountId: UUID): RespResult<String>

    fun needDelta(accountId: KUUID): Boolean
    fun delta(accountId: KUUID)
}

abstract class AbsClientAction(
    final override val type: ClientType
): ClientAction {
    protected val current: Current by lazy { Current::class.Bean }
    protected val fileDao: FileDao by lazy { FileDao::class.Bean }
    protected val clientDao: ClientsDao by lazy { ClientsDao::class.Bean }
    protected val accountDao: AccountsDao by lazy { AccountsDao::class.Bean }
    protected fun getClient(id: KUUID): ClientsEntity {
        val client = clientDao.getClient(id)
            ?: throw FailedResult.Client.NotFound
        if (client.type != type) {
            throw FailedResult.Client.TypeNotMatch
        }
        return client
    }
    protected fun getAccount(id: KUUID): AccountsEntity {
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

    override fun loginUri(clientId: KUUID, redirectUri: String): RespResult<String> {
        return action.loginUri(clientId, redirectUri)
    }

    override fun loginRequest(params: JsonObject): RespResult<Unit> {
        return action.loginRequest(params)
    }

    override fun create(name: String, params: JsonObject) {
        action.create(name, params)
    }

    override fun edit(params: JsonObject, clientId: KUUID) {
        action.edit(params, clientId)
    }

    override fun listFile(path: CanonicalPath, accountId: UUID): RespResult<JsonArray> {
        return action.listFile(path, accountId)
    }
    override fun downloadFile(path: CanonicalPath, accountId: UUID): RespResult<String> {
        return action.downloadFile(path, accountId)
    }

    override fun needDelta(accountId: KUUID): Boolean {
        return action.needDelta(accountId)
    }
    override fun delta(accountId: KUUID) {
        action.delta(accountId)
    }

    @PostConstruct
    override fun onConstruct() {
        action.onConstruct()
    }
}
