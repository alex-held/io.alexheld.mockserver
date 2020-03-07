package io.alexheld.mockserver.domain.repositories

import io.alexheld.mockserver.domain.models.*
import java.util.function.*

interface SetupRepository {
    fun list(): List<Setup>
    fun add(setup: Setup): Setup
    fun delete(id: Int): Setup?
    fun find(predicate: (Setup) -> Boolean): Setup?
}
