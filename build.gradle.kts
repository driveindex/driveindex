plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.deps)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.plugin.spring)
    alias(libs.plugins.kotlin.plugin.jpa)
    alias(libs.plugins.vaadin)
    alias(libs.plugins.node)
}

kotlin {
    compilerOptions {
        freeCompilerArgs = listOf(
            "-Xjsr305=strict",
        )
    }
}

dependencies {
    implementation(libs.vaadin.spring.boot.starter)
    implementation(libs.spring.boot.starter.security)

    implementation(libs.bundles.jjwt)
    implementation(libs.ini4j)
    implementation(libs.openfeign)

    runtimeOnly(libs.spring.boot.devtools)

    implementation(libs.bundles.spring.boot.data.jpa)
    implementation(libs.liquibase)
    runtimeOnly(libs.bundles.jdbc)

    testImplementation(libs.spring.boot.starter.test)
}

vaadin {
    pnpmEnable = true
}

node {
    isDownload = true
    isGlobal = false
    setNodeVersion("22.16.0")
    setNodeDir(file("node"))
}

allOpen {
    annotations(
        "com.vaadin.hilla.Endpoint",
        "com.vaadin.hilla.BrowserCallable",
    )
}

tasks {
    val vaadinPrepareFrontend by getting {
        mustRunAfter(downloadNode)
        dependsOn(downloadNode)
    }
}
