package io.github.driveindex.controller

import com.vaadin.hilla.BrowserCallable
import io.github.driveindex.annotation.AllOpen
import io.github.driveindex.dto.resp.Resp
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