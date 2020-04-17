package io.alexheld.mockserver.logging.models

import com.fasterxml.jackson.annotation.*
import io.alexheld.mockserver.domain.models.*
import io.alexheld.mockserver.logging.*


data class SetupCreatedData(var setup: Setup) : DataContainerData {
    override fun getType(): LogMessageType = LogMessageType.Setup_Created
}

data class SetupDeletedData(val setup: Setup) : DataContainerData {
    override fun getType(): LogMessageType = LogMessageType.Setup_Deleted
}

data class ExceptionData(val message: String? = null) : DataContainerData {

    @JsonIgnore
    var exception: Exception? = null

    constructor(exception: Exception) : this(exception.localizedMessage) {
        this.exception = exception
    }

    override fun getType(): LogMessageType = LogMessageType.Exception
}


data class RequestReceivedData(val requestReceived: Request) : DataContainerData {
    override fun getType(): LogMessageType = LogMessageType.Request_Received

}

data class RequestMatchedData(
    var requestReceived: Request,
    var setup: Setup,
    var actionExecuted: Action
) : DataContainerData {
    override fun getType(): LogMessageType = LogMessageType.Request_Matched

}

data class ActionData(val action: Action) : DataContainerData {
    override fun getType(): LogMessageType = LogMessageType.Action_Response
}


data class OperationData(
    @JsonIgnore
    val apiOperation: ApiOperation,
    val dataset: List<*>
) : DataContainerData {

    override fun getType(): LogMessageType = LogMessageType.Operation
}
