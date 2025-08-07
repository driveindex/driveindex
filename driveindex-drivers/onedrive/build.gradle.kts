plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.plugin.spring)
    alias(libs.plugins.kotlin.plugin.serialization)
}

dependencies {
    implementation(libs.autoservice)
    implementation(libs.openfeign)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.jackson.annotations)
    implementation(project(":driveindex-core"))
}

kotlin {
    compilerOptions {
//        freeCompilerArgs = listOf(
//            "-Xcontext-parameters",
//        )
    }
}
