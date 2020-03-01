package io.alexheld.mockserver.web.controllers

import io.alexheld.mockserver.domain.models.*
import io.alexheld.mockserver.domain.services.*
import io.alexheld.mockserver.logging.*
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*

class SetupController(private val service: SetupService, val stateHandler: HttpStateHandler) {

    suspend fun getAllSetups(call: ApplicationCall) {
        val setups = service.list()
        call.respond(HttpStatusCode.OK, setups)
    }

    suspend fun createSetup(call: ApplicationCall) {
        var setup = call.receive<Setup>()
        setup = service.add(setup)
        call.respond(HttpStatusCode.Created, setup)
    }

    suspend fun deleteById(call: ApplicationCall) {
        val id: Int = call.parameters["id"]?.toIntOrNull() ?: throw IllegalArgumentException("Missing id")

        when (val deleted = service.delete(id)) {
            is Setup -> call.respond(HttpStatusCode.OK, deleted)
            else -> call.respond(HttpStatusCode.NoContent)
        }
    }

}
