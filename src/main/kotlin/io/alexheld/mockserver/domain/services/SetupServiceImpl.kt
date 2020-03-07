package io.alexheld.mockserver.domain.services

import io.alexheld.mockserver.domain.models.*
import io.alexheld.mockserver.domain.repositories.*
import io.ktor.application.*
import io.ktor.request.*


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

    override fun getMatchingSetup(call: ApplicationCall) : Setup? {
       return repository.find { setup ->
          setup.request?.method == call.request.httpMethod.value
                  && setup.request.path == call.request.path()
       }
    }
}
