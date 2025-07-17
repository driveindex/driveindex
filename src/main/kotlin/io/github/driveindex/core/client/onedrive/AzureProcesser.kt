package io.github.driveindex.core.client.onedrive

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.TextNode
import feign.Response
import feign.codec.DecodeException
import feign.codec.ErrorDecoder
import io.github.driveindex.core.util.Json
import io.github.driveindex.core.util.log
import io.github.driveindex.dto.feign.AzureFailedResultDtoA
import io.github.driveindex.dto.feign.AzureFailedResultDtoB
import io.github.driveindex.core.exception.AzureDecodeException
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets


/**
 * @author sgpublic
 * @Date 2022/8/14 19:18
 */
@Component
class AzureErrorDecoder: ErrorDecoder {
    private val defaultDecoder: ErrorDecoder.Default = ErrorDecoder.Default()
    override fun decode(s: String, response: Response): Exception {
        try {
            val e = createException(response)
//            log.debug("拦截 AzureDecodeException(code: ${e.code})", e)
//            if (e.code != "InvalidAuthenticationToken") {
                return e
//            }
//            log.debug("拦截 InvalidAuthenticationToken", e)
//            val req = response.request()
//            return RetryableException(
//                e.status(), e.message, req.httpMethod(), e,
//                Date(System.currentTimeMillis() + 100), req
//            )
        } catch (e: RuntimeException) {
            log.warn("AzureDecodeException 创建失败", e)
            return defaultDecoder.decode(s, response)
        } catch (e: Exception) {
            return DecodeException(response.status(), e.message, response.request(), e)
        }
    }

    private fun createException(response: Response): AzureDecodeException {
        response.body().asReader(StandardCharsets.UTF_8).use { input ->
            val json: String = with(StringBuilder()) {
                val buffer = CharArray(1024)
                var length: Int
                while (input.read(buffer).also { length = it } > 0) {
                    appendRange(buffer, 0, length)
                }
                return@with toString()
            }
            val dto: JsonNode = Json.readTree(json)
            val errorA = dto["error"]
            if (errorA != null && errorA is TextNode) {
                val resultDto: AzureFailedResultDtoA = Json.readTreeAsString(json)
                return AzureDecodeException(
                    response.status(), resultDto.error,
                    resultDto.errorDescription, response.request()
                )
            } else {
                val errorB: AzureFailedResultDtoB = Json.treeToValue(errorA)
                return AzureDecodeException(
                    response.status(), errorB.code,
                    errorB.message, response.request()
                )
            }
        }
    }
}
