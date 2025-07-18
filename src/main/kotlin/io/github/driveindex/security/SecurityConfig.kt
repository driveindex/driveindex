package io.github.driveindex.security

import com.vaadin.flow.spring.security.VaadinWebSecurity
import com.vaadin.hilla.route.RouteUtil
import io.github.driveindex.Application
import io.github.driveindex.Application.Companion.Config
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.jose.jws.JwsAlgorithms
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import javax.crypto.spec.SecretKeySpec
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
            it.requestMatchers(
                "/share",
            ).permitAll()
            it.requestMatchers(
                "/drawable/**",
            ).permitAll()
        }
        super.configure(http)

        http.sessionManagement {
            it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }

        setLoginView(http, "/login")

        setStatelessAuthentication(
            http,
            SecretKeySpec(Config.token.jwtSecurity.toByteArray(), JwsAlgorithms.HS256),
            Application.GROUP,
            Config.token.expired,
        )

        // use stateless auth, so disable csrf.
        http.csrf {
            it.disable()
        }
    }

    companion object {
        const val ROLE_ADMIN = "ROLE_ADMIN"
        const val ROLE_USER = "ROLE_USER"
    }
}