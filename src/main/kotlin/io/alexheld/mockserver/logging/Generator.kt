package io.alexheld.mockserver.logging

import java.time.*
import java.util.*

object Generator {

    var enableDebugGeneration: Boolean = false
    var defaultId: String = "00000000-0000-0000-0000-000000000000"
    var defaultTimestamp: Instant = Instant.EPOCH


    fun getId(): String {
        if (enableDebugGeneration)
            return defaultId
        return UUID.randomUUID().toString()
    }

    private fun getTimestamp(): Instant {
        if (enableDebugGeneration)
            return defaultTimestamp
        return Instant.now()
    }

    fun getTimestampString(): Instant = getTimestamp()

}
