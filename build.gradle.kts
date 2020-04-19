import org.jetbrains.kotlin.gradle.tasks.*

extra["arrow_version"] = "0.10.4"
extra["mongodb_version"] = "4.0.2"
extra["kotlin_version"] = "1.3.72"
extra["log4j_version"] = "2.12.1"
extra["http4k_version"] = "3.196.0"
extra["jaxb_version"] = "2.3.0"
extra["jackson_version"] = "2.10.2"
extra["kluent_version"] = "1.60"
extra["koin_version"] = "2.1.0"
extra["azureVersion"] = "2.1.7"
extra["junit_version"] = "5.6.2"


buildscript {

    repositories {
        jcenter()
        mavenCentral()
    }

    dependencies {
     //   classpath(kotlin("gradle-plugin", "1.3.72"))
      //  classpath("org.jetbrains.kotlin:kotlin-allopen:1.3.72")
    }
}


plugins {
    id("org.springframework.boot") version "2.2.6.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    id("org.gradle.kotlin.kotlin-dsl") version "1.3.6"


    kotlin("jvm") version "1.3.72"
    kotlin("plugin.spring") version "1.3.72"
    kotlin("kapt") version "1.3.72"

  //  id("com.github.ben-manes.versions") version "0.26.0"
  //  id("com.adarshr.test-logger") version "2.0.0"
    application
    java
}



java {
    sourceCompatibility = javaversio //JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

application {
    mainClassName = "io.alexheld.mockserver.MainKt"
    group = "io.alexheld.mockserver"
}


val developmentOnly by configurations.creating
configurations {
    runtimeClasspath {
        extendsFrom(developmentOnly)
    }
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}



repositories {
    jcenter()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {

    // koin
    implementation("org.koin:koin-core:${property("koin_version")}")
    implementation("org.koin:koin-core-ext:${property("koin_version")}")

    // kotlin
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlin:kotlin-reflect")
   // implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // jwt
    implementation("io.jsonwebtoken:jjwt:0.9.1")

    // xml
    implementation("javax.xml.bind:jaxb-api:${property("jaxb_version")}")
    implementation("com.sun.xml.bind:jaxb-core:${property("jaxb_version")}")
    implementation("com.sun.xml.bind:jaxb-impl:${property("jaxb_version")}")

    // http4k
    implementation("org.http4k:http4k-core:${property("http4k_version")}")
    implementation("org.http4k:http4k-server-jetty:${property("http4k_version")}")
    implementation("org.http4k:http4k-format-jackson:${property("http4k_version")}")
    implementation("org.http4k:http4k-client-apache:${property("http4k_version")}")

    // log4j
    implementation("org.apache.logging.log4j:log4j-core:${property("log4j_version")}")
    implementation("org.apache.logging.log4j:log4j-api:${property("log4j_version")}")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:${property("log4j_version")}")
    // infrastructure

    // arrow
    implementation("io.arrow-kt:arrow-core:${property("arrow_version")}")
    implementation ("io.arrow-kt:arrow-syntax:${property("arrow_version")}")

    // jackson
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:${property("jackson_version")}")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-joda:${property("jackson_version")}")
    implementation("com.fasterxml.jackson.jaxrs:jackson-jaxrs-base:${property("jackson_version")}")
    implementation("com.fasterxml.jackson.core:jackson-databind:${property("jackson_version")}")
    implementation("com.fasterxml.jackson.core:jackson-annotations:${property("jackson_version")}")
    implementation("com.fasterxml.jackson.core:jackson-core:${property("jackson_version")}")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:${property("jackson_version")}")


    // == == == ==
    // T E S T
    // == == == ==

    testImplementation("org.amshove.kluent:kluent:${property("kluent_version")}")
    testImplementation("io.mockk:mockk:1.9.3")


    // Test container + Junit5

    testImplementation ("org.junit.jupiter:junit-jupiter-api:${property("junit_version")}")
    testImplementation( "org.junit.jupiter:junit-jupiter-params:${property("junit_version")}")
    testRuntimeOnly( "org.junit.jupiter:junit-jupiter-engine:${property("junit_version")}")
    //testImplementation("org.junit.jupiter:junit-jupiter:${property("junit_version")}")
   // testRuntimeOnly( "org.junit.vintage:junit-vintage-engine:${property("junit_version")}")
    // testImplementation("org.jetbrains.kotlin:kotlin-test-junit:${property("kotlin_version")}")


    testImplementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo")
    testImplementation("org.testcontainers:testcontainers:1.14.0")
    testImplementation("org.testcontainers:junit-jupiter:1.14.0")


    // Infra
    implementation("org.litote.kmongo:kmongo:4.0.0")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")

    // Tools
    developmentOnly("org.springframework.boot:spring-boot-devtools")


    // Annotation
    //kapt("io.arrow-kt:arrow-meta:${property("arrow_version")}")
    /*compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")*/
}

/*
tasks.jacocoTestReport {
    reports {
        xml.isEnabled = true
    }
}*/

tasks.withType<Test> {
    useJUnitPlatform()
}


tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget= "1.8"
    kotlinOptions.freeCompilerArgs = listOf("-Xjsr305=strict")
}

