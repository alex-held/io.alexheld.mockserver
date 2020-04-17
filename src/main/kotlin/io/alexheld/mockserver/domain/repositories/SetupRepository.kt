package io.alexheld.mockserver.domain.repositories

import io.alexheld.mockserver.domain.models.*

interface SetupRepository {
    fun list(): List<Setup>
    fun add(setup: Setup): Setup
    fun delete(id: String): Setup?
    fun find(predicate: (Setup) -> Boolean): Setup?
}
