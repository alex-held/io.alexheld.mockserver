package io.alexheld.mockserver

import io.alexheld.mockserver.config.*
//import org.apache.logging.log4j.core.config.*
import org.http4k.core.*
import org.http4k.routing.*
import org.http4k.server.*
import org.slf4j.*

/**
 * Entry point of the application: main method that starts an embedded server using Netty,
 * processes the application.conf file, interprets the command line args if available
 * and loads the application modules.
 */
fun main() {
    val server = startApp(MOCK_ENVIRONMENT_DEV)
    server.block()
}

class Router(val setupHandler: SetupHandler) {

    private val contexts = RequestContexts()
    operator fun invoke(): RoutingHttpHandler = CatchHttpExceptions()
}


class SetupHandler

fun startApp(config: AppConfig): Http4kServer {

    // logging
   // Configurator.initialize(null, config.logConfig)
    val logger = LoggerFactory.getLogger("main")
   // val context = DbContext.connectOrCreate(config.db)


    val setupHandler = SetupHandler()

    val app = Router(
        setupHandler
    )()


    // server startup message
    logger.info("Starting server...")
    val server = app.asServer(Jetty(config.port))
    logger.info("Server started on port ${config.port}")
    return server
}


/*




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
*/
