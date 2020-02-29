package io.alexheld.mockserver

import io.alexheld.mockserver.config.*
import io.ktor.server.engine.*
import io.ktor.util.*

/**
 * Entry point of the application: main method that starts an embedded server using Netty,
 * processes the application.conf file, interprets the command line args if available
 * and loads the application modules.
 */
@KtorExperimentalAPI
@EngineAPI
fun main() {
    setup().start(wait = true)
    //embeddedServer(CIO, commandLineEnvironment(args)).start()
}
