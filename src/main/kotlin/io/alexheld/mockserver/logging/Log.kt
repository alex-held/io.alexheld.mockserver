package io.alexheld.mockserver.logging

import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.databind.annotation.*
import io.alexheld.mockserver.domain.models.*
import io.alexheld.mockserver.serialization.*
import org.apache.logging.log4j.*
import org.gradle.internal.impldep.org.eclipse.jgit.errors.*
import java.time.*
import java.util.*


//@JsonSerialize(using = LogMessageTypeSerializer::class)
public enum class LogMessageType(public val type: String) {
    //RUNNABLE, TRACE, DEBUG, INFO, WARN, EXCEPTION, CLEARED, RETRIEVED,
    Setup_Created("Setup created"),
    Setup_Deleted("Setup deleted"),
    Setup_Deletion_Failed("Setup deletion failed"),
    Request_Received("Request received"),
    Request_Matched("Request matched"),
    Action_Response("Respond with HttpResponse"),
    Operation("SERVER_CONFIGURATION")
    /// VERIFICATION, VERIFICATION_FAILED, SERVER_CONFIGURATION,
}

@JsonSerialize
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties("id", "level", "requests", "arguments", "throwable")
public class Log(val properties: MutableMap<String, Any> = mutableMapOf()) {

    constructor(type: LogMessageType) : this(UUID.randomUUID().toString(), type, Instant.now())


    constructor(
        id: String, type: LogMessageType, timestamp: Instant = Instant.now(), loglevel: Level = Level.INFO, request: Request? = null, setup: Setup? = null, throwable:
        Throwable? = null
    ) : this(mutableMapOf()) {
        this.id = id
        this.event = type
        this.timestamp = timestamp
        this.level = loglevel
        if (request != null) this.request = request
        if (setup != null) this.setup = setup
        if (throwable != null) this.throwable = throwable
    }


    var level: Level = Level.ALL
    var event: LogMessageType by properties
    var timestamp: Instant by properties
    var request: Request by properties
    var setup: Setup by properties
    var throwable: Throwable by properties
    var id: String by properties


    companion object {

        public fun setupCreated(setup: Setup): YamlLog = YamlLog(
            LogMessageType.Setup_Created, mapOf(
                "setup" to setup
            )
        )

        public fun setupDeleted(setup: Setup): YamlLog = YamlLog(
            LogMessageType.Setup_Deleted, mapOf(
                "setup" to setup
            )
        )

        public fun setupDeletionFailed(): YamlLog = YamlLog(LogMessageType.Setup_Deletion_Failed)

        public fun requestReceived(request: Request): YamlLog = YamlLog(
            LogMessageType.Request_Received, mapOf(
                "request" to request
            )
        )

        public fun matched(request: Request, setup: Setup): YamlLog = YamlLog(
            LogMessageType.Request_Matched, mapOf(
                "events" to listOf(
                    YamlLog(
                        LogMessageType.Request_Received, mapOf(
                            "request" to request
                        )
                    ),
                    YamlLog(
                        LogMessageType.Request_Matched, mapOf(
                            "setup" to setup
                        )
                    )
                )
            )
        )


        public fun listSetups(): YamlLog = YamlLog(
            LogMessageType.Operation, mapOf(
                "operation" to "list",
                "kind" to "Setup"
            )
        )

        public fun listLogs(): YamlLog = YamlLog(
            LogMessageType.Operation, mapOf(
                "operation" to "list",
                "kind" to "Log"
            )
        )
    }
}

fun Log.toYaml(): YamlLog {
    return when (event) {
        LogMessageType.Setup_Created -> YamlLog.setupCreated(this)
        LogMessageType.Setup_Deleted -> YamlLog.setupDeleted(this)
        LogMessageType.Request_Matched -> YamlLog.requestMatched(this)
        LogMessageType.Request_Received -> YamlLog.requestReceived(this)
        LogMessageType.Action_Response -> YamlLog.actionResponse(this)
        else -> throw NotSupportedException("LogType $event is not yet supported")
    }
}


fun Log.withRequest(request: Request): Log {
    this.request = request
    return this
}

fun Log.withSetup(setup: Setup): Log {
    this.setup = setup
    return this
}

fun Log.withLogLevel(logLevel: Level): Log {
    this.level = logLevel
    return this
}

fun Log.withThrowable(throwable: Throwable): Log {
    this.throwable = throwable
    return this
}
