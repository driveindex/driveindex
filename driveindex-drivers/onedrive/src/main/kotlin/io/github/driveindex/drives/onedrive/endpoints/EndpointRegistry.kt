package io.github.driveindex.drives.onedrive.endpoints

import io.github.driveindex.core.ItemRegistry
import io.github.driveindex.drives.onedrive.core.OneDriveEndpoint
import org.springframework.beans.factory.getBeansOfType
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

class EndpointRegistry(
    clients: Map<OneDriveEndpoint, BaseEndpoint>
): ItemRegistry<OneDriveEndpoint, BaseEndpoint>(clients)

@Configuration
class EndpointRegistryFactory(
    private val applicationContext: ApplicationContext
) {
    @Bean
    fun endpointRegistry(): EndpointRegistry {
        val clients = applicationContext.getBeansOfType<BaseEndpoint>()
        val clientRegistry = HashMap<OneDriveEndpoint, BaseEndpoint>()
        for ((_, client) in clients) {
            clientRegistry[client.endpoint] = client
        }
        return EndpointRegistry(clientRegistry)
    }
}