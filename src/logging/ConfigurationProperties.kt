@file:Suppress("UNCHECKED_CAST")

package io.alexheld.mockserver.logging

import io.netty.util.NettyRuntime.*
import io.netty.util.internal.SystemPropertyUtil.*
import kotlinx.atomicfu.*
import java.lang.Runtime.*

fun <T> ConfigurationProperties.Options.Value(): T {
    return this.value as T
}

class ConfigurationProperties(private val options: Options) {

    enum class Options(val key: String, private val default: Any, var value: Any = default) {

        REBUILD_KEY_STORE("REBUILD_KEY_STORE", atomic(false)),
        REBUILD_SERVER_KEY_STORE("REBUILD_SERVER_KEY_STORE", atomic(false)),

        MOCKSERVER_INITIALIZATION_CLASS("MOCKSERVER_INITIALIZATION_CLASS", "mockserver.initializationClass"),
        MOCKSERVER_INITIALIZATION_JSON_PATH("MOCKSERVER_INITIALIZATION_JSON_PATH", "mockserver.initializationJsonPath"),
        MOCKSERVER_WATCH_INITIALIZATION_JSON("MOCKSERVER_WATCH_INITIALIZATION_JSON", "mockserver.watchInitializationJson"),
        MOCKSERVER_PERSISTED_EXPECTATIONS_PATH("MOCKSERVER_PERSISTED_EXPECTATIONS_PATH", "persistedExpectations.json"),

        MOCKSERVER_LOCAL_BOUND_IP("mockserver.localBoundIP", ""),
        DEFAULT_LOG_LEVEL("mockserver.logLevel", ch.qos.logback.classic.Level.INFO),
        MOCKSERVER_MAX_LOG_ENTRIES("mockserver.maxLogEntries", 5000),
        MOCKSERVER_MAX_EXPECTATIONS("mockserver.maxExpectations", 5000),
        PROPERTY_FILE("mockserver.propertyFile", System.getenv("MOCKSERVER_PROPERTY_FILE") ?: "mockserver.properties"),

        MOCKSERVER_PERSIST_EXPECTATIONS("MOCKSERVER_PERSIST_EXPECTATIONS", false),
        MOCKSERVER_METRICS_ENABLED("mockserver.metricsEnabled", false),
        MOCKSERVER_DISABLE_SYSTEM_OUT("MOCKSERVER_DISABLE_SYSTEM_OUT", false),
        MOCKSERVER_ACTION_HANDLER_THREAD_COUNT("mockserver.actionHandlerThreadCount", 20.coerceAtLeast(getRuntime().availableProcessors())),
        MOCKSERVER_NIO_EVENT_LOOP_THREAD_COUNT("mockserver.nioEventLoopThreadCount", 35.coerceAtLeast(getInt("io.netty.eventLoopThreads", availableProcessors()))),
    }

    private var slf4jOrJavaLoggerToJavaLoggerLevelMapping: MutableMap<String, String> = mutableMapOf(
        Pair("TRACE", "FINEST"),
        Pair("DEBUG", "FINE"),
        Pair("INFO", "INFO"),
        Pair("WARN", "WARNING"),
        Pair("ERROR", "SEVERE"),
        Pair("FINEST", "FINEST"),
        Pair("FINE", "FINE"),
        Pair("WARNING", "WARNING"),
        Pair("SEVERE", "SEVERE"),
        Pair("OFF", "OFF")
    )

    private var slf4jOrJavaLoggerToSLF4JLevelMapping: MutableMap<String, String> = mutableMapOf(
        Pair("FINEST", "TRACE"),
        Pair("FINE", "DEBUG"),
        Pair("INFO", "INFO"),
        Pair("WARNING", "WARN"),
        Pair("SEVERE", "ERROR"),
        Pair("TRACE", "TRACE"),
        Pair("DEBUG", "DEBUG"),
        Pair("WARN", "WARN"),
        Pair("ERROR", "ERROR"),
        Pair("OFF", "ERROR")
    )

}
