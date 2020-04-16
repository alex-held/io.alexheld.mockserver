package io.alexheld.mockserver.domain.repositories

import io.alexheld.mockserver.logging.*
import io.alexheld.mockserver.logging.models.*

interface LogRepository {
    fun list(): IdentifiableLog<OperationData>
    fun delete(id: String): IdentifiableLog<OperationData>
    fun<T: IdentifiableLog<*>> add(log: T) : T
}
