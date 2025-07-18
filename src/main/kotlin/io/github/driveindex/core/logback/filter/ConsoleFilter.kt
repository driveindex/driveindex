package io.github.driveindex.core.logback.filter

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.filter.Filter
import ch.qos.logback.core.spi.FilterReply
import io.github.driveindex.Application
import io.github.driveindex.Application.Companion.Config
import io.github.driveindex.core.ConfigDto

/**
 * @author sgpublic
 * @Date 2022/8/5 18:38
 */
class ConsoleFilter : Filter<ILoggingEvent>() {
    private val self: Level
    private val out: Level

    init {
        val check = !Config.system.debug
        self = if (check) Level.INFO else if (Config.system.trace) Level.TRACE else Level.DEBUG
        out = if (check) Level.WARN else if (Config.system.trace) Level.DEBUG else Level.INFO
    }

    override fun decide(event: ILoggingEvent): FilterReply {
        val target = if (event.loggerName.startsWith(Application::class.java.packageName)) self else out
        return if (event.level.isGreaterOrEqual(target)) FilterReply.ACCEPT else FilterReply.DENY
    }
}