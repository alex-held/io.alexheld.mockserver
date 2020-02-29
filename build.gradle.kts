import org.jetbrains.kotlin.gradle.dsl.Coroutines.*

val koin_version: String by project
val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val mockk_version: String by project
val spek_version: String by project
val junit_version: String by project

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
    maven { url = uri("https://dl.bintray.com/kotlin/exposed") }
}

apply(plugin = "org.sonarqube")
plugins {
    kotlin("jvm") version "1.3.61"
    id("org.jetbrains.dokka") version "0.10.1"
    application
    java
    jacoco
}

application {
    group = "io.alexheld"
    mainClassName = "io.ktor.server.cio.EngineMain"
}

kotlin.sourceSets["main"].kotlin.srcDirs("src")
kotlin.sourceSets["test"].kotlin.srcDirs("test")

sourceSets["main"].resources.srcDirs("resources")
//sourceSets["test"].resources.srcDirs("testresources")


dependencies {

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$ktor_version")
    implementation("io.ktor:ktor-server-cio:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-locations:$ktor_version")
    implementation("io.ktor:ktor-jackson:$ktor_version")
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-host-common:$ktor_version")
    implementation("org.koin:koin-ktor:$koin_version")

    implementation("joda-time:joda-time:2.10.5")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("com.google.code.gson:gson:2.8.6")

    // TEST

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testImplementation("io.kotlintest:kotlintest-runner-junit5:3.4.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")
    testImplementation("io.ktor:ktor-server-test-host:$ktor_version")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

kotlin {
    experimental { coroutines = ENABLE }
}
