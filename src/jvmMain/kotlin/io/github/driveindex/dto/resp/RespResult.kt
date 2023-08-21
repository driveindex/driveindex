package io.github.driveindex.dto.resp

import jakarta.servlet.http.HttpServletResponse
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import io.github.driveindex.core.util.JsonGlobal
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import java.nio.charset.StandardCharsets

/**
 * @author sgpublic
 * @Date 2023/2/7 16:07
 */
@Serializable
open class RespResult<T: @Serializable Any> internal constructor(
    val code: Int = 200,
    val message: String = "success.",
    val data: T? = null
)

object SampleResult: RespResult<Unit>()

fun <T: @Serializable Any> T.resp(): RespResult<T> {
    return RespResult(data = this)
}

fun <T: @Serializable Any> HttpServletResponse.write(serializer: KSerializer<T>, result: T) {
    characterEncoding = StandardCharsets.UTF_8.name()
    addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
    writer.use {
        it.write(JsonGlobal.encodeToString(
            RespResult.serializer(serializer), result.resp()
        ))
        it.flush()
    }
}