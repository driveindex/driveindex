package io.github.driveindex.utils

import io.github.driveindex.drives.DriveClientLoader
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.SerializersModuleBuilder
import kotlinx.serialization.modules.plus
import java.util.*
import kotlin.reflect.KClass

val JsonKt: Json by lazy {
    Json {
        encodeDefaults = true
        prettyPrint = false
        explicitNulls = true
        ignoreUnknownKeys = true
        classDiscriminator = "@type"

        serializersModule += SerializersModule {
            for ((name, service) in DriveClientLoader) {
                service.registerAttrJsonModule(this)
            }
        }
    }
}

inline fun <B: Any, reified T: B> SerializersModuleBuilder.polymorphic(baseClass: KClass<B>, serializer: KSerializer<T>) {
    polymorphic(baseClass, T::class, serializer)
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
