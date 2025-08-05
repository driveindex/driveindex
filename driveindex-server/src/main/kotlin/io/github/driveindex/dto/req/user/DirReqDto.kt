package io.github.driveindex.dto.req.user

import java.util.UUID
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

/**
 * @author sgpublic
 * @Date 2023/3/30 下午3:11
 */

data class CreateDirReqDto(
    @param:Schema(description = "新目录名称")
    val name: String,

    @param:Schema(description = "父文件夹 ID")
    val parentId: UUID,

    @param:Schema(description = "挂载目标 ID，创建挂载点时传递")
    val mountTo: UUID? = null,
)

data class ListDirReqDto(
    @param:Schema(description = "目标文件夹路径")
    val path: String,

    @param:Schema(description = "排序规则", allowableValues = ["name", "create_time", "modified_time"], defaultValue = "name")
    val sortBy: GetDirReqSort = GetDirReqSort.NAME,

    @param:Schema(description = "是否升序", defaultValue = "true")
    val asc: Boolean = true,

    @param:Schema(description = "分页大小", defaultValue = "20")
    val pageSize: Int = 20,

    @param:Schema(description = "页索引", defaultValue = "0")
    val pageIndex: Int = 0,
)

enum class GetDirReqSort {
    NAME, CREATE_TIME, MODIFIED_TIME;

    override fun toString(): String {
        return super.name.lowercase(Locale.getDefault())
    }
}

data class DeleteDirReqDto(
    @param:JsonProperty("path")
    val path: UUID,
)

data class RenameDirReqDto(
    @param:JsonProperty("path")
    val path: UUID,
    @param:JsonProperty("name")
    val name: String,
)
