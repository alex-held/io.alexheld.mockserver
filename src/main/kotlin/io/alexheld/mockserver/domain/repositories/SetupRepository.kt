package io.alexheld.mockserver.domain.repositories

import io.alexheld.mockserver.domain.models.*
import io.alexheld.mockserver.responses.*

interface SetupRepository {
    fun list(): List<Setup>
    fun add(setup: Setup): Setup
    fun delete(id: String): GenericResponse<Setup>
    fun find(predicate: (Setup) -> Boolean): Setup?
}
