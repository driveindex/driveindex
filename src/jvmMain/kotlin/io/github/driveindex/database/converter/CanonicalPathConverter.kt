package io.github.driveindex.database.converter

import io.github.driveindex.core.util.CanonicalPath
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter(autoApply = true)
class CanonicalPathConverter: AttributeConverter<CanonicalPath, String> {
    override fun convertToDatabaseColumn(attribute: CanonicalPath): String = attribute.path

    override fun convertToEntityAttribute(dbData: String): CanonicalPath = CanonicalPath.of(dbData)
}