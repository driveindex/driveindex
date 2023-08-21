package io.github.driveindex.security

import io.github.driveindex.core.ConfigManager
import io.github.driveindex.security.filter.IUsernamePasswordAuthenticationFilter
import io.github.driveindex.security.filter.JwtTokenAuthenticationFilter
import io.github.driveindex.security.handler.IAccessDeniedHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

/**
 * @author sgpublic
 * @Date 2022/8/3 19:44
 */
@Configuration
class SecurityConfig(
    private val password: IUsernamePasswordAuthenticationFilter,
    private val jwt: JwtTokenAuthenticationFilter,
    private val accessDeniedHandler: IAccessDeniedHandler,
    private val entryPoint: IAuthenticationEntryPoint
) {
    @Bean
    fun filterChain(http: HttpSecurity, environment: Environment): SecurityFilterChain {
        if (ConfigManager.Debug) {
            if (environment.getProperty("spring.h2.console.enabled", Boolean::class.java, false)) {
                // 若开启 h2-console 则允许 iframe
                http.headers().frameOptions().disable()
            }
            registerCorsConfiguration("/api/**", CorsConfiguration().apply {
                allowedHeaders = listOf(CorsConfiguration.ALL)
                allowedMethods = listOf(CorsConfiguration.ALL)
                allowedOrigins = listOf(CorsConfiguration.ALL)
            })
        }
        http.cors().configurationSource(corsConfigurationSource)
        http.csrf().disable()
        http.httpBasic().disable()
        http.addFilterBefore(password, UsernamePasswordAuthenticationFilter::class.java)
            .addFilterAfter(jwt, IUsernamePasswordAuthenticationFilter::class.java)
        http.authorizeHttpRequests()
            .requestMatchers("/api/token_state").authenticated()
            .requestMatchers("/api/admin/**").hasRole(UserRole.ADMIN.name)
            .requestMatchers("/api/user/**").hasRole(UserRole.USER.name)
            .anyRequest().permitAll()
        http.exceptionHandling()
            .accessDeniedHandler(accessDeniedHandler)
            .authenticationEntryPoint(entryPoint)
        return http.build()
    }

    companion object {
        const val JWT_TAG = "tag"
        const val JWT_USERNAME = "username"
        const val Header = "Authorization"

        private val corsConfigurationSource: UrlBasedCorsConfigurationSource by lazy {
            UrlBasedCorsConfigurationSource()
        }
        private fun registerCorsConfiguration(pattern: String, conf: CorsConfiguration): Companion {
            corsConfigurationSource.registerCorsConfiguration(pattern, conf)
            return this
        }
    }
}