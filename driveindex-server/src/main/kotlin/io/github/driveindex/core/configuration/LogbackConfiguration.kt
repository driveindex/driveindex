package io.github.driveindex.core.configuration

import io.github.driveindex.core.DriveIndexConfig
import org.springframework.stereotype.Component

@Component
class LogbackConfiguration(
    private val config: DriveIndexConfig
) {
    fun logPath() = config.system.logDir

    fun debug() = config.system.debug
    fun trace() = config.system.trace
}