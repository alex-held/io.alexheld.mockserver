package io.alexheld.mockserver.domain.repositories

import io.alexheld.mockserver.domain.models.*
import io.alexheld.mockserver.logging.*


class SetupRepositoryImpl(private val gen: GenerationService) : SetupRepository {

    private val setups: MutableMap<String, Setup> = mutableMapOf()

    override fun list(): List<Setup> {
        return setups.values.toList()
    }

    override fun add(setup: Setup): Setup {
        setup.id = gen.getId()
        setups[setup.id] = setup
        return setup
    }

    override fun delete(id: String): Setup? {
        return setups.remove(id)
    }

    override fun find(predicate: (Setup) -> Boolean): Setup? {
        return setups.values.firstOrNull(predicate)
    }
}
