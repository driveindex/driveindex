package io.github.driveindex.core.configuration

import feign.Contract
import feign.codec.Decoder
import feign.codec.Encoder
import io.github.driveindex.Application
import io.github.driveindex.core.DriveIndexConfig
import io.github.driveindex.utils.RemoteHttpClientFactory
import org.springframework.cloud.openfeign.FeignClientsConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(FeignClientsConfiguration::class)
class RemoteHttpClientFactoryConfiguration(
    private val encoder: Encoder,
    private val decoder: Decoder,
    private val contract: Contract,
    private val config: DriveIndexConfig,
) {
    @Bean
    fun remoteHttpClientFactory(): RemoteHttpClientFactory {
        return RemoteHttpClientFactory(
            encoder, decoder, contract, config.system.debug
        )
    }
}