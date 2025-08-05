plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.plugin.spring)
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.vaadin)
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.openfeign)
    implementation(libs.jackson.annotations)
    implementation(libs.classgraph)
}

kotlin {
    compilerOptions {
//        freeCompilerArgs = listOf(
//            "-Xcontext-parameters",
//        )
    }
}

vaadin {
    bunEnable = true
    reactEnable = true
}
