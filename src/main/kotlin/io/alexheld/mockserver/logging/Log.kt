package io.alexheld.mockserver.logging

import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.databind.annotation.*
import io.alexheld.mockserver.domain.models.*
import org.apache.logging.log4j.*
import org.gradle.internal.impldep.org.joda.time.*
import java.util.*


public enum class LogMessageType(public val type: String) {
    //RUNNABLE, TRACE, DEBUG, INFO, WARN, EXCEPTION, CLEARED, RETRIEVED,
    Setup_Created("Setup created"),
    Setup_Deleted("Setup deleted"),
    Setup_Deletion_Failed("Setup deletion failed"),
    Request_Received("Request received"),
    Request_Matched("Request matched"),
    Operation("SERVER_CONFIGURATION")
   /// VERIFICATION, VERIFICATION_FAILED, SERVER_CONFIGURATION,
}

@JsonSerialize
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties("id", "level",  "requests",  "arguments", "throwable")
public data class Log(

    val id: String,

    @JsonProperty(required = true)
    var message: String,

    @JsonProperty(required = true)
    var type: LogMessageType,

    var timestamp: Date,

    var level: Level = Level.ALL,

    var requests: MutableList<Request> = mutableListOf(),

    val arguments: MutableList<Any?> = mutableListOf(),

    @JsonProperty(required = false)
    var setup: Setup? = null,

    var throwable: Throwable? = null

)  {

    @JsonCreator
    constructor(message: String, type: LogMessageType) : this(UUID.randomUUID().toString(), message, type, DateTime.now(DateTimeZone.UTC).toDate(), Level.INFO,  mutableListOf(),
        mutableListOf())

    companion object {

        public fun setupCreated(setup: Setup): Log = Log("successfully created setup", LogMessageType.Setup_Created)
            .withSetup(setup)

        public fun setupDeleted(setup: Setup?): Log {
            if (setup is Setup)
                return Log("successfully deleted setup",type = LogMessageType.Setup_Deleted).withSetup(setup)
            return Log( "no Setup matched for deletion", type = LogMessageType.Setup_Deletion_Failed)
        }

        public fun requestReceived(request: Request): Log = Log("received incoming request", LogMessageType.Request_Received)
            .withRequests(mutableListOf(request))

        public fun matched(request: Request, setup: Setup): Log = Log("setup matched", LogMessageType.Request_Matched)
            .withRequests(mutableListOf(request))
            .withSetup(setup)


        public fun listSetups(): Log = Log("Listing Setups", LogMessageType.Operation)
        public fun listLogs(): Log = Log("Listing Logs", LogMessageType.Operation)
    }
}



fun Log.format(): String {
    return when (type) {
        LogMessageType.Request_Matched -> LogFormatter.formatLogMessage("returning response:{}for request:{}", arrayOf(setup!!.action, requests.first()))
        LogMessageType.Setup_Created -> LogFormatter.formatLogMessage("created setup:{}", arrayOf(setup!!))
        LogMessageType.Setup_Deleted -> LogFormatter.formatLogMessage("deleted setup:{}", arrayOf(setup!!))
        LogMessageType.Setup_Deletion_Failed -> LogFormatter.formatLogMessage("no matching setup to delete found for:{}", arrayOf(setup!!))
        LogMessageType.Request_Received -> LogFormatter.formatLogMessage("received request:{}", arrayOf(requests.first()))
        LogMessageType.Operation -> LogFormatter.formatLogMessage("operation", arrayOf())
      //  LogMessageType.CLEARED -> message!!
        //else -> ""
    }
}






fun Log.hasMessage(): Boolean = this.message.isBlank()


fun Log.withRequests(requests: MutableList<Request>): Log {
    this.requests = requests
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


fun Log.withThrowable(throwable: Throwable?): Log {
    this.throwable = throwable
    return this
}

fun Log.withDateTime(dateTime: DateTime): Any {
    this.timestamp = dateTime.toDate()
    return this
}
