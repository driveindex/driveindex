package io.github.driveindex.dto.resp

import com.fasterxml.jackson.databind.node.ObjectNode
import io.github.driveindex.core.util.Json
import jakarta.annotation.Nonnull
import jakarta.annotation.Nullable
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.server.ServerHttpResponse
import java.nio.charset.StandardCharsets

/**
 * @author sgpublic
 * @Date 2023/2/7 16:07
 */
sealed interface BaseRespResult<T: Any> {
    val code: Int
    val message: String
    val params: ObjectNode? get() = null
    val data: T? get() = null
}

data class SampleRespResult(
    @Nonnull
    override val code: Int = 200,
    @Nonnull
    override val message: String = "success.",
    @Nullable
    override val params: ObjectNode? = null,
): BaseRespResult<Unit>

data class DataRespResult<T: RespResultData>(
    @Nonnull
    override val code: Int = 200,
    @Nonnull
    override val message: String = "success.",
    @Nullable
    override val params: ObjectNode? = null,
    @Nullable
    override val data: T?
): BaseRespResult<T>

data class ListRespResult<T: Any>(
    @Nonnull
    override val code: Int = 200,
    @Nonnull
    override val message: String = "success.",
    @Nullable
    override val params: ObjectNode? = null,
    @Nullable
    override val data: Collection<T>?
): BaseRespResult<Collection<T>>

fun Resp(block: () -> Unit): SampleRespResult {
    block()
    return SampleRespResult()
}

fun <T: Any> ListResp(block: () -> Collection<T>): ListRespResult<T> {
    val list = block()
    return ListRespResult(data = list)
}

fun <T: RespResultData> DataResp(block: () -> T?): DataRespResult<T> {
    val list = block()
    return DataRespResult(data = list)
}

sealed interface RespResultData

inline fun <reified T: RespResultData> HttpServletResponse.write(result: T) {
    characterEncoding = StandardCharsets.UTF_8.name()
    addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
    writer.use {
        it.write(Json.writeValueAsString(DataRespResult(data = result)))
        it.flush()
    }
}

fun ServerHttpResponse.writeJson(json: String) {
    headers[HttpHeaders.CONTENT_TYPE] = MediaType.APPLICATION_JSON_VALUE
    body.writer().use {
        it.write(json)
        it.flush()
    }
}

