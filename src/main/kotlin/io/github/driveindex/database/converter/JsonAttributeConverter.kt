package io.github.driveindex.database.converter

import com.fasterxml.jackson.databind.node.ObjectNode
import io.github.driveindex.core.util.Json
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter(autoApply = true)
class JsonAttributeConverter: AttributeConverter<ObjectNode, String> {
    override fun convertToDatabaseColumn(attribute: ObjectNode): String = Json.writeValueAsString(attribute)

    override fun convertToEntityAttribute(dbData: String): ObjectNode = Json.readTreeAsString(dbData)
}