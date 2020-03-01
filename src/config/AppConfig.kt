package io.alexheld.mockserver.config

import com.google.inject.*
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
import io.ktor.util.*
import java.lang.System.*


@KtorExperimentalAPI
@EngineAPI
fun setup(): BaseApplicationEngine {
    return server(CIO)
}

@KtorExperimentalAPI
@EngineAPI
fun server(engine: ApplicationEngineFactory<BaseApplicationEngine, out ApplicationEngine.Configuration>): BaseApplicationEngine = embeddedServer(
    engine, port = (getenv("PORT")?.toIntOrNull() ?: 8080), watchPaths = listOf("mainModule"), module = Application::mainModule
)

@KtorExperimentalAPI
fun Application.mainModule() {

    val injector = Guice.createInjector(Core(this))
    val ctrl = injector.getInstance(LoggingController::class.java)
    val setupController = injector.getInstance(SetupController::class.java)



/*
    val t by kodein().newInstance { LoggingController() }
    val test by kodein().newInstance { SetupServiceImpl(instance()) }
    val httpStateHandler by kodein().newInstance { HttpStateHandler(instance(), instance(), instance(), instance()) }

    httpStateHandler.log(Log(UUID.randomUUID().asLogId(), Date.from(Instant.EPOCH), message = "Hello World!"))
    val setupController: SetupController by inject()*/

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

