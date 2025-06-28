plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.deps)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.plugin.spring)
    alias(libs.plugins.kotlin.plugin.jpa)
    alias(libs.plugins.vaadin)
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
    nodeVersion = "v22.17.0"
    nodeAutoUpdate = true
}

allOpen {
    annotations(
        "com.vaadin.hilla.Endpoint",
        "com.vaadin.hilla.BrowserCallable",
    )
}
