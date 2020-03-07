package io.alexheld.mockserver.logging

import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.databind.annotation.*
import io.alexheld.mockserver.domain.models.*
import io.alexheld.mockserver.serialization.*
import org.slf4j.event.*
import java.util.*


@JsonInclude(JsonInclude.Include.NON_DEFAULT)
data class Log (

    @JsonSerialize(contentAs = String::class)
    val id: LogId,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val timestamp: Date,

    @JsonIgnore
    var level: Level? = null,

    @JsonIgnore
    var type: Log.LogMessageType? = null,

    val requests: MutableList<Request>? = null,

    var message: String? = null,

    val setup: Setup? = null,

    @JsonIgnore
    var throwable: Throwable? = null
) : MockSerializable {


    @JsonCreator
    constructor() : this(UUID.randomUUID().asLogId(), Date())

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
        Setup_Updated,
        Setup_Deleted,
        RECEIVED_REQUEST,
        EXPECTATION_RESPONSE,
        EXPECTATION_NOT_MATCHED_RESPONSE,
        EXPECTATION_MATCHED,
        EXPECTATION_NOT_MATCHED,
        VERIFICATION,
        VERIFICATION_FAILED,
        FORWARDED_REQUEST,
        TEMPLATE_GENERATED,
        SERVER_CONFIGURATION
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
