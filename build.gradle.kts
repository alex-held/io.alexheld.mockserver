import org.jetbrains.kotlin.gradle.dsl.*

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
    }

    dependencies {
        classpath(kotlin("gradle-plugin", version = "1.3.61"))
   //     classpath("org.sonarsource.scanner.gradle:sonarqube-gradle-plugin:2.8")
    }
}

//apply(plugin = "org.sonarqube")
apply(plugin = "kotlin")

plugins {
    base
    kotlin("jvm") version "1.3.61"
   // id("org.jetbrains.dokka") version "0.10.1"
    application
    java
    //jacoco

}

application {
    group = "io.alexheld.mockserver"
    mainClassName = "io.ktor.server.netty.EngineMain"
}

kotlin {

    java.targetCompatibility = org.gradle.api.JavaVersion.VERSION_1_8
    java.sourceCompatibility = org.gradle.api.JavaVersion.VERSION_1_8

}


repositories {
    jcenter()
            //mavenCentral()
    mavenLocal()
    maven { url = uri("https://dl.bintray.com/kotlin/ktor") }
            //å  maven { url = uri("https://dl.bintray.com/arrow-kt/arrow-kt/") }
    //maven { url = uri("https://oss.jfrog.org/artifactory/oss-snapshot-local/") }
}

dependencies {
    // implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$ktor_version")
   // implementation("io.ktor:ktor-server-cio:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    // implementation("io.ktor:ktor-locations:$ktor_version")
    implementation("io.ktor:ktor-jackson:$ktor_version")
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-host-common:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("org.koin:koin-ktor:$koin_version")

//    implementation("joda-time:joda-time:2.10.5")
//    implementation("com.google.inject:guice:$guice_version")
    // implementation("com.google.code.gson:gson:2.8.6")

   // implementation("io.arrow-kt:arrow-core:$arrow_version")
    // implementation("io.arrow-kt:arrow-optics:$arrow_version")
    // implementation("io.arrow-kt:arrow-fx:$arrow_version")
    //implementation("io.arrow-kt:arrow-syntax:$arrow_version")
    // kapt("io.arrow-kt:arrow-meta:$arrow_version")

    // TEST
    testImplementation("org.junit.jupiter:junit-jupiter:5.6.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    testImplementation("io.kotlintest:kotlintest-runner-junit5:3.4.2")
    testImplementation("org.amshove.kluent:kluent:$kluent_version")


    // testImplementation("io.ktor:ktor-server-test-host:$ktor_version")
}

java {
    targetCompatibility = org.gradle.api.JavaVersion.VERSION_1_8
    sourceCompatibility = org.gradle.api.JavaVersion.VERSION_1_8
}

tasks.withType<Test> {
    useJUnitPlatform()
}

