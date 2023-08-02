plugins {
    id("org.springframework.boot") version "3.0.5" apply false
    id("io.spring.dependency-management") version "1.1.0" apply false

    val kotlin = "1.9.0"
    kotlin("jvm") version kotlin apply false
    kotlin("plugin.serialization") version kotlin apply false
    kotlin("plugin.spring") version kotlin apply false
    kotlin("plugin.jpa") version kotlin apply false
}