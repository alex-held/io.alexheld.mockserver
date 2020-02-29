package io.alexheld.mockserver

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Routing.setupApi(setupService: SetupService) {
    route("/setup") {

        get {
            val setups = setupService.list()
            call.respond(HttpStatusCode.OK, setups)
        }

        post {
            var setup = call.receive<Setup>()
            setup = setupService.add(setup)
            call.respond(HttpStatusCode.Created, setup)
        }

        delete("/{id}"){
            val id: Int = call.parameters["id"]?.toIntOrNull() ?: throw IllegalArgumentException("Missing id")

            when(val deleted = setupService.delete(id)){
                is Setup -> call.respond(HttpStatusCode.OK, deleted)
                else -> call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}
