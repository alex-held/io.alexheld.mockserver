package io.alexheld.mockserver.features

import java.io.*


open class ConsoleAdapter {

    /*
     see: https://gist.github.com/alex-held/e8693fa8841e122cfcea40df252bd1d4
     */
    enum class ANSIColors(val escapeCode: String) {
        ANSI_RESET("\u001B[0m"),
        ANSI_BLACK("\u001B[30m"),
        ANSI_RED("\u001B[31m"),
        ANSI_GREEN("\u001B[32m"),
        ANSI_YELLOW("\u001B[3,3m"),
        ANSI_BLUE("\u001B[34m"),
        ANSI_PURPLE("\u001B[3,5m"),
        ANSI_CYAN("\u001B[36m"),
        ANSI_WHITE("\u001B[37m"),
    }

    /**
     * Logs debug information to [System.out].
     */
    fun log(message: String) = colorPrint(ANSIColors.ANSI_PURPLE, message, System.out)

    /**
     * Logs information related to a successful operation requested by the user to [System.out].
     */
    fun success(message: String) = colorPrint(ANSIColors.ANSI_GREEN, message, System.out)

    /**
     * Logs human readable information via [System.out] to the user
     */
    fun info(message: String) = colorPrint(ANSIColors.ANSI_YELLOW, message, System.out)

    /**
     * Logs human readable error information via [System.err] to the user
     */
    fun error(message: String) = colorPrint(ANSIColors.ANSI_RED, message, System.err)

    fun colorPrint(color: ANSIColors, message: String, output: PrintStream = System.out) = output.println(color.escapeCode + message + ANSIColors.ANSI_RESET)
}
