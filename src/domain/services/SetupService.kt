package io.alexheld.mockserver.domain.services

import io.alexheld.mockserver.domain.models.*

interface SetupService {
    fun list(): List<Setup>
    fun add(setup: Setup): Setup
    fun delete(id: Int): Setup?
}
