package io.github.driveindex.core.logback.encoder

import ch.qos.logback.classic.PatternLayout
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import io.github.driveindex.core.logback.converter.TraceConverter

/**
 * @author sgpublic
 * @Date 2022/8/5 18:30
 */
class ConsolePatternLayoutEncoder : PatternLayoutEncoder() {
    companion object {
        init {
            PatternLayout.DEFAULT_CONVERTER_MAP["trace"] = TraceConverter::class.java.name
            PatternLayout.DEFAULT_CONVERTER_MAP["fulltrace"] = TraceConverter::class.java.name
        }
    }
}