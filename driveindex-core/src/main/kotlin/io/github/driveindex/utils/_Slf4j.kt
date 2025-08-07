package io.github.driveindex.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory

val Any.log: Logger get() = LoggerFactory.getLogger(
    if (this::class.isCompanion) javaClass.enclosingClass else javaClass
)