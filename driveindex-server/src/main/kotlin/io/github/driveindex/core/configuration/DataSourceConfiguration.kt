package io.github.driveindex.core.configuration

import com.zaxxer.hikari.HikariDataSource
import io.github.driveindex.core.DriveIndexConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DataSourceConfiguration(
    private val config: DriveIndexConfig,
) {
    @Bean
    fun driveIndexDataSource() = HikariDataSource().apply {
        jdbcUrl = "jdbc:postgresql://${config.sql.host}/${config.sql.database}"
        username = config.sql.username
        password = config.sql.password
    }
}
