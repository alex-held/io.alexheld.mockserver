package io.alexheld.mockserver.web

import io.alexheld.mockserver.web.controllers.*
import io.ktor.application.*
import io.ktor.routing.*
import kotlinx.coroutines.*

fun Routing.setupAPI(controller: SetupController) {
    route("/setup") {

            get { controller.getAllSetups(call) }

            post { controller.createSetup(call) }

            delete("/{id}") { controller.deleteById(call) }
    }

}
