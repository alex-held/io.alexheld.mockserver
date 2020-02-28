val kotlin_version: String by project
val logback_version: String by project
val mockk_version: String by project
val koin_version: String by project
val ktor_version: String by project

plugins {
    kotlin("jvm")
    id("java-library")
    application
    java
}

buildscript {

    repositories {
        mavenCentral()
        jcenter()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.61")
    }
}



dependencies {

    kotlin("stdlib-jdk8")

    // Ktor - Server
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")

    // Ktor - Auth
    implementation("io.ktor:ktor-auth:$ktor_version")
    implementation("io.ktor:ktor-auth-jwt:$ktor_version")
    implementation("io.ktor:ktor-auth-ldap:$ktor_version")

    // Ktor - Features
    implementation("io.ktor:ktor-features:$ktor_version")
    implementation("io.ktor:ktor-gson:$ktor_version")
    implementation("io.ktor:ktor-html-builder:$ktor_version")
    implementation("io.ktor:ktor-locations:$ktor_version")
    implementation("io.ktor:ktor-metrics:$ktor_version")
    implementation("io.ktor:ktor-jackson:$ktor_version")
    implementation("io.ktor:ktor-server-sessions:$ktor_version")
    implementation("io.ktor:ktor-websockets:$ktor_version")

    // Ktor - DI
    implementation("org.koin:koin-core:$koin_version")

    // Ktor - Templating
    implementation("io.ktor:ktor-mustache:$ktor_version")

    // Ktor - Test
    testImplementation("io.ktor:ktor-client-mock-jvm:$ktor_version")
    testImplementation("io.ktor:ktor-client-mock:$ktor_version")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    testImplementation("io.ktor:ktor-server-test-host:$ktor_version")
    testImplementation("org.koin:koin-test:$koin_version")



    implementation("ch.qos.logback:logback-classic:$logback_version")
 //   testImplementation("io.mockk:mockk:$mockk_version")


    implementation("com.microsoft.azure:applicationinsights-core:2.6.0-BETA.2")
    implementation("ch.qos.logback:logback-classic:1.2.3")
    testImplementation(group = "junit", name = "junit", version = "4.12")
}

application {
    mainClassName = "io.ktor.server.netty.EngineMain"

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
    }
}

project(":mockserver-api")
