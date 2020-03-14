package io.alexheld.mockserver.domain.services

import io.alexheld.mockserver.domain.models.*
import io.alexheld.mockserver.domain.repositories.*
import io.alexheld.mockserver.serialization.*
import io.ktor.application.*
import io.ktor.request.*
import org.apache.logging.log4j.kotlin.*


class SetupServiceImpl(private val repository: SetupRepository, private val logService: LogService) : SetupService, Logging {


    override fun list(): List<Setup> {
        logService.add(Log.listSetups())
        return repository.list()
    }

    override fun add(setup: Setup): Setup {
        val created = repository.add(setup)
        logService.add(Log.setupCreated(created))
        return created
    }

    override fun delete(id: Int): Setup? {
        val deleted = repository.delete(id)
        if (deleted == null) logService.add(Log.setupDeletionFailed())
        else logService.add(Log.setupDeleted(deleted))
        return deleted
    }

    override fun getMatchingSetup(call: ApplicationCall): Setup? {
        return repository.find { setup ->
            setup.request?.method == call.request.httpMethod.value
                    && setup.request.path == call.request.path()
        }
    }
}
