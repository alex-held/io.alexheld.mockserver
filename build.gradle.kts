import org.jetbrains.kotlin.gradle.tasks.*

val koin_version: String by project
val kodein_version: String by project
val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val mockk_version: String by project
val spek_version: String by project
val junit_version: String by project
val arrow_version: String by rootProject
val guice_version: String by rootProject
val kluent_version: String by rootProject



buildscript {
    repositories {
        jcenter()
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
    mavenCentral()
    maven { url = uri("https://dl.bintray.com/kotlin/ktor") }
}

apply(plugin = "org.sonarqube")

plugins {
    kotlin("jvm") version "1.3.61"
    id("org.gradle.kotlin.kotlin-dsl") version "1.3.3"
    id("org.jetbrains.dokka") version "0.10.1"
    application
    java
    jacoco

}

application {
    group = "io.alexheld"
    mainClassName = "io.ktor.server.cio.EngineMain"
}

kotlin {

    experimental {
        coroutines = org.jetbrains.kotlin.gradle.dsl.Coroutines.ENABLE
    }

    sourceSets {

        val main by getting {
            languageSettings.apply {
                languageVersion = "1.3"
                apiVersion = "1.3"
            }
            kotlin.srcDir("src")
            resources.srcDir("resources")
        }
        val test by getting {
            languageSettings.apply {
                languageVersion = "1.3"
                apiVersion = "1.3"
            }
            dependsOn(main)
            kotlin.srcDir("test")
        }
    }
}


allprojects {

    repositories {
        mavenCentral()
        jcenter()
        maven { url = uri("https://dl.bintray.com/arrow-kt/arrow-kt/") }
        maven { url = uri("https://oss.jfrog.org/artifactory/oss-snapshot-local/") } // for SNAPSHOT builds
    }
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_13
    targetCompatibility = JavaVersion.VERSION_13
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$ktor_version")
    implementation("io.ktor:ktor-server-cio:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    // implementation("io.ktor:ktor-locations:$ktor_version")
    implementation("io.ktor:ktor-jackson:$ktor_version")
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-host-common:$ktor_version")

    implementation("joda-time:joda-time:2.10.5")
    implementation("com.google.inject:guice:$guice_version")
    // implementation("com.google.code.gson:gson:2.8.6")

    implementation("io.arrow-kt:arrow-core:$arrow_version")
    // implementation("io.arrow-kt:arrow-optics:$arrow_version")
    implementation("io.arrow-kt:arrow-fx:$arrow_version")
    //implementation("io.arrow-kt:arrow-syntax:$arrow_version")
    // kapt("io.arrow-kt:arrow-meta:$arrow_version")

    // TEST
    testImplementation("org.junit.jupiter:junit-jupiter:5.6.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    testImplementation("io.kotlintest:kotlintest-runner-junit5:3.4.2")
    testImplementation("org.amshove.kluent:kluent:$kluent_version")


    // testImplementation("io.ktor:ktor-server-test-host:$ktor_version")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
