package io.alexheld.mockserver.domain.services

import io.alexheld.mockserver.logging.*
import io.alexheld.mockserver.logging.models.*

interface LogService {

    fun list(): IdentifiableLog<OperationData>
    fun delete(id: String): IdentifiableLog<OperationData>
    fun <T : IdentifiableLog<*>> add(log: T): T
}
