package io.alexheld.mockserver.domain.services

import io.alexheld.mockserver.domain.repositories.*
import io.alexheld.mockserver.logging.*
import io.alexheld.mockserver.logging.models.*
import io.alexheld.mockserver.serialization.*
import java.time.*


class LogFactory(private val gen: GenerationService = GenerationServiceImpl()) {

    fun <T: DataContainerData> parse(id: String?, timestamp: String?, apiCategory: ApiCategory, type: LogMessageType, data: T): IdentifiableLog<T> {
        val log = IdentifiableLog(gen.newId(id),Instant.parse(timestamp) ?: gen.getTimestamp(), apiCategory, type, null, data)
        log.data = data
        log.type = data.getType()

        if (data is OperationData) log.operation = data.apiOperation
        return log
    }

    fun createOperation(category: ApiCategory, data: OperationData): IdentifiableLog<OperationData> {
        return IdentifiableLog(gen.newId(), gen.getTimestamp(), category, LogMessageType.Operation, data.apiOperation, data)
    }

    fun<T: DataContainerData> createEvent(category: ApiCategory, type: LogMessageType, data:T): IdentifiableLog<T> {
        return IdentifiableLog(gen.newId(), gen.getTimestamp(), category, type, null, data)
    }

    fun <T : DataContainerData> generateNew(apiCategory: ApiCategory, type: LogMessageType, data: T, gen: GenerationService): IdentifiableLog<T> {
        val id = gen.newId<IdentifiableLog<T>>()
        val time = gen.getTimestamp()
        val log = IdentifiableLog<T>(id, time, apiCategory, type, null, data)

        log.data = data
        log.type = data.getType()

        if (data is OperationData) log.operation = data.apiOperation
        return log
    }
}




class LogServiceImpl(private val logRepository: LogRepository, val logFactory: LogFactory) : LogService {

    override fun list(): IdentifiableLog<OperationData> = logRepository.list()
    override fun delete(id: String): IdentifiableLog<OperationData> = logRepository.delete(id)
    override fun <T : IdentifiableLog<*>> add(log: T): T = logRepository.add(log)
    override fun <T : IdentifiableLog<*>> addNew(factory: (LogFactory) -> T): T = logRepository.add(factory(logFactory))

    fun tryLog(log: IdentifiableLog<*>): String? {
        println(Yaml.serialize(log))
        return log.toString()
    }
}
