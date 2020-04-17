package io.alexheld.mockserver.domain.services

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.util.pipeline.*
import org.koin.core.*

class MockService : KoinComponent {

    private val setupService: SetupService by inject()

    suspend fun executeMockPipeline(context: PipelineContext<Unit, ApplicationCall>) {

        println("[MOCK] Executing Admin Pipeline")

        val attributes = context.call.attributes.allKeys


        val setup = setupService.getMatchingSetup(context.call)

        if (setup != null) {
            context.call.respond(HttpStatusCode.OK, setup)
        } else {
            context.call.respondText("NO MATCHING SETUP CONFIGURED", contentType = ContentType.Text.Plain, status = HttpStatusCode.NotFound)
        }
    }

}

