package io.github.driveindex

import com.charleskorn.kaml.Yaml
import com.vaadin.flow.component.page.AppShellConfigurator
import com.vaadin.flow.server.AppShellSettings
import com.vaadin.flow.server.PWA
import com.vaadin.flow.theme.Theme
import io.github.driveindex.utils.RemoteHttpClientFactory
import io.github.driveindex.core.DriveIndexConfig
import jakarta.annotation.PostConstruct
import org.jetbrains.exposed.v1.spring.boot.autoconfigure.ExposedAutoConfiguration
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.ApplicationContext
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import java.io.File
import java.util.*

@EnableFeignClients(defaultConfiguration = [RemoteHttpClientFactory::class])
@EnableScheduling
@EnableAsync
@SpringBootApplication
@ImportAutoConfiguration(ExposedAutoConfiguration::class)
@Theme("${Application.BASE_NAME_LOWER}-app")
@PWA(name = Application.BASE_NAME, shortName = Application.BASE_NAME)
class Application: AppShellConfigurator {
    override fun configurePage(settings: AppShellSettings) {

    }

    @PostConstruct
    fun init() {

    }

    companion object {
        const val BASE_NAME = "DriveIndex"
        const val BASE_NAME_LOWER = "driveindex"
        const val GROUP = "io.github.${BASE_NAME_LOWER}"

        private var configPath: String = "./config.yaml"
        val Config: DriveIndexConfig by lazy {
            Yaml.default.decodeFromString(DriveIndexConfig.serializer(), File(configPath).readText())
        }

        @JvmStatic
        fun main(args: Array<String>) {
            setupConfig(args)
            Bootstrap(Application::class.java)
                .setPort(Config.system.port)
                .setDatasource(
                    Config.sql.host,
                    Config.sql.database,
                    Config.sql.username,
                    Config.sql.password,
                )
                .setDebug(Config.system.debug)
                .setLogPath(Config.system.logDir)
                .run(args)
        }

        private fun setupConfig(args: Array<String>) {
            for (arg in args) {
                if (arg.startsWith("--config=")) {
                    configPath = arg.substring(9)
                    break
                }
            }
            System.getenv("DRIVEINDEX_CONFIG")
                    ?.takeIf { it.isNotBlank() }
                    ?.let { configPath = it }
        }
    }
}


private class Bootstrap(clazz: Class<*>) {
    private val application: SpringApplication = SpringApplication(clazz)
    private val properties: MutableMap<String, Any> = HashMap()

    fun setDatasource(
        dbHost: String,
        dbDatabase: String,
        dbUsername: String,
        dbPassword: String
    ): Bootstrap {
        properties["spring.datasource.username"] = dbUsername
        properties["spring.datasource.password"] = dbPassword
        properties["spring.datasource.url"] = "jdbc:postgresql://$dbHost/$dbDatabase"
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