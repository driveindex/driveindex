package io.github.driveindex.database.entity.file.attributes

import io.github.driveindex.core.client.ClientType
import io.github.driveindex.dto.resp.file.FileDetail
import kotlinx.serialization.Serializable

@Serializable
sealed interface FileAttribute

@Serializable
sealed interface RemoteFileAttribute: FileAttribute {
    val type: ClientType
    val accountId: Int

    fun toRespDtoDetail(): FileDetail
}

@Serializable
sealed interface LocalFileAttribute: FileAttribute
