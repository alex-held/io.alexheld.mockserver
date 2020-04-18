/*
package io.alexheld.mockserver.domain.services

import org.eclipse.jetty.websocket.api.*
import org.gradle.internal.impldep.com.google.api.client.http.*
import org.koin.core.*

class MockService : KoinComponent {

    private val setupService: SetupService by inject()

    suspend fun executeMockPipeline(context: PipelineContext<Unit, ApplicationCall>) {

        println("[MOCK] Executing Admin Pipeline")

        val attributes = context.call.attributes.allKeys


        val setup = setupService.getMatchingSetup(context.call)

        if (setup != null) {
            context.call.respond(HttpStatusCodes.STATUS_CODE_OK, setup)
        } else {
            context.call.respondText("NO MATCHING SETUP CONFIGURED", contentType = ContentType.Text.Plain, status = HttpStatusCode.NotFound)
        }
    }

}

*/
