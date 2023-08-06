package io.github.driveindex

import io.github.driveindex.core.ConfigManager
import jakarta.annotation.PostConstruct
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.ApplicationContext
import org.springframework.scheduling.annotation.EnableScheduling
import java.util.*
import kotlin.reflect.KClass

@EnableScheduling
@SpringBootApplication
class Application {
    @PostConstruct
    fun setup() {

    }

    companion object {
        const val APPLICATION_BASE_NAME = "DriveIndex"
        val APPLICATION_BASE_NAME_LOWER = APPLICATION_BASE_NAME.lowercase(Locale.getDefault())

        private lateinit var context: ApplicationContext
        val Context: ApplicationContext get() = context

        @JvmStatic
        fun main(args: Array<String>) {
            context = Bootstrap(Application::class.java)
                .setDatasource(
                        ConfigManager.SqlDatabaseHost,
                        ConfigManager.SqlDatabaseName,
                        ConfigManager.SqlUsername,
                        ConfigManager.SqlPassword,
                )
                .setDebug(ConfigManager.Debug)
                .setLogPath(ConfigManager.LogPath)
                .run(args)
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
            dbHost: String, dbDatabase: String,
            dbUsername: String, dbPassword: String
    ): Bootstrap {
        properties["spring.datasource.username"] = dbUsername
        properties["spring.datasource.password"] = dbPassword
        properties["spring.datasource.url"] = "jdbc:mariadb://$dbHost/$dbDatabase"
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

    fun setLogPath(path: String): Bootstrap {
        properties["driveindex.logging.path"] = path
        return this
    }

    fun run(args: Array<String>): ApplicationContext {
        application.setDefaultProperties(properties)
        return application.run(*args)
    }
}