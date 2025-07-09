package io.github.driveindex.core.util

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.ser.std.UUIDSerializer
import io.github.driveindex.Application.Companion.Bean
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import java.util.UUID

val JsonKt = Json {
    prettyPrint = false
    classDiscriminator = "attr_type"
}

object Json {
    private val mapper: ObjectMapper by lazy {
        ObjectMapper::class.Bean
    }

    fun readTree(json: String): JsonNode {
        return mapper.readTree(json)
    }

    fun <T: Any> readTreeAsString(json: String, clazz: Class<T>): T {
        return treeToValue(readTree(json), clazz)
    }
    inline fun <reified T: Any> readTreeAsString(json: String): T {
        return readTreeAsString(json, T::class.java)
    }

    fun <T: Any> treeToValue(node: JsonNode, clazz: Class<T>): T {
        return mapper.treeToValue(node, clazz)
    }
    inline fun <reified T: Any> treeToValue(node: JsonNode): T {
        return treeToValue(node, T::class.java)
    }

    fun <T: Any> writeValueAsString(value: T): String {
        return mapper.writeValueAsString(value)
    }

    fun <T: Any> valueToTree(value: T, clazz: Class<T>): ObjectNode {
        return mapper.valueToTree(value)
    }
    inline fun <reified T: Any> valueToTree(value: T): ObjectNode {
        return valueToTree(value, T::class.java)
    }

    fun <T: Any> newObjectNode(vararg pairs: Pair<String, T>): ObjectNode {
        return valueToTree(pairs.toMap())
    }
}

typealias UuidKt = @Serializable(with = UuidAsStringSerializer::class) UUID
object UuidAsStringSerializer : KSerializer<UUID> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(UUID::class.java.packageName, PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: UUID) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): UUID {
        val string = decoder.decodeString()
        return UUID.fromString(string)
    }
}
