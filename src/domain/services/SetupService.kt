package io.alexheld.mockserver.domain.services

import io.alexheld.mockserver.domain.models.*
import io.alexheld.mockserver.domain.repositories.*


interface SetupService {
    fun list(): List<Setup>
    fun add(setup: Setup): Setup
    fun delete(id: Int): Setup?
}

class SetupServiceImpl(private val repository: SetupRepository) : SetupService {

    override fun list(): List<Setup> {
        return repository.list()
    }

    override fun add(setup: Setup): Setup {
        return repository.add(setup)
    }

    override fun delete(id: Int): Setup? {
        return repository.delete(id)
    }
}
