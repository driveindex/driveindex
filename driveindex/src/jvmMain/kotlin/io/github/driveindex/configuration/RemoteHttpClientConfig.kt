package io.github.driveindex.configuration

import feign.Contract
import feign.Feign
import feign.Logger
import feign.codec.Encoder
import feign.slf4j.Slf4jLogger
import io.github.driveindex.Application
import io.github.driveindex.core.ConfigManager
import io.github.driveindex.feigh.onedrive.AzureErrorDecoder
import kotlinx.serialization.json.Json
import org.springframework.boot.autoconfigure.http.HttpMessageConverters
import org.springframework.cloud.openfeign.FeignClientsConfiguration
import org.springframework.cloud.openfeign.support.SpringDecoder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.http.converter.json.KotlinSerializationJsonHttpMessageConverter

@Import(FeignClientsConfiguration::class, AzureErrorDecoder::class)
@Configuration
class FeignClientConfig(
    private val encoder: Encoder,
//    private val decoder: Decoder,
    private val contract: Contract,
    private val errorDecoder: AzureErrorDecoder,
) {
    @Bean
    fun feignBuilder(): Feign.Builder {
        return Feign.builder()
            .encoder(encoder)
            .decoder(SpringDecoder {
                HttpMessageConverters(true, listOf(
                    KotlinSerializationJsonHttpMessageConverter(Json {
                        ignoreUnknownKeys = true
                        useAlternativeNames = true
                    })
                ))
            })
            .contract(contract)
            .errorDecoder(errorDecoder)
            .logLevel(feignLoggerLevel())
    }

    private fun feignLoggerLevel(): Logger.Level {
        return if (ConfigManager.Debug) {
            Logger.Level.FULL
        } else {
            Logger.Level.BASIC
        }
    }
}

inline fun <reified T> lazyFeignClientOf(
    url: String,
    clazz: Class<T> = T::class.java,
    crossinline block: Feign.Builder.() -> Unit = { }
): Lazy<T> {
    return lazy {
        Application.getBean<Feign.Builder>()
            .logger(Slf4jLogger(clazz))
            .apply(block)
            .target(clazz, url)
    }
}