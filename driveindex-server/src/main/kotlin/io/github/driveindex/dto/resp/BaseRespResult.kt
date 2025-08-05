package io.github.driveindex.dto.resp

import com.fasterxml.jackson.databind.node.ObjectNode
import io.github.driveindex.core.util.Json
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
    override val code: Int = 200,
    override val message: String = "success.",
    override val params: ObjectNode? = null,
): BaseRespResult<Unit>

data class ObjRespResult<T: Any>(
    override val code: Int = 200,
    override val message: String = "success.",
    override val params: ObjectNode? = null,
    override val data: T?
): BaseRespResult<T>

data class ListRespResult<T: Any>(
    override val code: Int = 200,
    override val message: String = "success.",
    override val params: ObjectNode? = null,
    override val data: Collection<T>?
): BaseRespResult<Collection<T>>

fun Resp(
    catch: (e: Exception) -> Exception = { it },
    block: () -> Unit,
): SampleRespResult {
    try {
        block()
        return SampleRespResult()
    } catch (e: Exception) {
        throw catch(e)
    }
}

fun <T: Any> ListResp(
    catch: (e: Exception) -> Exception = { it },
    block: () -> Collection<T>,
): ListRespResult<T> {
    try {
        val list = block()
        return ListRespResult(data = list)
    } catch (e: Exception) {
        throw catch(e)
    }
}

fun <T: Any> ObjResp(
    catch: (e: Exception) -> Exception = { it },
    block: () -> T?,
): ObjRespResult<T> {
    try {
        val list = block()
        return ObjRespResult(data = list)
    } catch (e: Exception) {
        throw catch(e)
    }
}

inline fun <reified T: Any> HttpServletResponse.write(result: T) {
    characterEncoding = StandardCharsets.UTF_8.name()
    addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
    writer.use {
        it.write(Json.writeValueAsString(ObjRespResult(data = result)))
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

