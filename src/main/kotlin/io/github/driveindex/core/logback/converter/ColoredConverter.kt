package io.github.driveindex.core.logback.converter

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.pattern.CompositeConverter

/**
 * @author sgpublic
 * @Date 2022/8/4 15:15
 */
class ColoredConverter : CompositeConverter<ILoggingEvent>() {
    override fun transform(event: ILoggingEvent, s: String): String {
        return colored.getOrDefault(event.level, FG_TRACE) + s + FG_END
    }

    companion object {
        // 利用 HashMap 提高性能
        private val colored: MutableMap<Level, String> = HashMap()
        const val FG_TRACE = "\u001B[1;37;1m"

        init {
            colored[Level.TRACE] = FG_TRACE
            colored[Level.DEBUG] = "\u001B[1;36;1m"
            colored[Level.INFO] = "\u001B[1;32;1m"
            colored[Level.WARN] = "\u001B[1;33;1m"
            colored[Level.ERROR] = "\u001B[1;31;1m"
        }

        const val FG_END = "\u001B[0m"
    }
}