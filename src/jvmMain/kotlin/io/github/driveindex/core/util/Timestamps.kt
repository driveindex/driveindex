package io.github.driveindex.core.util

import java.sql.Timestamp
import java.time.Instant
import java.util.*

/**
 * @author sgpublic
 * @Date 2022/8/17 9:23
 */
val String.asUtcTime: Long get() {
    return Timestamp.from(Instant.parse(this)).time
}

val Long.asDate: Date get() {
    return Date(this)
}