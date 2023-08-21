package io.github.driveindex.core.util

import io.github.driveindex.exception.FailedResult
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*
import org.springframework.boot.autoconfigure.http.HttpMessageConverters
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.KotlinSerializationJsonHttpMessageConverter
import java.util.*

@OptIn(ExperimentalSerializationApi::class)
val JsonGlobal = Json {
    namingStrategy = JsonNamingStrategy.SnakeCase
    encodeDefaults = true
    explicitNulls = false
    useAlternativeNames = true
    ignoreUnknownKeys = true
}

fun jsonObjectOf(vararg contents: Pair<String, Any?>): JsonObject {
    return buildJsonObject {
        for ((key, value) in contents) {
            put(key, when (value) {
                is String -> JsonPrimitive(value)
                is Boolean -> JsonPrimitive(value)
                is Int -> JsonPrimitive(value)
                is Number -> JsonPrimitive(value)
                is KUUID -> JsonPrimitive(value.toString())
                else -> throw IllegalArgumentException("不支持的类型")
            })
        }
    }
}

@Configuration
class GsonConfig {
    @Bean
    fun customConverters(): HttpMessageConverters {
        return HttpMessageConverters(true, listOf(
            KotlinSerializationJsonHttpMessageConverter(JsonGlobal)
        ))
    }
}

object UuidAdapter: KSerializer<UUID> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): UUID {
        return try {
            UUID.fromString(decoder.decodeString())
        } catch (e: Exception) {
            throw FailedResult.BadArgument
        }
    }

    override fun serialize(encoder: Encoder, value: UUID) {
        encoder.encodeString(value.toString())
    }
}

typealias KUUID = @Serializable(UuidAdapter::class) UUID
