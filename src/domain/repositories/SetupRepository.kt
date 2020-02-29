package io.alexheld.mockserver.domain.repositories

import io.alexheld.mockserver.domain.models.*


interface SetupRepository {
    fun list(): List<Setup>
    fun add(setup: Setup): Setup
    fun delete(id: Int): Setup?
}

class SetupRepositoryImpl : SetupRepository {
    private val setups: MutableMap<Int, Setup> = mutableMapOf()

    override fun list(): List<Setup> {
        return setups.values.toList()
    }

    override fun add(setup: Setup): Setup {
        var id = setups.keys.max() ?: 0
        id++
        setup.id = id
        setups[id] = setup
        return setup
    }

    override fun delete(id: Int): Setup? {
        return setups.remove(id)
    }
}
