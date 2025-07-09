package io.github.driveindex.database.entity.file.attributes

import io.github.driveindex.client.ClientType
import kotlinx.serialization.Serializable

@Serializable
sealed interface FileAttribute

@Serializable
sealed interface RemoteFileAttribute: FileAttribute {
    val type: ClientType
    val accountId: Int
}

@Serializable
sealed interface LocalFileAttribute: FileAttribute
