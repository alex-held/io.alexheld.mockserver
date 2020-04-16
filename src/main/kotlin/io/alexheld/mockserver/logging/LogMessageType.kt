package io.alexheld.mockserver.logging

import io.alexheld.mockserver.logging.models.*
import kotlin.reflect.*


enum class LogMessageType {
    //RUNNABLE, TRACE, DEBUG, INFO, WARN, EXCEPTION, CLEARED, RETRIEVED, VERIFICATION, VERIFICATION_FAILED,
    Setup_Created {
        override val type: KClass<*> = SetupCreatedData::class
    },
    Setup_Deleted {
        override val type: KClass<*> = SetupDeletedData::class
    },
    Exception {
        override val type: KClass<*> = ExceptionData::class
    },
    Request_Received {
        override val type: KClass<*> = RequestReceivedData::class
    },
    Request_Matched {
        override val type: KClass<*> = RequestMatchedData::class
    },
    Action_Response {
        override val type: KClass<*> = ActionData::class
    },
    Operation {
        override val type: KClass<*> = OperationData::class
    };

    open val type: KClass<*> = LogMessageType::class
}
