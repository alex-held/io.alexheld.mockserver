package io.alexheld.mockserver.features

import io.alexheld.mockserver.responses.*
import org.apache.logging.log4j.kotlin.*

/**
 * Provides human readable information via [System.out] and [System.err] to the user.
 */
class MockServerLogger<T> : ConsoleAdapter(), Logging {

    /**
     * Logs human readable error information of [error]  via [System.err] to the user
     */
    fun error(error: ErrorResponse) {
        val message = formatErrorMessage(error)
        error(message)
    }

    private fun formatErrorMessage(error: ErrorResponse): String {
        val sb = StringBuilder()
        sb.append(
            """
    > Error(s) occurred while executing the request.
    > ~~~
""".trimIndent())

        error.errors.forEach {
            sb.append(
                """
    > [${it.errorCode.code}] }[${it.errorCode.name}] 
    > ${it.message}
    > ~~~
""".trimIndent())
        }
        return sb.toString()
    }
}
