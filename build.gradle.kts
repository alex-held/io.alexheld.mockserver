val koin_version: String by project
val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val kluent_version: String by project


buildscript {
    repositories {
        jcenter()
        mavenCentral()
    }

    dependencies {
        classpath(kotlin("gradle-plugin", version = "1.3.61"))
   //     classpath("org.sonarsource.scanner.gradle:sonarqube-gradle-plugin:2.8")
    }
}

//apply(plugin = "org.sonarqube")
plugins {
    idea
    id("org.jetbrains.kotlin.jvm") version "1.3.70"
    id("org.gradle.kotlin.kotlin-dsl") version "1.3.4"
    id("org.jetbrains.intellij") version "0.4.16"
   // id("org.jetbrains.dokka") version "0.10.1"
    application
    java
    //jacoco

}


idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

application {
    group = "io.alexheld.mockserver"
    mainClassName = "io.ktor.server.netty.EngineMain"
}

dependencies {
   // implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-jackson:$ktor_version")
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-host-common:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("org.koin:koin-ktor:$koin_version")
    implementation("org.apache.logging.log4j:log4j-api-kotlin:1.0.0")
  //  implementation("org.apache.logging.log4j:log4j-api:2.11.0")
    implementation("org.apache.logging.log4j:log4j-core:2.11.0")

    // TEST
    testImplementation("org.junit.jupiter:junit-jupiter:5.6.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.3.61")
   // testImplementation("io.kotlintest:kotlintest-runner-junit5:3.4.2")
    testImplementation("org.amshove.kluent:kluent:$kluent_version")
    testImplementation("io.ktor:ktor-server-test-host:$ktor_version")
}



tasks.withType<Test>() {
    useJUnitPlatform()
}




repositories {
    jcenter()
    mavenCentral()
    // mavenCentral()
    maven { url = uri("https://dl.bintray.com/kotlin/ktor") }

}
