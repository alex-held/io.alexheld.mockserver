package io.alexheld.mockserver.domain.repositories

import io.alexheld.mockserver.logging.*
import io.alexheld.mockserver.logging.models.*


class LogRepositoryImpl : LogRepository {

    private val logs: MutableList<IdentifiableLog<*>> = mutableListOf()

    override fun list(): IdentifiableLog<OperationData> = IdentifiableLog
        .generateNew(ApiCategory.Log, LogMessageType.Operation,
            OperationData(ApiOperation.List, ApiCategory.Log, Operations.OperationMessages.List,
                logs.toMutableList()))


    override fun delete(id: String): IdentifiableLog<LogDeletedData> {
        val results: MutableList<IdentifiableLog<*>> = mutableListOf()
        this.logs.filterTo(results, {log -> log.id == id})

        val result = IdentifiableLog
            .generateNew(ApiCategory.Log, LogMessageType.Operation, LogDeletedData(results))

        return result
    }


    override fun<T: IdentifiableLog<*>> add(log: T) : T {
        logs.add(log)
        return log
    }
}
