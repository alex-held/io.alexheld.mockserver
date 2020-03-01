package io.alexheld.mockserver.logging

import arrow.fx.*
import io.alexheld.mockserver.domain.models.*
import org.slf4j.Logger
import org.slf4j.helpers.*
import java.lang.System.*
import java.util.logging.*
import org.slf4j.event.Level as LogLevel


fun MockServerLogger.LevelHelper.isEnabled(): Boolean = getProperty("io.alexheld.loglevel") != null
        && getProperty("java.util.logging.config.file") == null
        && getProperty("java.util.logging.config.class") == null


class MockServerLogger {

    constructor(httpStateHandler: HttpStateHandler) : this() {
        LevelHelper.httpStateHandler = httpStateHandler
    }

    private val logger: Logger? = null

    constructor() : this(Util.getCallingClass().resolveName)

    constructor(name: String) {
        logger?.debug("The calling classname = $name")
    }

    fun withHttpStateHandler(httpStateHandler: HttpStateHandler): MockServerLogger {
        LevelHelper.httpStateHandler = httpStateHandler
        return this
    }

    companion object LevelHelper {
        val Level: LogLevel = LogLevel.DEBUG
        var httpStateHandler: HttpStateHandler? = null
    }

    init {
        logger?.debug("Initializing the MockServerLogger..")

        try {
            if (LevelHelper.isEnabled()) {
                val config = "" +
                        "handlers=org.mockserver.logging.StandardOutConsoleHandler\n" +
                        "org.mockserver.logging.StandardOutConsoleHandler.level=ALL\n" +
                        "org.mockserver.logging.StandardOutConsoleHandler.formatter=java.util.logging.SimpleFormatter\n" +
                        "java.util.logging.SimpleFormatter.format=%1\$tF %1\$tT  %3\$s  %4\$s  %5\$s %6\$s%n\n" +
                        ".level=" + Level.toString() + "\n" +
                        "io.netty.handler.ssl.SslHandler.level=WARNING"

                val stream = config.byteInputStream(charset = Charsets.UTF_8)
                LogManager.getLogManager().readConfiguration(stream)
            }
        } catch (throwable: Exception) {
            MockServerLogger().logEvent(
                Log()
                    .withType(Log.LogMessageType.SERVER_CONFIGURATION)
                    .withLogLevel(LogLevel.ERROR)
                    .withMessageFormat("exception while configuring Java logging - " + throwable.localizedMessage)
                    .withThrowable(throwable)
            )
        }
    }

    fun logEvent(logEntry: Log) {
        if (logEntry.type === Log.LogMessageType.RECEIVED_REQUEST
            || logEntry.type === Log.LogMessageType.FORWARDED_REQUEST
            || isEnabled(logEntry.level)
        ) {
            if (httpStateHandler != null) {
                httpStateHandler?.log(logEntry)
            } else {
                writeToSystemOut(logger ?: NOPLogger.NOP_LOGGER, logEntry)
            }
        }
    }

    private fun writeToSystemOut(logger: Logger, logEntry: Log) {
        if (getProperty("io.alexheld.logging.disable.stdout") == null
            || !isEnabled(logEntry.level)
            || !logEntry.hasMessage()
        ) return

        when (logEntry.level) {
            LogLevel.ERROR -> logger.error(logEntry.message, logEntry.throwable)
            LogLevel.WARN -> logger.warn(logEntry.message, logEntry.throwable)
            LogLevel.INFO -> logger.info(logEntry.message, logEntry.throwable)
            LogLevel.DEBUG -> logger.debug(logEntry.message, logEntry.throwable)
            LogLevel.TRACE -> logger.trace(logEntry.message, logEntry.throwable)
        }
    }


    private fun isEnabled(level: LogLevel?): Boolean = Level >= level
    val name: String get() = logger?.name ?: "DEFAULT_MOCKSERVER_LOGGER"
}

typealias LogSetupCreated = (setup: Setup) -> IO<Log>
typealias LogSetupUpdated = (setup: Setup) -> IO<Log>
typealias LogSetupDeleted = (setup: Setup) -> IO<Log>

