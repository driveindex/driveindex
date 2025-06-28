package io.github.driveindex.database.entity

import com.fasterxml.jackson.databind.node.ObjectNode
import io.github.driveindex.core.util.Json

interface IdEntity {
    val id: Int
}

interface AttributeEntity {
    val attribute: ObjectNode
}
inline fun <reified T: Any> AttributeEntity.writeAttribute(obj: T) {
    attribute.setAll<ObjectNode>(Json.valueToTree(obj))
}
inline fun <reified T: Any> AttributeEntity.readAttribute(): T {
    return Json.treeToValue<T>(attribute)
}
inline fun <EntT: AttributeEntity, reified AttT: Any> EntT.withAttribute(crossinline block: (EntT, AttT) -> Unit) {
    block(this, readAttribute())
}

interface TimeEntity {
    val createAt: Long
    val createBy: Int
    val modifyAt: Long
    val modifyBy: Int
}
