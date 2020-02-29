import org.jetbrains.kotlin.gradle.dsl.Coroutines.*

val koin_version: String by project
val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val mockk_version: String by project

buildscript {
    repositories {
        jcenter()
        gradlePluginPortal()
        mavenCentral()
        maven {
             url = uri("https://plugins.gradle.org/m2/")
        }
    }

    dependencies {
        classpath(kotlin("gradle-plugin", version = "1.3.61"))
        classpath("org.sonarsource.scanner.gradle:sonarqube-gradle-plugin:2.8")
    }
}

repositories {
    jcenter()
    gradlePluginPortal()
    mavenCentral()
    maven { url = uri("https://dl.bintray.com/kotlin/ktor") }
    maven { url = uri("https://plugins.gradle.org/m2/") }
    maven { url = uri("https://dl.bintray.com/kotlin/exposed") }
}

apply(plugin = "org.sonarqube")
plugins {
    application
    id("org.jetbrains.dokka") version "0.10.1"
    id("jacoco")
    id("idea")
    kotlin("jvm") version "1.3.61"
}

application {
    group = "io.alexheld"
    mainClassName = "io.ktor.server.cio.EngineMain"
}

kotlin.sourceSets["main"].kotlin.srcDirs("src")
kotlin.sourceSets["test"].kotlin.srcDirs("test")

sourceSets["main"].resources.srcDirs("resources")
sourceSets["test"].resources.srcDirs("testresources")

dependencies {

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")
    implementation("io.ktor:ktor-server-cio:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-locations:$ktor_version")
    implementation("io.ktor:ktor-jackson:$ktor_version")
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-host-common:$ktor_version")

    implementation("org.koin:koin-ktor:$koin_version")
    implementation("org.koin:koin-core:$koin_version")

    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("org.jetbrains.exposed:exposed:0.14.1")

    testImplementation("org.koin:koin-test:$koin_version")
    testImplementation("io.mockk:mockk:$mockk_version")
    testImplementation("io.ktor:ktor-server-test-host:$ktor_version")
}

kotlin {

    experimental {
        this.coroutines = ENABLE

    }
}

