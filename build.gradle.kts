import org.jetbrains.kotlin.gradle.tasks.*

val koin_version: String by project
val ktor_version: String by project
val logback_version: String by project
val kluent_version: String by project


extra["arrow_version"] = "0.10.4"
extra["kotlin_version"] = "1.3.70"
extra["snippetsDir"] = file("build/generated-snippets")
extra["azureVersion"] = "2.1.7"
extra["springCloudVersion"] = "Greenwich.SR5"
extra["vaadinVersion"] = "14.1.20"





/*
idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}*/

val kotliV = "1.3.70"

buildscript {

    repositories {
        jcenter()
        mavenCentral()
    }

    dependencies {
        classpath(kotlin("gradle-plugin", "1.3.70"))
        classpath(kotlin("serialization","1.3.70"))
        //     classpath("org.sonarsource.scanner.gradle:sonarqube-gradle-plugin:2.8")
    }


}


fun Project.versionOf(dependency: String): String = extra.get("${dependency}_version") as String



plugins {

    id("org.springframework.boot") version "2.1.13.RELEASE"
    id("io.spring.dependency-management") version "1.0.6.RELEASE"
//    id("org.asciidoctor.convert") version "1.5.8"
    id("org.gradle.kotlin.kotlin-dsl") version "1.3.3"
    kotlin("jvm") version "1.3.70"
    kotlin("plugin.spring")  version "1.3.70"
    kotlin("plugin.allopen")  version "1.3.70"
    kotlin("plugin.jpa")  version "1.3.70"
    kotlin("plugin.serialization")  version "1.3.70"
    kotlin("kapt")  version "1.3.70"

    application
    java
    //id("org.gradle.kotlin.kotlin-dsl") version "1.3.3"
}


java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

application {
    group = "io.alexheld.mockserver"
    mainClassName = "io.ktor.server.netty.EngineMain"
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
    maven { url = uri("https://repo.spring.io/milestone") }
    maven { url = uri("https://repo.spring.io/snapshot") }
    maven { url = uri("https://dl.bintray.com/kotlin/ktor") }
}


dependencies {
    // ---

    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-jackson:$ktor_version")
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-host-common:$ktor_version")

    implementation("org.koin:koin-ktor:$koin_version")
    implementation("org.apache.logging.log4j:log4j-api-kotlin:1.0.0")
    implementation("org.apache.logging.log4j:log4j-core:2.11.0")

    // implementation("org.snakeyaml:snakeyaml-engine:2.1")

    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.10.3")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.10.3")

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:${property("kotlin_version")}")
    testImplementation("org.junit.jupiter:junit-jupiter:5.6.0")
    testImplementation("org.amshove.kluent:kluent:$kluent_version")
    //testImplementation("io.ktor:ktor-server-test-host:$ktor_version")

    // TEST


    // ----- JAVA ------ //
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-batch")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-groovy-templates")
    implementation("org.springframework.boot:spring-boot-starter-integration")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    developmentOnly("org.springframework.boot:spring-boot-devtools")
    compileOnly("org.projectlombok:lombok")


    implementation("io.arrow-kt:arrow-core:${property("arrow_version")}")
    implementation ("io.arrow-kt:arrow-syntax:${property("arrow_version")}")
    kapt("io.arrow-kt:arrow-meta:${property("arrow_version")}")

    //implementation("com.vaadin:vaadin-spring-boot-starter")
    // implementation("com.microsoft.azure:azure-spring-boot-starter")


    // Annotation
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.projectlombok:lombok")


    // test //
  //  testImplementation("org.springframework.batch:spring-batch-test")
//    testImplementation("org.springframework.cloud:spring-cloud-starter-contract-stub-runner")
  //  testImplementation("org.springframework.cloud:spring-cloud-starter-contract-verifier")
   // testImplementation("org.springframework.integration:spring-integration-test")
    //testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
 /*   testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }*/

    // Support


}


kapt {
    useBuildCache = false
    this.useLightAnalysis = true
    this.strictMode = false
    correctErrorTypes = true

}

dependencyManagement {
    imports {
       // mavenBom("com.vaadin:vaadin-bom:${property("vaadinVersion")}")
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
        mavenBom("com.microsoft.azure:azure-spring-boot-bom:${property("azureVersion")}")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "13"
    }
}


tasks.wrapper.configure {
    this.gradleVersion = "6.2.2"
    this.distributionType = Wrapper.DistributionType.ALL
    this.distributionBase = org.gradle.api.tasks.wrapper.Wrapper.PathBase.GRADLE_USER_HOME
}
