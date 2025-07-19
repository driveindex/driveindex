package io.github.driveindex.dto.resp

import io.github.driveindex.dto.resp.file.FileDetail
import java.util.LinkedList
import java.util.UUID

/**
 * @author sgpublic
 * @Date 2023/4/10 下午3:14
 */
data class FileListRespDto(
    val contentSize: Long,
    val content: LinkedList<FileItem>,
) {
    data class FileItem(
        val id: UUID,
        val name: String,
        val createAt: Long,
        val modifyAt: Long,
        val isDir: Boolean,
        val isMount: Boolean,
        val isRemote: Boolean,
        val detail: FileDetail?,
    )
}

