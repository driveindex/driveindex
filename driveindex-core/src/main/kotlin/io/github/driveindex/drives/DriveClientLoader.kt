package io.github.driveindex.drives

import io.github.driveindex.drives.api.DriveClient
import java.util.ServiceLoader

object DriveClientLoader: Map<String, DriveClient> {
    private val clients by lazy {
        val list = LinkedHashMap<String, DriveClient>()
        for (service in ServiceLoader.load(DriveClient::class.java)) {
            list[service.name] = service
            service.create()
        }
        return@lazy list
    }

    override val size: Int get() = clients.size
    override val keys: Set<String> get() = clients.keys
    override val values: Collection<DriveClient> get() = clients.values
    override val entries: Set<Map.Entry<String, DriveClient>> get() = clients.entries
    override fun isEmpty() = clients.isEmpty()
    override fun containsKey(key: String) = clients.containsKey(key)
    override fun containsValue(value: DriveClient) = clients.containsValue(value)
    override fun get(key: String): DriveClient? = clients[key]
}
