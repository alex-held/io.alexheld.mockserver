/*
package io.alexheld.mockserver.api

import io.alexheld.mockserver.domain.services.*
import io.alexheld.mockserver.logging.*
import io.alexheld.mockserver.logging.models.*
import org.apache.logging.log4j.core.appender.routing.*

fun Route.logs(logService: LogService) {

    route("/logs") {

        get {
            val setups = logService.list()
            call.respond(HttpStatusCode.OK, setups)
        }

        delete("/{id}") {
            val id = call.parameters["id"]

            if (id is String) {
                val deleted = logService.delete(id)
                if (deleted is IdentifiableLog<OperationData>)
                    call.respond(HttpStatusCode.OK, deleted)
            } else call.respond(HttpStatusCode.NoContent)
        }
    }

}
*/
