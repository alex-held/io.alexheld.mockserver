package io.alexheld.mockserver.config

import io.alexheld.mockserver.web.*
import io.alexheld.mockserver.web.controllers.*
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.jackson.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.util.*
import org.koin.ktor.ext.*

const val SERVER_PORT = 8081

@KtorExperimentalAPI
@EngineAPI
fun setup(isCio: Boolean = true): BaseApplicationEngine {

    return server(if (isCio) CIO else Netty)
}

@KtorExperimentalAPI
@EngineAPI
fun server(
    engine: ApplicationEngineFactory<BaseApplicationEngine,
            out ApplicationEngine.Configuration>
): BaseApplicationEngine = embeddedServer(
    engine,
    port = SERVER_PORT,
    watchPaths = listOf("mainModule"),
    module = Application::mainModule
)

@KtorExperimentalAPI
fun Application.mainModule() {

    install(Koin) {
        modules(ModulesConfig.setupModule)
    }
    val setupController: SetupController by inject()

    install(CallLogging)
    install(ContentNegotiation) { jackson {} }
    install(StatusPages) {
        exception(Exception::class.java) {
            val errorResponse = ErrorResponse(mapOf("error" to listOf("detail", this.toString())))
            context.respond(HttpStatusCode.InternalServerError, errorResponse)
        }
    }

    install(Routing) {
        setupAPI(setupController)
    }
}

