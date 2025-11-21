package io.github.driveindex.core.configuration

import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addCommandLineSource
import com.sksamuel.hoplite.addResourceSource
import com.sksamuel.hoplite.sources.EnvironmentVariablesPropertySource
import io.github.driveindex.core.DriveIndexConfig
import io.github.driveindex.core.utils.letIfNotNull
import org.springframework.beans.factory.config.BeanFactoryPostProcessor
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.web.server.ConfigurableWebServerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component

@Configuration
class DriveIndexConfigConfiguration {
    @Bean
    fun driveIndexConfig(args: ApplicationArguments): DriveIndexConfig {
        var configPath: String? = null
        for (arg in args.sourceArgs) {
            if (arg.startsWith("--config=")) {
                configPath = arg.substring(9)
                break
            }
        }
        System.getenv("DRIVEINDEX_CONFIG")
            ?.takeIf { it.isNotBlank() }
            ?.let { configPath = it }
        return ConfigLoaderBuilder.default()
            .letIfNotNull(configPath) { configPath ->
                addResourceSource(configPath, true)
            }
            .addResourceSource("./config.toml", true)
            .addResourceSource("./config.yaml", true)
            .addResourceSource("./config.yml", true)
            .addPropertySource(EnvironmentVariablesPropertySource(
                prefix = "DRIVEINDEX"
            ))
            .addCommandLineSource(args.sourceArgs)
            .build()
            .loadConfigOrThrow()
    }
}

@Component
class ServerConfiguration(
    private val config: DriveIndexConfig
): BeanFactoryPostProcessor {
    override fun postProcessBeanFactory(beanFactory: ConfigurableListableBeanFactory) {
        for((_, obj) in beanFactory.getBeansOfType(ConfigurableWebServerFactory::class.java)) {
            obj.setPort(config.system.port)
        }
    }
}
