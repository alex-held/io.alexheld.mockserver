package io.alexheld.mockserver.domain.services

import io.alexheld.mockserver.domain.models.*
import io.ktor.application.*

interface SetupService {
    fun list(): List<Setup>
    fun add(setup: Setup): Setup
    fun delete(id: Int): Setup?
    fun getMatchingSetup(call: ApplicationCall): Setup?
}
