package io.alexheld.mockserver.domain.services

import io.alexheld.mockserver.domain.models.*
import io.alexheld.mockserver.responses.*

interface SetupService {
    fun list(): List<Setup>
    fun add(setup: Setup): Setup
    fun delete(id: String): GenericResponse<Setup>
    fun getMatchingSetup(call: Setup): Setup?
}
