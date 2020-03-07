package io.alexheld.mockserver

import io.alexheld.mockserver.config.*
import io.alexheld.mockserver.features.*
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.jackson.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.koin.ktor.ext.*

/**
 * Entry point of the application: main method that starts an embedded server using Netty,
 * processes the application.conf file, interprets the command line args if available
 * and loads the application modules.
 */
fun main(args: Array<String>): Unit {

    val env = applicationEngineEnvironment {

        module {
            module()
        }

        connector {
            host = "0.0.0.0"
            port = MockConfig.ADMIN_PORT
        }
        connector {
            host = "0.0.0.0"
            port = MockConfig.MOCK_PORT
        }

    }

    embeddedServer(Netty, environment = env).start(true)
}


fun Application.module() {

    install(Koin) {
        modules(ModulesConfig.setupModule)
    }
    install(ContentNegotiation) {
        jackson()
    }

    install(CallLogging)
    install(StatusPages)

    install(Routing) {
        api()
    }
    install(Mock)

}
