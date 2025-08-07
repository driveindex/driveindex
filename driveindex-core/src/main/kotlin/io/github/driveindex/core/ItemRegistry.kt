package io.github.driveindex.core

abstract class ItemRegistry<KT, VT>(
    private val items: Map<KT, VT>
) {
    operator fun get(clientType: KT): VT? {
        return items[clientType]
    }

    operator fun iterator(): Iterator<Map.Entry<KT, VT>> {
        return items.iterator()
    }
}