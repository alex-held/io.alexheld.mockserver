package io.alexheld.mockserver.web.controllers

import com.fasterxml.jackson.core.*
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.module.kotlin.*
import io.alexheld.mockserver.domain.models.*
import io.alexheld.mockserver.domain.services.*
import io.alexheld.mockserver.logging.*
import io.alexheld.mockserver.serialization.*
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import kotlinx.coroutines.*

class SetupController(private val service: SetupService) {

    suspend fun getAllSetups(call: ApplicationCall) {
        val setups = service.list()
        val json = jacksonObjectMapper()
            .writerWithDefaultPrettyPrinter()
            .with(JsonGenerator.Feature.IGNORE_UNKNOWN)
            .with(SerializationFeature.INDENT_OUTPUT)
            .writeValueAsString(setups)

        call.respondText(json, contentType = ContentType.Application.Json, status = HttpStatusCode.OK)
    }

    suspend fun createSetup(call: ApplicationCall) {
        var setup = call.receive<Setup>()
        setup = service.add(setup)

        call.respond(HttpStatusCode.Created, setup)
    }

    suspend fun deleteById(call: ApplicationCall) {
        val id: Int = call.parameters["id"]?.toIntOrNull() ?: throw IllegalArgumentException("Missing id")

        when (val deleted = service.delete(id)) {
            is Setup -> {

                val json = jacksonObjectMapper()
                    .writerWithDefaultPrettyPrinter()
                    .with(JsonGenerator.Feature.IGNORE_UNKNOWN)
                    .with(SerializationFeature.INDENT_OUTPUT)
                    .writeValueAsString(deleted)

                call.respondText(json, contentType = ContentType.Application.Json, status = HttpStatusCode.OK)

            }
            else -> call.respond(HttpStatusCode.NoContent)
        }

    }

}
