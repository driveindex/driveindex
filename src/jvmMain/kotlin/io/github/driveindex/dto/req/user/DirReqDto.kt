package io.github.driveindex.dto.req.user

import io.github.driveindex.core.util.CanonicalPath
import io.github.driveindex.core.util.KUUID
import kotlinx.serialization.Serializable
import java.util.*

/**
 * @author sgpublic
 * @Date 2023/3/30 下午3:11
 */

@Serializable
data class CreateDirReqDto(
    val name: String,
    val parent: CanonicalPath,
)

@Serializable
data class CreateLinkReqDto(
    val name: String?,
    val target: KUUID,
    val parent: CanonicalPath,
)

enum class GetDirReqSort {
    NAME, SIZE, CREATE_TIME, MODIFIED_TIME;

    override fun toString(): String {
        return super.name.lowercase(Locale.getDefault())
    }
}

data class DeleteDirReqDto(
    val path: CanonicalPath,
)

data class RenameDirReqDto(
    val path: CanonicalPath,
    val name: String,
)
