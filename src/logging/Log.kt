package io.alexheld.mockserver.logging

import ch.qos.logback.classic.*
import org.joda.time.*
import java.util.*

public data class Log(
    val id: UUID = UUID.randomUUID(),
    val level: Level = Level.DEBUG,
    val timestamp: DateTime = DateTime.now()
) {

    public val date: String = DateTime.now().toString("yyyy-MM-dd HH:mm:ss")

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
