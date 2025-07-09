package io.github.driveindex.configuration

import feign.Contract
import feign.Feign
import feign.Logger
import feign.codec.Decoder
import feign.codec.Encoder
import io.github.driveindex.Application.Companion.Bean
import io.github.driveindex.Application.Companion.Config
import io.github.driveindex.core.ConfigDto
import org.springframework.cloud.openfeign.FeignClientsConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Import(FeignClientsConfiguration::class)
@Configuration
class FeignClientConfig(
    private val encoder: Encoder,
    private val decoder: Decoder,
    private val contract: Contract,
) {
    @Bean
    fun feignBuilder(): Feign.Builder {
        return Feign.builder()
            .encoder(encoder)
            .decoder(decoder)
            .contract(contract)
            .logLevel(feignLoggerLevel())
    }

    private fun feignLoggerLevel(): Logger.Level {
        return if (Config.system.debug) {
            Logger.Level.FULL
        } else {
            Logger.Level.BASIC
        }
    }

    companion object {
        fun newFeignBuilder(): Feign.Builder {
            return FeignClientConfig::class.Bean
                .feignBuilder()
        }
    }
}
