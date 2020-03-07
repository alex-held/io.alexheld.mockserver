package io.alexheld.mockserver.logging

import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.databind.annotation.*
import io.alexheld.mockserver.domain.models.*
import io.alexheld.mockserver.serialization.*
import org.apache.logging.log4j.*
import org.apache.logging.log4j.kotlin.*
import java.text.*
import java.util.*


data class Log(

    @JsonSerialize(contentAs = String::class)
    val id: LogId,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val timestamp: Date,

    @JsonIgnore
    var level: Level = Level.ALL,

    var type: Log.LogMessageType? = null,

    val requests: MutableList<Request> = mutableListOf(),
    val arguments: MutableList<Any?> = mutableListOf(),

    var message: String? = null,

    val setup: Setup? = null,

    @JsonIgnore
    var throwable: Throwable? = null
) : MockSerializable, Logging {


    @JsonCreator
    constructor(
        type: LogMessageType? = null,
        level: Level = Level.INFO,
        requests: MutableList<Request> = mutableListOf(),
        arguments: MutableList<Any?> = mutableListOf(),
        message: String? = null,
        setup: Setup? = null
    ) : this(UUID.randomUUID().asLogId(), Date(), level, type, requests, arguments, message, setup)

    enum class LogMessageType {
        RUNNABLE,
        TRACE,
        DEBUG,
        INFO,
        WARN,
        EXCEPTION,
        CLEARED,
        RETRIEVED,
        Setup_Created,
        Setup_Deleted,
        Setup_Deletion_Failed,
        RECEIVED_REQUEST,
        Match,
        VERIFICATION,
        VERIFICATION_FAILED,
        SERVER_CONFIGURATION,
        Operation
    }

    public class Operations {
        companion object {
            public fun listSetups(): Log = Log(type = LogMessageType.Operation, message = "Listing Setups")
            public fun listLogs(): Log = Log(type = LogMessageType.Operation, message = "Listing Logs")
        }
    }

    companion object {

        public val dateFormat = SimpleDateFormat( "HH:mm:ss MM-dd")

        public fun setupCreated(setup: Setup): Log = Log(type = LogMessageType.Setup_Created, level = Level.INFO, setup = setup)
        public fun setupDeleted(setup: Setup?): Log {
            if (setup is Setup)
                return Log(type = LogMessageType.Setup_Deleted, level = Level.INFO, setup = setup)
            return Log(type = LogMessageType.Setup_Deletion_Failed, level = Level.INFO, message = "No Setup matched for deletion")
        }

        public fun requestReceived(request: Request): Log = Log(type = LogMessageType.RECEIVED_REQUEST, requests = mutableListOf(request), level = Level.INFO)
        public fun matched(request: Request, setup: Setup): Log = Log(type = LogMessageType.Match, requests = mutableListOf(request), level = Level.INFO, setup = setup)

    }
}



fun Log.format(): String {
    return when (type) {
        Log.LogMessageType.Match -> LogFormatter.formatLogMessage("returning response:{}for request:{}", arrayOf(setup!!.action, setup.request!!))
        Log.LogMessageType.Setup_Created -> LogFormatter.formatLogMessage("created setup:{}", arrayOf(setup!!))
        Log.LogMessageType.Setup_Deleted -> LogFormatter.formatLogMessage("deleted setup:{}", arrayOf(setup!!))
        Log.LogMessageType.Setup_Deletion_Failed -> LogFormatter.formatLogMessage("no matching setup to delete found for:{}", arrayOf(setup!!))
        Log.LogMessageType.RECEIVED_REQUEST -> LogFormatter.formatLogMessage("received request:{}", arrayOf(requests.first()))
        Log.LogMessageType.CLEARED -> message!!
        else -> ""
    }
}






fun Log.hasMessage(): Boolean = this.message.isNullOrBlank()

fun Log.withType(logType: Log.LogMessageType): Log {
    this.type = logType
    return this
}

fun Log.withLogLevel(logLevel: Level): Log {
    this.level = logLevel
    return this
}

fun Log.withMessageFormat(msg: String): Log {
    this.message = msg
    return this
}

fun Log.withThrowable(throwable: Throwable?): Log {
    this.throwable = throwable
    return this
}
