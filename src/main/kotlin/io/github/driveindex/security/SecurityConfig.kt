package io.github.driveindex.security

import com.nimbusds.jose.JWSAlgorithm
import com.vaadin.flow.spring.security.VaadinWebSecurity
import com.vaadin.hilla.route.RouteUtil
import io.github.driveindex.Application
import io.github.driveindex.core.ConfigManager
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import javax.crypto.spec.SecretKeySpec
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

/**
 * @author sgpublic
 * @Date 2022/8/3 19:44
 */
@EnableWebSecurity
@Configuration
class SecurityConfig(
    private val route: RouteUtil
): VaadinWebSecurity() {
    @OptIn(ExperimentalEncodingApi::class)
    override fun configure(http: HttpSecurity) {
        http.authorizeHttpRequests {
            it.requestMatchers(route::isRouteAllowed).permitAll()
        }
        super.configure(http)

        http.sessionManagement {
            it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }

        setStatelessAuthentication(
            http,
            SecretKeySpec(Base64.decode(ConfigManager.TokenSecurityKey), JWSAlgorithm.HS256.name),
            Application.APPLICATION_GROUP
        )
    }

    companion object {
        const val ROLE_ADMIN = "ROLE_ADMIN"
        const val ROLE_USER = "ROLE_USER"
    }
}