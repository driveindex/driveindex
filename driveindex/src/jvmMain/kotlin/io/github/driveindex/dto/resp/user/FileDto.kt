package io.github.driveindex.dto.resp.user

import io.github.driveindex.client.ClientType
import kotlinx.serialization.Serializable

/**
 * @author sgpublic
 * @Date 2023/4/10 下午3:14
 */
@Serializable
data class FileListRespDto(
    val contentSize: Int,
    val content: List<Item<*>>,
) {
    @Serializable
    data class Item<T: Item.Detail>(
        val name: String,
        val createAt: Long,
        val modifyAt: Long,
        val isDir: Boolean,
        val isLink: Boolean,
        val type: ClientType?,
        val detail: T,
    ) {
        @Serializable
        sealed interface Detail

        @Serializable
        data class OneDrive(
            val webUrl: String,
            val mimeType: String,
            val quickXorHash: String? = null,
            val sha1Hash: String? = null,
            val sha256Hash: String? = null,
        ): Detail
    }
}

