package io.github.driveindex.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author sgpublic
 * @Date 2023/8/6 14:39
 */
@RestController
class HealthCheck {
    @GetMapping("/api/health")
    fun checkHealth() { }
}