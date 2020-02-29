@file:Suppress("EXPERIMENTAL_API_USAGE")

package io.alexheld.mockserver

import io.ktor.server.cio.*
import io.ktor.server.engine.*

/**
 * Entry point of the application: main method that starts an embedded server using Netty,
 * processes the application.conf file, interprets the command line args if available
 * and loads the application modules.
 */
fun main(args: Array<String>) {
    embeddedServer(CIO, commandLineEnvironment(args)).start()
}
