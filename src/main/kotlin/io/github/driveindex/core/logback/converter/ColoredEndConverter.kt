package io.github.driveindex.core.logback.converter

import ch.qos.logback.classic.pattern.ClassicConverter
import ch.qos.logback.classic.spi.ILoggingEvent

/**
 * @author sgpublic
 * @Date 2022/8/4 15:15
 */
class ColoredEndConverter : ClassicConverter() {
    override fun convert(event: ILoggingEvent): String {
        return ColoredConverter.Companion.FG_END + "\n"
    }
}