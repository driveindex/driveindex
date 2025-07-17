package io.github.driveindex.controller

import com.vaadin.hilla.BrowserCallable
import io.github.driveindex.core.annotation.AllOpen
import jakarta.annotation.security.PermitAll

/**
 * @author sgpublic
 * @Date 2023/8/6 14:39
 */
@AllOpen
@BrowserCallable
class HealthCheck {
    @PermitAll
    fun checkHealth() = ""
}