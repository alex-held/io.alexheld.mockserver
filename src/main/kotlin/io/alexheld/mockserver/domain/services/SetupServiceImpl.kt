package io.alexheld.mockserver.domain.services

import io.alexheld.mockserver.domain.models.*
import io.alexheld.mockserver.domain.repositories.*
import io.alexheld.mockserver.logging.*
import io.alexheld.mockserver.serialization.*
import io.ktor.application.*
import io.ktor.request.*
import org.apache.logging.log4j.kotlin.*
import java.time.*
import java.util.*


class SetupServiceImpl(private val repository: SetupRepository, private val logService: LogService) : SetupService, Logging {


    override fun list(): List<Setup> {
        logService.add { f ->
            f.create(
                LogMessageType.Operation, mapOf(
                    "operation" to "list",
                    "kind" to "Setup"
                )
            )
        }
        return repository.list()
    }

    override fun add(setup: Setup): Setup {
        val created = repository.add(setup)
        logService.add { f ->
            f.create(
                LogMessageType.Setup_Created, mapOf(
                    "setup" to created
                )
            )
        }
        return created
    }

    override fun delete(id: Int): Setup? {
        val deleted = repository.delete(id)
        if (deleted == null)
            logService.add { f -> f.create(LogMessageType.Setup_Deleted, mapOf()) }
        else
            logService.add { f ->
                f.create(
                    LogMessageType.Setup_Deleted, mapOf(
                        "setup" to deleted
                    )
                )
            }
        return deleted
    }

    override fun getMatchingSetup(call: ApplicationCall): Setup? {
        return repository.find { setup ->
            setup.request?.method == call.request.httpMethod.value
                    && setup.request.path == call.request.path()
        }
    }
}

object YamlLogFactory {

    var idOverride: String? = null
    val timestampOverride: Instant? = null

    fun create(type: LogMessageType, properties: Map<String, Any>): YamlLog {
        val log = YamlLog(properties)
        log.id = idOverride ?: UUID.randomUUID().toString()
        log.timestamp = timestampOverride?.toString() ?: Instant.now().toString()
        log.event = type
        return log
    }
}
