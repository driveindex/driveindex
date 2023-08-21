@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.plugin.spring)
    alias(libs.plugins.kotlin.plugin.jpa)
    alias(libs.plugins.kotlin.plugin.serialization)

    alias(libs.plugins.org.springframework.boot)
    alias(libs.plugins.io.spring.dependency.management)

    application
}

group = "io.github.driveindex"
version = "1.0-SNAPSHOT"

kotlin {
    jvm {
        jvmToolchain(17)
        withJava()
        testRuns.named("test") {
            executionTask.configure {
                useJUnitPlatform()
            }
        }
    }
    js {
        binaries.executable()
        browser {
            commonWebpackConfig {
                cssSupport {
                    enabled.set(true)
                }
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.serialization.json)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(libs.kotlin.reflect)
                implementation(libs.kotlin.stdlib.jdk8)

                implementation(libs.spring.boot.starter)
                implementation(libs.spring.boot.starter.web)
                implementation(libs.spring.boot.starter.security)
//                implementation(libs.spring.boot.starter.websocket)

                implementation(libs.spring.cloud.starter.openfeign)

                implementation(libs.knife4j.openapi3.jakarta.spring.boot.starter)
                implementation(libs.springdoc.openapi.security)

                implementation(libs.spring.boot.starter.data.jpa)
                implementation(libs.spring.boot.starter.validation)
                runtimeOnly(libs.mariadb.java.client)

                runtimeOnly(libs.flyway.core)
                runtimeOnly(libs.flyway.mysql)
                runtimeOnly(libs.flyway.sqlserver)

                implementation(libs.ini4j)
                implementation(libs.kotlinx.serialization.json.jvm)
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(libs.spring.boot.test)
                implementation(libs.spring.boot.starter.test)
                implementation(libs.spring.security.test)
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(platform(libs.kotlinw.wrappers.bom.get()))
                implementation(libs.kotlinw.node)
                implementation(libs.kotlinw.typescript)
                implementation(libs.kotlinw.emotion)
                implementation(libs.kotlinw.react)
                implementation(libs.kotlinw.react.dom)
                implementation(libs.kotlinw.react.router.dom)

                implementation(libs.kotlinx.serialization.json.js)
            }
        }
        val jsTest by getting
    }
}

application {
    mainClass.set("io.github.driveindex.application.ServerKt")
}

tasks.named<Copy>("jvmProcessResources") {
    val jsBrowserDistribution = tasks.named("jsBrowserDistribution")
    from(jsBrowserDistribution)
}

tasks.named<JavaExec>("run") {
    dependsOn(tasks.named<Jar>("jvmJar"))
    classpath(tasks.named<Jar>("jvmJar"))
}