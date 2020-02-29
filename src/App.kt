package io.alexheld.mockserver

import com.fasterxml.jackson.databind.*
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.jackson.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import org.jetbrains.exposed.sql.*
import org.koin.dsl.*
import org.koin.ktor.ext.*

val module = module {
    single<SetupService>{SetupServiceImpl(get())}
    single<SetupRepository>{SetupRepositoryImpl()}
}

fun Application.main(){

    install(Koin) {
        Slf4jSqlLogger()
        modules(module)
    }

    val setupService: SetupService by inject()
    mainWithDependencies(setupService)
}
fun Application.mainWithDependencies(setupService: SetupService) {

    install(ContentNegotiation){
        jackson(){
            enable(SerializationFeature.INDENT_OUTPUT)
            disableDefaultTyping()
        }
    }

    install(StatusPages) {
        this.exception<Throwable> { e ->
            call.respondText(e.localizedMessage, ContentType.Text.Plain, HttpStatusCode.InternalServerError)
            throw e
        }
    }

    install(Routing){
        setupApi(setupService)
    }

    install(CallLogging)
    install(PartialContent)
    install(Locations)


}
