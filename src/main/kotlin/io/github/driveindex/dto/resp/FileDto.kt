package io.github.driveindex.dto.resp

import io.github.driveindex.core.client.ClientType
import io.github.driveindex.dto.resp.file.FileDetail

/**
 * @author sgpublic
 * @Date 2023/4/10 下午3:14
 */
data class FileListRespDto(
    val contentSize: Int,
    val content: List<Item>,
) {
    data class Item(
        val name: String,
        val createAt: Long,
        val modifyAt: Long,
        val isDir: Boolean,
        val isLink: Boolean,
        val type: ClientType?,
        val detail: FileDetail,
    )
}

