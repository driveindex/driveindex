package io.github.driveindex.dto.resp

import io.github.driveindex.client.ClientType
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * @author sgpublic
 * @Date 2023/4/10 下午3:14
 */
data class FileListRespDto(
        @JsonProperty("content_size")
    val contentSize: Int,
        @JsonProperty("content")
    val content: List<Item<*>>,
): RespResultData {
    data class Item<T: Item.Detail>(
        @JsonProperty("name")
        val name: String,
        @JsonProperty("create_at")
        val createAt: Long,
        @JsonProperty("modify_at")
        val modifyAt: Long,
        @JsonProperty("is_dir")
        val isDir: Boolean,
        @JsonProperty("is_link")
        val isLink: Boolean,
        @JsonProperty("type")
        val type: ClientType?,
        @JsonProperty("detail")
        val detail: T,
    ) {
        sealed interface Detail

        data class OneDrive(
            @JsonProperty("web_url")
            val webUrl: String,
            @JsonProperty("mime_type")
            val mimeType: String,
            @JsonProperty("quick_xor_hash")
            val quickXorHash: String? = null,
            @JsonProperty("sha1_hash")
            val sha1Hash: String? = null,
            @JsonProperty("sha256_hash")
            val sha256Hash: String? = null,
        ): Detail
    }
}

