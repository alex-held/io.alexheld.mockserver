package io.alexheld.mockserver.domain.repositories

import io.alexheld.mockserver.domain.models.*
import java.util.*


class SetupRepositoryImpl : SetupRepository {

    private val setups: MutableMap<String, Setup> = mutableMapOf()

    override fun list(): List<Setup> {
        return setups.values.toList()
    }

    override fun add(setup: Setup): Setup {
        val id = UUID.randomUUID().toString()
        setup.id = id
        setups[id] = setup
        return setup
    }

    override fun delete(id: Int): Setup? {
        return setups.remove(id.toString())
    }

    override fun find(predicate: (Setup) -> Boolean): Setup? {
        return setups.values.firstOrNull(predicate)
    }
}
