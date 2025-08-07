package io.github.driveindex.drivers

import io.github.driveindex.core.ItemRegistry
import io.github.driveindex.drivers.api.ClientType
import io.github.driveindex.drivers.api.NetDiskContext
import io.github.driveindex.utils.log
import org.springframework.beans.factory.getBeansOfType
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import kotlin.collections.iterator

class DriverRegistry(
    clients: Map<ClientType, NetDiskContext>
): ItemRegistry<ClientType, NetDiskContext>(clients)

@Configuration
class DriverRegistryFactory(
    private val applicationContext: ApplicationContext
) {
    @Bean
    fun driveRegistry(): DriverRegistry {
        val clients = applicationContext.getBeansOfType<NetDiskContext>()
        val clientRegistry = HashMap<ClientType, NetDiskContext>()
        for ((_, client) in clients) {
            val anno = client.javaClass.annotations
                .find { annotation -> annotation is io.github.driveindex.annotations.DriveClient }
                ?: continue
            anno as io.github.driveindex.annotations.DriveClient
            clientRegistry[anno.type] = client

            try {
                client.create()
            } catch (e: Exception) {
                log.warn("Error creating drive client for type ${anno.type}: ${client.javaClass}", e)
            }
        }
        return DriverRegistry(clientRegistry)
    }
}
