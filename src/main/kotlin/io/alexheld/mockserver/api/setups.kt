package io.alexheld.mockserver.api

import io.alexheld.mockserver.domain.models.*
import io.alexheld.mockserver.domain.services.*
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*


fun Route.setups(setupService: SetupService) {

    route("/setups") {

        get {
            val setups = setupService.list()
            call.respond(HttpStatusCode.OK, setups)
        }

        post {
            var setup = call.receive<Setup>()
            setup = setupService.add(setup)
            call.respond(HttpStatusCode.Created, setup)
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()

            if (id is Int)
            {
                val deleted = setupService.delete(id)
                if (deleted is Setup)
                    call.respond(HttpStatusCode.OK, deleted)
            }
            else call.respond(HttpStatusCode.NoContent)

        }
    }

}
