package io.github.driveindex.annotations

import io.github.driveindex.drivers.api.ClientType
import org.springframework.stereotype.Component

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Component
annotation class DriveClient(
    val type: ClientType
)
