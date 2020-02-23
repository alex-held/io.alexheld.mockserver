var gradle_version = "6.2"
var kotlin_version = "1.3.61"
var ktor_version = "1.3.0"


plugins {
    id("org.jetbrains.kotlin.jvm")
    //id("org.jetbrains.gradle.plugin.idea-ext") version "0.7"
    java
}



tasks.register("shared"){
    org.jetbrains.kotlin.daemon.common.experimental.log.warning("Called shared mockserver-core")
}


project(":mockserver-core")
