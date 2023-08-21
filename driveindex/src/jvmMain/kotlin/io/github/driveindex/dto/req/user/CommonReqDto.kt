package io.github.driveindex.dto.req.user

import io.github.driveindex.core.util.KUUID
import kotlinx.serialization.Serializable


@Serializable
data class CommonSettingsReqDto(
    val nick: String? = null,
    val corsOrigin: String? = null,
)

@Serializable
data class AccountDeleteReqDto(
    val accountId: KUUID
)

@Serializable
data class ClientListReqDto(
    val clientId: KUUID
)
