package io.alexheld.mockserver

import com.fasterxml.jackson.databind.*
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.jackson.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.*


fun Application.main() {
    install(CallLogging)
    install(Koin) {
        modules(setupModule)
    }

    val setupService: SetupService by inject()

    install(StatusPages) {
        this.exception<Throwable> { e ->
            call.respondText(e.localizedMessage, ContentType.Text.Plain, HttpStatusCode.InternalServerError)
            throw e
        }
    }

    install(ContentNegotiation) {
        jackson() {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    install(Routing) {
        setupApi(setupService)
    }

}
