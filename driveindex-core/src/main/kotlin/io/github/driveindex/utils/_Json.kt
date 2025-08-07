package io.github.driveindex.utils

import io.github.driveindex.drivers.DriverRegistry
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
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*
import kotlin.reflect.KClass

private lateinit var json: Json
val JsonKt: Json get() = json

@Configuration
class JsonKtConfiguration(
    private val clientLoader: DriverRegistry
) {
    @Bean
    fun json(): Json {
        val jsonKy = Json {
            encodeDefaults = true
            prettyPrint = false
            explicitNulls = true
            ignoreUnknownKeys = true
            classDiscriminator = "@type"

            serializersModule += SerializersModule {
                for ((_, service) in clientLoader) {
                    service.registerAttrJsonModule(this)
                }
            }
        }
        json = jsonKy
        return jsonKy
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
