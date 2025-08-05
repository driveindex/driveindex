package io.github.driveindex.core.client

import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import io.github.driveindex.Application.Companion.Bean
import io.github.driveindex.core.client.onedrive.OneDriveAction
import io.github.driveindex.utils.CanonicalPath
import io.github.driveindex.dto.resp.AccountCreateRespDto
import jakarta.annotation.PostConstruct
import kotlin.reflect.KClass

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
