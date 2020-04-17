package io.alexheld.mockserver

import io.alexheld.mockserver.config.*
import io.alexheld.mockserver.features.*
import io.alexheld.mockserver.responses.*
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.jackson.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.sessions.*
import io.ktor.util.*
import org.koin.ktor.ext.*
import java.util.*

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

    install(CallLogging) {

    }

    install(Sessions) {

    }

    attributes.put(AttributeKey("ErrErr"), ErrorResponse(OperationFailedErrorResponse("")))

    install(StatusPages) {
        exception<Throwable> { cause ->
            call.respondText("""
                ErrorMessage: ${cause.localizedMessage}
                StackTrace:
                ${cause.printStackTrace()}""".trimIndent())
        }
    }

    install(ContentNegotiation) {
        jackson()
    }

    install(CallId) {
        this.generate {
            val method = it.request.httpMethod.value
            val path = it.request.path()

            return@generate "$method!!$path!!${UUID.randomUUID()}"
        }
    }

    install(ErrorResponseHandler) {
        this.message = "SLF4J: Class path contains multiple SLF4J bindings."
    }

    install(Routing) {
        api()
    }
    install(Mock)

}
