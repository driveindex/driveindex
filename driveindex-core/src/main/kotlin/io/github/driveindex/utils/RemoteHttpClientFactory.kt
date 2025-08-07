package io.github.driveindex.utils

import feign.Contract
import feign.Feign
import feign.Logger
import feign.codec.Decoder
import feign.codec.Encoder

class RemoteHttpClientFactory(
    private val encoder: Encoder,
    private val decoder: Decoder,
    private val contract: Contract,
    private val debug: Boolean
) {
    fun feignBuilder(): Feign.Builder {
        return Feign.builder()
            .encoder(encoder)
            .decoder(decoder)
            .contract(contract)
            .logLevel(feignLoggerLevel())
    }

    private fun feignLoggerLevel(): Logger.Level {
        return if (debug) {
            Logger.Level.FULL
        } else {
            Logger.Level.BASIC
        }
    }
}
