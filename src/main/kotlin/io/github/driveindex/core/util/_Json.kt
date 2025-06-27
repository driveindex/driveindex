package io.github.driveindex.core.util

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import io.github.driveindex.Application

object Json {
    private val mapper: ObjectMapper by lazy {
        Application.Context.getBean(ObjectMapper::class.java)
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

    fun objectNodeOf(vararg pairs: Pair<String, Any>): ObjectNode {
        return mapper.valueToTree(pairs.toMap())
    }
}
