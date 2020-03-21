package io.alexheld.mockserver.logging

import WithContentMap
import org.apache.logging.log4j.kotlin.*
import org.gradle.internal.impldep.org.eclipse.jgit.errors.*
import kotlin.reflect.*


enum class LogMessageType : LogBuilder {

    //RUNNABLE, TRACE, DEBUG, INFO, WARN, EXCEPTION, CLEARED, RETRIEVED, VERIFICATION, VERIFICATION_FAILED,
    Setup_Created {
        override val type: KClass<*> = SetupCreatedLog::class
    },
    Setup_Deleted {
        override val type: KClass<*> = SetupDeletedLog::class
    },
    Setup_Deletion_Failed {
        override val type: KClass<*> = SetupDeletionFailedLog::class
    },
    Request_Received {
        override val type: KClass<*> = RequestReceivedLog::class
    },
    Request_Matched {
        override val type: KClass<*> = RequestMatchedLog::class
    },
    Action_Response {
        override val type: KClass<*> = ActionLog::class
    },
    Operation {
        override val type: KClass<*> = OperationLog::class
    };


}


/**
 *
 * Provides a way to create new instances of [LogDTO] using
 * recursive objects if it is required. (create Native Data Structure out of
 * Node Graph)
 *
 * @see LogDTP
 */
interface LogBuilder {

    val type: KClass<*>

    fun createInstance(properties: Map<String, Any>): WithContentMap = createInstance(properties, type.java)
    fun tryCreateInstance(properties: Map<String, Any>): WithContentMap? = tryCreateInstance(properties, type.java)

    /**
     * Builds a new instance of [LogDTO] using the provided [properties].
     * @return New instance of type [LogDTO]
     * @param[properties] The [properties] used to create the [LogDTO] instance.
     * @param[type] The [Class] of the [LogDTO] to instantiate
     * @throws ConfigInvalidException Thrown when [type] is not a valid [LogDTO] for [Enum.name]
     * @throws UnsupportedOperationException Thrown when [type] has no constructor with only parameter
     * @author Alexander Held
     */
    fun createInstance(properties: Map<String, Any>, type: Class<*> = this.type.java): WithContentMap = type.kotlin.constructors.first { constructor ->

        if (this.type.java != type)
            throw ConfigInvalidException("Cannot create instance of ${type.simpleName}! Supported types are: [${this.type.simpleName}].")

        return@first if (constructor.parameters.size == 1) {
            println("Single parameter constructor found! Parameter=${constructor.parameters.first().name}; Type=${type.simpleName};")
            true
        } else throw UnsupportedOperationException("No viable constructor found! Type=${type.simpleName};")

    }.call(properties) as WithContentMap

    /**
     *
     * Builds a new instance of [LogDTO] using the provided [properties].
     * @ return New instance of type [LogDTO] or [Nothing] if creating of [LogDTO] failed
     * @param [properties] The [properties] used to create the [LogDTO] instance.
     * @param [type] The [Class] of the [LogDTO] to instantiate
     * @author Alexander Held
     */
    fun tryCreateInstance(properties: Map<String, Any>, type: Class<*>): WithContentMap? = try {
        createInstance(properties, type)
    } catch (e: Exception) {
        loggerOf(type).debug(e.localizedMessage)
        null
    }

}

/**
 * Builds a new instance of [T] using the provided [properties].
 * @ return New instance of type [T]
 * @param [properties] The [properties] used to create the [T] instance.
 * @throws ConfigInvalidException Thrown when [type] is not a valid [LogDTO] for [Enum.name]
 * @throws UnsupportedOperationException Thrown when [type] has no constructor with only parameter
 * @author Alexander Held
 */
inline fun <reified T : WithContentMap> LogBuilder.createInstance(properties: Map<String, Any>): T = createInstance(properties, T::class.java) as T

/**
 * Builds a new instance of [T] using the provided [properties].
 * @param [properties] The [properties] used to create the [T] instance.
 * @ return A new instance of type [T] or [Void]
 * @author Alexander Held
 */
inline fun <reified T : WithContentMap> LogBuilder.tryCreateInstance(properties: Map<String, Any>): T? = tryCreateInstance(properties, T::class.java) as? T
