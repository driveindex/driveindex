package io.github.driveindex

import com.vaadin.flow.component.page.AppShellConfigurator
import com.vaadin.flow.server.AppShellSettings
import com.vaadin.flow.server.PWA
import com.vaadin.flow.theme.Theme
import io.github.driveindex.utils.RemoteHttpClientFactory
import jakarta.annotation.PostConstruct
import org.jetbrains.exposed.v1.spring.boot.autoconfigure.ExposedAutoConfiguration
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling

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

        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(Application::class.java, *args)
        }
    }
}
