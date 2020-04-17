package io.alexheld.mockserver.domain.services

import io.alexheld.mockserver.domain.models.*
import io.alexheld.mockserver.domain.repositories.*
import io.alexheld.mockserver.logging.*
import io.alexheld.mockserver.logging.models.*
import io.alexheld.mockserver.responses.*
import io.ktor.application.*
import io.ktor.request.*
import org.apache.logging.log4j.kotlin.*


class SetupServiceImpl(private val repository: SetupRepository, private val logService: LogService, private val gen: GenerationService) : SetupService, Logging {

    override fun list(): List<Setup> {
        val setups = repository.list()
        logService.addNew { it.createOperation(ApiCategory.Setup, OperationData(ApiOperation.List, setups)) }
        return setups
    }

    override fun add(setup: Setup): Setup {
        val created = repository.add(setup)
        logService.addNew { it.createEvent(ApiCategory.Setup, LogMessageType.Setup_Created, SetupCreatedData(setup)) }
        return created
    }

    override fun delete(id: String): GenericResponse<Setup> {
        val deleted = repository.delete(id)

        try {
            logService.addNew { it.createEvent(ApiCategory.Setup, LogMessageType.Setup_Deleted, SetupDeletedData(deleted.data.orNull()!!)) }
            return deleted
        }catch (e: Exception){
            logService.addNew { it.createEvent(ApiCategory.Setup, LogMessageType.Exception, ExceptionData(e)) }
            return deleted
        }


    }

    override fun getMatchingSetup(call: ApplicationCall): Setup? {
        return repository.find { setup ->
            return@find setup.request != null
                    && setup.request?.method == call.request.httpMethod.value
                    && setup.request?.path == call.request.path()
        }
    }
}
