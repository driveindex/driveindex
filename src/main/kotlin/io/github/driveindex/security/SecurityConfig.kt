package io.github.driveindex.security

import com.vaadin.flow.spring.security.VaadinWebSecurity
import com.vaadin.hilla.route.RouteUtil
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

/**
 * @author sgpublic
 * @Date 2022/8/3 19:44
 */
@EnableWebSecurity
@Configuration
class SecurityConfig(
    private val route: RouteUtil
): VaadinWebSecurity() {
    override fun configure(http: HttpSecurity) {
        http.authorizeHttpRequests { registry ->
            registry.requestMatchers(route::isRouteAllowed).permitAll()
        }
        super.configure(http)
    }

    companion object {
        const val ROLE_ADMIN = "ROLE_ADMIN"
        const val ROLE_USER = "ROLE_USER"
    }
}