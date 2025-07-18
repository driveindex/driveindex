package io.github.driveindex.database.entity.file.attributes

import io.github.driveindex.core.util.UuidKt
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
@Serializable
@SerialName("local_mount")
data class LocalMountAttribute(
    @SerialName("mount_target")
    val mountTarget: UuidKt
): LocalFileAttribute