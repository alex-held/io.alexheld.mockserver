package io.alexheld.mockserver.logging

import org.slf4j.*
import org.slf4j.helpers.*

fun <T> Class<T>.resolveName(): String {
    return typeName.substringAfterLast('.')
}

class MockServerLogger(private val logger: Logger) {


    constructor(name: String = Util.getCallingClass().resolveName()) : this(LoggerFactory.getLogger(name)) {
        logger.debug("The calling classname = $name")
    }

    val name: String get() = logger.name
}
