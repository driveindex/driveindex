package io.github.driveindex.dto.req.user

import io.github.driveindex.core.util.CanonicalPath
import java.util.UUID
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

/**
 * @author sgpublic
 * @Date 2023/3/30 下午3:11
 */

data class CreateDirReqDto(
    @JsonProperty("name")
    val name: String,
    @JsonProperty("parent")
    val parent: CanonicalPath,
)

data class CreateLinkReqDto(
    @JsonProperty("name")
    val name: String?,
    @JsonProperty("target")
    val target: UUID,
    @JsonProperty("parent")
    val parent: CanonicalPath,
)

enum class GetDirReqSort {
    NAME, SIZE, CREATE_TIME, MODIFIED_TIME;

    override fun toString(): String {
        return super.name.lowercase(Locale.getDefault())
    }
}

data class DeleteDirReqDto(
    @JsonProperty("path")
    val path: CanonicalPath,
)

data class RenameDirReqDto(
    @JsonProperty("path")
    val path: CanonicalPath,
    @JsonProperty("name")
    val name: String,
)
