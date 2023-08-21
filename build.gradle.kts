@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.plugin.spring) apply false
    alias(libs.plugins.kotlin.plugin.jpa) apply false
    alias(libs.plugins.kotlin.plugin.serialization) apply false

    alias(libs.plugins.kfc.library) apply false
    alias(libs.plugins.kfc.application) apply false

    alias(libs.plugins.org.springframework.boot) apply false
    // see https://youtrack.jetbrains.com/issue/KT-58021/NoClassDefFoundError-kotlin-enums-EnumEntries-with-language-version-1.9-or-2.0#focus=Comments-27-7955046.0-0
//    alias(libs.plugins.io.spring.dependency.management)
}
true
