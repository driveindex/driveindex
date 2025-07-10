plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.deps)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.plugin.spring)
    alias(libs.plugins.kotlin.plugin.jpa)
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.vaadin)
    alias(libs.plugins.node)
}

dependencies {
    implementation(libs.vaadin.spring.boot.starter)
    implementation(libs.bundles.spring.boot.security)
    implementation(libs.jackson.kotlin)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.serialization.yaml)

    implementation(libs.ini4j)
    implementation(libs.openfeign)

    runtimeOnly(libs.spring.boot.devtools)

    implementation(libs.bundles.spring.boot.data.exposed)
    implementation(libs.liquibase)
//    implementation(libs.exposed.migration)
    runtimeOnly(libs.bundles.jdbc)

    testImplementation(libs.spring.boot.starter.test)
}

kotlin {
    compilerOptions {
        freeCompilerArgs = listOf(
            "-Xcontext-parameters",
        )
    }
}

val nodeVer = "22.17.0"

node {
    isDownload = true
    isGlobal = false
    setNodeVersion(nodeVer)
    setNodeDir(file("node"))
    setUseNpm(false)
}

vaadin {
    bunEnable = true
    applicationProperties = file("src/main/resources/application.yml")
}

allOpen {
    annotations(
        "io.github.driveindex.annotation.AllOpen",
        "com.vaadin.hilla.Endpoint",
        "com.vaadin.hilla.BrowserCallable",
    )
}

tasks {
    vaadinPrepareFrontend {
        mustRunAfter(downloadNode)
        dependsOn(downloadNode)
    }
}
