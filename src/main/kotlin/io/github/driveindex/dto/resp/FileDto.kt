package io.github.driveindex.dto.resp

import io.github.driveindex.client.ClientType
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * @author sgpublic
 * @Date 2023/4/10 下午3:14
 */
data class FileListRespDto(
    val contentSize: Int,
    val content: List<Item<*>>,
): RespResultData {
    data class Item<T: Item.Detail>(
        val name: String,
        val createAt: Long,
        val modifyAt: Long,
        val isDir: Boolean,
        val isLink: Boolean,
        val type: ClientType?,
        val detail: T,
    ) {
        sealed interface Detail

        data class OneDrive(
            val webUrl: String,
            val mimeType: String,
            val quickXorHash: String? = null,
            val sha1Hash: String? = null,
            val sha256Hash: String? = null,
        ): Detail
    }
}

