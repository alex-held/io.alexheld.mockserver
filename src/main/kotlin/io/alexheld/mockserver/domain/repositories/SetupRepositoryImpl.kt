package io.alexheld.mockserver.domain.repositories

import io.alexheld.mockserver.domain.models.*
import io.alexheld.mockserver.domain.services.*
import io.alexheld.mockserver.responses.*


class SetupRepositoryImpl(private val gen: GenerationService) : SetupRepository {

    private val setups: MutableMap<String, Setup> = mutableMapOf()

    override fun list(): List<Setup> {
        return setups.values.toList()
    }

    override fun add(setup: Setup): Setup {
        setup.id = gen.newId()
        setups[setup.id.toString()] = setup
        return setup
    }

    override fun delete(id: String): GenericResponse<Setup> {
        val error = OperationFailedErrorResponse("No setup with id $id was found. Could not delete setup.")
        val deleted = setups.remove(id) ?: return GenericErrorResponse(error)
        return GenericOkResponse(deleted)
    }

    override fun find(predicate: (Setup) -> Boolean): Setup? {
        return setups.values.firstOrNull(predicate)
    }
}
