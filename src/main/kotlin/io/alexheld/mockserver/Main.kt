package io.alexheld.mockserver

import io.alexheld.mockserver.config.*
import io.alexheld.mockserver.domain.repositories.*
import io.alexheld.mockserver.domain.services.*
import io.alexheld.mockserver.web.*
import io.alexheld.mockserver.web.ErrorResponse
import io.alexheld.mockserver.web.controllers.*
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.jackson.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

/**
 * Entry point of the application: main method that starts an embedded server using Netty,
 * processes the applweeication.conf file, interprets the command line args if available
 * and loads the application modules.
 */
fun main(args: Array<String>) : Unit {

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

//    val injector = Guice.createInjector(Core(this))
//    val ctrl = injector.getInstance(LoggingController::class.java)
//    val setupController = injector.getInstance(SetupController::class.java)
install(ContentNegotiation){
    jackson()
}
    install(CallLogging)
    install(StatusPages) {
//        exception(Exception::class.java) {
//            val errorResponse = ErrorResponse(mapOf("error" to listOf("detail", this.toString())))
//            context.respond(HttpStatusCode.InternalServerError, errorResponse)
//        }
    }

    install(Routing) {
        setupAPI(Instances.setupController)
    }
    install(Mock)

}


object Instances {
    val setupRepo = SetupRepositoryImpl()
    val setupService = SetupServiceImpl(setupRepo)
    val setupController = SetupController(setupService)
}
