package io.github.driveindex

import com.vaadin.flow.component.page.AppShellConfigurator
import com.vaadin.flow.server.AppShellSettings
import com.vaadin.flow.theme.Theme
import io.github.driveindex.configuration.FeignClientConfig
import io.github.driveindex.core.ConfigManager
import io.github.driveindex.core.util.CanonicalPath
import jakarta.annotation.PostConstruct
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.ApplicationContext
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import java.io.File
import java.util.*
import kotlin.reflect.KClass

@EnableFeignClients(defaultConfiguration = [FeignClientConfig::class])
@EnableScheduling
@EnableAsync
@SpringBootApplication
@Theme("${Application.APPLICATION_BASE_NAME_LOWER}-app")
class Application: AppShellConfigurator {
    override fun configurePage(settings: AppShellSettings) {

    }

    @PostConstruct
    fun init() {

    }

    companion object {
        const val APPLICATION_BASE_NAME = "DriveIndex"
        const val APPLICATION_BASE_NAME_LOWER = "driveindex"
        const val APPLICATION_GROUP = "io.github.driveindex"

        private lateinit var context: ApplicationContext
        val Context: ApplicationContext get() = context

        @JvmStatic
        fun main(args: Array<String>) {
            setupConfig(args)
            context = Bootstrap(Application::class.java)
                .setPort(ConfigManager.Port)
                .setDatasource(
                        ConfigManager.SqlType,
                        ConfigManager.SqlDatabaseHost,
                        ConfigManager.SqlDatabaseName,
                        ConfigManager.SqlUsername,
                        ConfigManager.SqlPassword,
                )
                .setDebug(ConfigManager.Debug)
                .setLogPath(ConfigManager.LogPath)
                .run(args)
        }

        private fun setupConfig(args: Array<String>) {
            var configPath: String? = null
            for (arg in args) {
                if (arg.startsWith("--config=")) {
                    configPath = arg.substring(9)
                    return
                }
            }
            System.getenv("DRIVEINDEX_CONFIG")
                    ?.takeIf { it.isNotBlank() }
                    ?.let { configPath = it }
            ConfigManager.setConfigFile(configPath)
        }

        inline fun <reified T> getBean(): T {
            return Context.getBean(T::class.java)
        }

        val <T: Any> KClass<T>.Bean: T get() {
            return Context.getBean(java)
        }
    }
}


private class Bootstrap(clazz: Class<*>) {
    private val application: SpringApplication = SpringApplication(clazz)
    private val properties: MutableMap<String, Any> = HashMap()

    fun setDatasource(
        dbType: String,
        dbHost: String,
        dbDatabase: String,
        dbUsername: String,
        dbPassword: String
    ): Bootstrap {
        properties["spring.datasource.username"] = dbUsername
        properties["spring.datasource.password"] = dbPassword
        properties["spring.datasource.url"] = "jdbc:$dbType://${if (dbType != "sqlite") "$dbHost/" else ""}$dbDatabase"
        return this
    }

    fun setPort(port: Int): Bootstrap {
        properties["server.port"] = port
        return this
    }

    fun setDebug(isDebug: Boolean): Bootstrap {
        if (isDebug) {
            properties["spring.profiles.active"] = "dev"
        } else {
            properties["spring.profiles.active"] = "prod"
        }
        return this
    }

    fun test(): Bootstrap {
        properties["spring.profiles.active"] = "test"
        return this
    }

    fun setLogPath(path: String): Bootstrap {
        val logPath = File(path)
        if (!logPath.exists()) {
            logPath.mkdirs()
        }
        properties["driveindex.logging.path"] = logPath.path
        return this
    }

    fun run(args: Array<String>): ApplicationContext {
        application.setDefaultProperties(properties)
        return application.run(*args)
    }
}