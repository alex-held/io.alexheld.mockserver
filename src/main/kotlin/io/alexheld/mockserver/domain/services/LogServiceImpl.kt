package io.alexheld.mockserver.domain.services

import io.alexheld.mockserver.domain.repositories.*
import io.alexheld.mockserver.logging.*
import io.alexheld.mockserver.logging.models.*
import io.alexheld.mockserver.serialization.*

class LogServiceImpl(private val logRepository: LogRepository) : LogService {

    override fun list(): IdentifiableLog<OperationData> = logRepository.list()
    override fun delete(id: String): IdentifiableLog<LogDeletedData> = logRepository.delete(id)
    override fun<T: IdentifiableLog<*>> add(log: T): T = logRepository.add(log)

    fun tryLog(log: IdentifiableLog<*>): String? {
        println(Serializer.serialize(log))
        return log.toString()
    }
}
