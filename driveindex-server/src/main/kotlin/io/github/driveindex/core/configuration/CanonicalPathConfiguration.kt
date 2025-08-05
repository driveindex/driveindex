package io.github.driveindex.core.configuration

import io.github.driveindex.utils.CanonicalPath
import org.springframework.context.annotation.Configuration
import org.springframework.format.Formatter
import org.springframework.format.FormatterRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.util.Locale


object CanonicalPathFormatter: Formatter<CanonicalPath> {
    override fun print(target: CanonicalPath, locale: Locale): String {
        return target.path
    }

    override fun parse(text: String, locale: Locale): CanonicalPath {
        return CanonicalPath.of(text)
    }
}

@Configuration
class CanonicalPathConfiguration: WebMvcConfigurer {
    override fun addFormatters(registry: FormatterRegistry) {
        registry.addFormatter(CanonicalPathFormatter)
    }
}