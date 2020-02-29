import org.jetbrains.kotlin.gradle.plugin.*

val koin_version: String by project
val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val mockk_version: String by project

buildscript {
    repositories {
        jcenter()
    }

    dependencies {
        classpath(kotlin("gradle-plugin", version = "1.3.61"))
    }
}


plugins {
    application
    kotlin("jvm") version "1.3.61"
}


repositories {
    jcenter()
    mavenCentral()
    maven { url = uri("https://dl.bintray.com/kotlin/ktor") }
    maven { url = uri("https://dl.bintray.com/kotlin/exposed") }
}
application{
    mainClassName = "io.ktor.server.cio.EngineMain"
}

kotlin {
    sourceSets {

        val main by getting {
            languageSettings.apply {
                apiVersion = "1.3"
                languageVersion = "1.3"
            }
            kotlin.srcDir("src")
            resources.srcDir("resources")

            dependencies {

                implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")
                implementation("io.ktor:ktor-server-cio:$ktor_version")
            //    implementation("io.ktor:ktor-server-netty:$ktor_version")
                implementation("io.ktor:ktor-locations:$ktor_version")
                implementation("io.ktor:ktor-jackson:$ktor_version")
                implementation("io.ktor:ktor-server-core:$ktor_version")
                implementation("io.ktor:ktor-server-host-common:$ktor_version")

                implementation("org.koin:koin-ktor:$koin_version")

                implementation("ch.qos.logback:logback-classic:$logback_version")
                implementation("org.jetbrains.exposed:exposed:0.3.2") {
                    exclude(module = "log4j")
                    exclude(module = "slf4j-log4j12")
                    exclude(module = "kotlin-stdlib")
                }
            }
        }

        val test by getting {
            dependsOn(main)
            kotlin.srcDir("test")

            dependencies {
                implementation("org.koin:koin-test:$koin_version")

                implementation("io.mockk:mockk:$mockk_version")
                implementation("io.ktor:ktor-server-test-host:$ktor_version")
            }
        }
    }
}

