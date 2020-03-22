package io.alexheld.mockserver.logging.models

import io.alexheld.mockserver.domain.models.*
import io.alexheld.mockserver.logging.*


data class SetupCreatedData(var setup: Setup) : DataContainerData
data class SetupDeletedData(val setup: Setup) : DataContainerData
data class SetupDeletionFailedData(val exception: Exception, val message: String? = null) : DataContainerData


data class RequestReceivedData(val requestReceived: Request)

class LogDeletedData(deleted: MutableList<IdentifiableLog<*>> = mutableListOf()) : DataContainerData

data class RequestMatchedData(
    var setup: Setup,
    var requestReceived: Request,
    var action: Action
)

data class ActionData(val action: Setup)



data class OperationData1<T: Any>(
    val apiOperation: ApiOperation,
    val apiCategory: ApiCategory,
    val message: Operations.OperationMessages,
    val dataset: MutableList<T?>? = null
) : DataContainerData


data class OperationData(
    val apiOperation: ApiOperation,
    val apiCategory: ApiCategory,
    val message: Operations.OperationMessages,
    val dataset: MutableList<Any?>? = null
) : DataContainerData
