package io.alexheld.mockserver.config

import io.alexheld.mockserver.api.*
import io.alexheld.mockserver.domain.services.*
import io.ktor.routing.*
import org.koin.ktor.ext.*

fun Route.api() {

    val setupService: SetupService by inject()
    val logService: LogService by inject()

    setups(setupService)
    logs(logService)
}

