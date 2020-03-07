package io.alexheld.mockserver.logging

import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.databind.annotation.*
import io.alexheld.mockserver.domain.models.*
import io.alexheld.mockserver.serialization.*
import org.apache.logging.log4j.*
import org.apache.logging.log4j.kotlin.*
import java.util.*


data class Log (

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
    constructor(type: LogMessageType? = null,
                level: Level = Level.INFO,
                requests: MutableList<Request> = mutableListOf(),
                arguments: MutableList<Any?> = mutableListOf(),
                message: String? = null,
                setup: Setup? = null) : this(UUID.randomUUID().asLogId(), Date(), level, type, requests, arguments ,message,setup)

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
        Setup_Matched_Response,
        Setup_Unmatched_Response,
        RECEIVED_REQUEST,
        Match,
        Match_Failed,
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

        public fun setupCreated(setup: Setup): Log = Log(type = LogMessageType.Setup_Created, level = Level.INFO, setup = setup)
        public fun setupDeleted(setup: Setup?): Log {
            if (setup is Setup)
                return Log(type = LogMessageType.Setup_Deleted, level = Level.INFO, setup = setup)
            return Log(type = LogMessageType.Setup_Deletion_Failed, level = Level.INFO, message = "No Setup matched for deletion")
        }

        public fun matched(request: Request, setup: Setup): Log = Log(type = LogMessageType.Match, requests = mutableListOf(request), level = Level.INFO, setup = setup)
        public fun matchFailed(request: Request, setup: Setup): Log = Log(type = LogMessageType.Match_Failed, requests = mutableListOf(request) ,level = Level.INFO, setup = setup)

    }
}


object Character {
    val NEW_LINE = System.getProperty("line.separator")
}

fun Log.format(): String {

    fun indentAndToString(vararg objects: Any?): List<StringBuilder> {
        val builders: MutableList<StringBuilder> = mutableListOf()

        for ((i, obj) in objects.withIndex()){
            builders[i]= StringBuilder(Character.NEW_LINE)
                .append(Character.NEW_LINE)
                .append(objects[i]?.toString()?.replace("(?m)^", "\t"))
                .append(Character.NEW_LINE)
        }

        return builders
    }


    val sb = StringBuilder()
    val formattedArguments = indentAndToString(arguments)
    val messageParts = message?.split("\\{}") ?: emptyList()
    for (messagePartIndex in messageParts.indices) {

        sb.append(messageParts[messagePartIndex])

        if (formattedArguments.isNotEmpty() && formattedArguments.size > messagePartIndex) sb.append(formattedArguments[messagePartIndex])

        if (messagePartIndex < messageParts.size - 1) {
            sb.append(Character.NEW_LINE)
            if (!messageParts[messagePartIndex + 1].startsWith(" ")) sb.append(" ")
        }
    }

    val logMessage = sb.toString()
    return logMessage
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
