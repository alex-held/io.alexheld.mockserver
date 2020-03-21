package io.alexheld.mockserver.logging

import WithContentMap
import io.alexheld.mockserver.domain.models.*
import io.alexheld.mockserver.serialization.*
import org.gradle.internal.impldep.software.amazon.ion.*
import org.gradle.kotlin.dsl.*
import java.time.*
import kotlin.properties.*
import kotlin.reflect.*


class ContentBind<T : WithContentMap> : ReadWriteProperty<ContentMapBase, T> {

    override operator fun getValue(thisRef: ContentMapBase, property: KProperty<*>): T {

        val propertyName = property.name
        val value = thisRef.content[propertyName]


        if (value == null) throw NullValueException("Property '$propertyName' must not be <null>! Value=${value?.toString() ?: "<null>"}")

        else if (MutableMap::class.isInstance(value)) {
            val propertyType = (thisRef::class.java.declaredMethods[1].genericReturnType as Class<*>)
            val ctor = propertyType.getConstructor(MutableMap::class.java)
            val instance = ctor.newInstance(thisRef.content)
            if (propertyType.kotlin.isInstance(instance))
                return instance as T
        }

        return try {
            val value = thisRef.content[propertyName] as T
            return value
        } catch (e: Exception) {
            throw e
        }
    }


    override operator fun setValue(thisRef: ContentMapBase, property: KProperty<*>, value: T) {
        val propertyValue = property.name
        val content = value.content
        thisRef.content[propertyValue] = content
    }

}

class SetupCreatedLog : LogDTO {

    var setup: Setup by ContentBind<Setup>()

    constructor(content: MutableMap<String, Any?> = mutableMapOf()) : super(content)
    constructor(id: String, timestamp: Instant, content: MutableMap<String, Any?> = mutableMapOf()) : super(id, timestamp, LogMessageType.Setup_Created, content)
    constructor(id: String, timestamp: Instant, setup: Setup) : this(id, timestamp, mutableMapOf("setup" to setup))

}


/*
class RequestMatchedModelLog : io.alexheld.mockserver.logging.LogDTO {

    val events: MutableList<Any> by content

    var requestReceived: Request
        get() = events[0] as Request
        set(value) { events[0] = value}

    var setup: Setup
        get() = events[1] as Setup
        set(value) { events[1] = value}

    var action: Action
        get() = events[2] as Action
        set(value) { events[2] = value}


    constructor(content: MutableMap<String, Any> = mutableMapOf()) : super(content)
    constructor(requestReceived: Request, setup: Setup, action: Action) : this(
        mutableMapOf(
            "events" to mutableListOf(Pair("requestReceived", requestReceived), Pair("setup", setup), Pair("action", action))
        )
    )
}*/


class OperationLog : LogDTO {

    enum class Operation {
        List,
        Delete,
        Inspect
    }

    enum class Entity {
        Log,
        Request,
        Setup
    }

    var operation: Operation by content
    var entity: Entity by content
    var results: MutableList<Any>?
        get() = content["results"] as MutableList<Any>?
        set(value) {
            if (value is MutableList<Any>) content["results"] = value
        }

    constructor(content: MutableMap<String, Any?> = mutableMapOf()) : super(content)

    constructor(operation: Operation, entity: Entity, content: MutableMap<String, Any?> = mutableMapOf()) : this(
        content.appendWith(
            mapOf(
                "operation" to operation,
                "entity" to entity
            )
        )
    )

    constructor(operation: Operation, entity: Entity, results: List<Any?>) : this(
        operation, entity, mutableMapOf(
            "results" to results
        )
    )


}
