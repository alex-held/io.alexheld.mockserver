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
    Setup_Deletion_Failed {
        override val type: KClass<*> = SetupDeletionFailedData::class
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
/*


*/
/**
 *
 * Provides a way to create new instances of [LogDTO] using
 * recursive objects if it is required. (create Native Data Structure out of
 * Node Graph)
 *
 * *//*

interface LogBuilder {

    val type: KClass<*>

    fun createInstance(properties: Map<String, Any>, type: Class<*> = this.type.java): WithContentMap = type.kotlin.constructors.first { constructor ->

        if (this.type.java != type)
            throw Exception("Cannot create instance of ${type.simpleName}! Supported types are: [${this.type.simpleName}].")

        return@first if (constructor.parameters.size == 1) {
            println("Single parameter constructor found! Parameter=${constructor.parameters.first().name}; Type=${type.simpleName};")
            true
        } else throw UnsupportedOperationException("No viable constructor found! Type=${type.simpleName};")

    }.call(properties) as WithContentMap



    fun tryCreateInstance(properties: Map<String, Any>, type: Class<*>): WithContentMap? = try {
        createInstance(properties, type)
    } catch (e: Exception) {
        loggerOf(type).debug(e.localizedMessage)
        null
    }

}
*/
