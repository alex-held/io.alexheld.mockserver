package io.alexheld.mockserver.domain.services

import io.alexheld.mockserver.domain.models.*
import io.alexheld.mockserver.domain.repositories.*
import io.alexheld.mockserver.logging.*
import io.alexheld.mockserver.logging.models.*
import io.ktor.application.*
import io.ktor.request.*
import org.apache.logging.log4j.kotlin.*


class SetupServiceImpl(private val repository: SetupRepository, private val logService: LogService, private val gen: GenerationService) : SetupService, Logging {


    override fun list(): List<Setup> {
        val setups = repository.list()
        val log = IdentifiableLog.generateNew(
            ApiCategory.Setup,
            LogMessageType.Setup_Created,
            OperationData(ApiOperation.List, setups.toMutableList()),
            gen
        )

        logService.add(log)
        return setups
    }

    override fun add(setup: Setup): Setup {

        val created = repository.add(setup)
        val log = IdentifiableLog.generateNew(
            ApiCategory.Setup,
            LogMessageType.Setup_Created,
            SetupCreatedData(created),
            gen
        )

        logService.add(log)
        return created
    }

    override fun delete(id: String): Setup? {
        val deleted = repository.delete(id)

        val log = try {
            IdentifiableLog.generateNew(
                ApiCategory.Setup,
                LogMessageType.Setup_Deleted, SetupDeletedData(deleted!!),
                gen
            )
        } catch (e: Exception) {
            IdentifiableLog.generateNew(
                ApiCategory.Setup,
                LogMessageType.Setup_Deleted, ExceptionData.fromException(e),
                gen
            )
        }
        logService.add(log)
        return deleted
    }

    override fun getMatchingSetup(call: ApplicationCall): Setup? {
        return repository.find { setup ->
            return@find setup.request != null
                    && setup.request?.method == call.request.httpMethod.value
                    && setup.request?.path == call.request.path()
        }
    }
}
