package io.alexheld.mockserver.logging

import org.slf4j.*
import org.slf4j.Logger
import org.slf4j.event.*
import org.slf4j.helpers.*
import java.lang.System.*
import java.util.logging.*
import org.slf4j.event.Level as LogLevel


fun MockServerLogger.LevelHelper.isEnabled(): Boolean = getProperty("io.alexheld.loglevel") != null
        && getProperty("java.util.logging.config.file") == null
        && getProperty("java.util.logging.config.class") == null


class MockServerLogger {


    /**
    TODO: Create a scope factory for the mock server logger, so that
    any class wo gets the Logger injected, the logger will use the classes' name
     **/
    private val defaultLogger get() = LoggerFactory.getLogger("DEFAULT_MOCKSERVER_LOGGER")
    private var logger: Logger = defaultLogger


    fun isEnabled(level: LogLevel?) = Level >= level
    val name: String get() = logger.name

    constructor(httpStateHandler: HttpStateHandler) : this() {
        LevelHelper.httpStateHandler = httpStateHandler
    }

    constructor(name: String = Util.getCallingClass().resolveName) {
        logger =  LoggerFactory.getLogger(name)
        logger.debug("The calling classname = $name")
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
        logger.debug("Initializing the MockServerLogger..")

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
                writeToSystemOut(logger, logEntry)
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
}
