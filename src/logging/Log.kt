package io.alexheld.mockserver.logging

import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.databind.annotation.*
import io.alexheld.mockserver.domain.models.*
import org.slf4j.event.*
import java.util.*


@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public data class Log(

    @JsonSerialize(contentAs = String::class)
    val id: UUID,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val timestamp: Date,

    @JsonIgnore
    val level: Level? = null,

    @JsonIgnore
    val type: LogMessageType? = null,

    val requests: MutableList<Request>? = null,

    val setup: Setup? = null
) {
    @JsonCreator
    constructor() : this(UUID.randomUUID(), Date())

    enum class LogMessageType {
        RUNNABLE,
        TRACE,
        DEBUG,
        INFO,
        WARN,
        EXCEPTION,
        CLEARED,
        RETRIEVED,
        UPDATED_EXPECTATION,
        CREATED_EXPECTATION,
        REMOVED_EXPECTATION,
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
