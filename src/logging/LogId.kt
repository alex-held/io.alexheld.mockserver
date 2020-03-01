package io.alexheld.mockserver.logging

import java.util.*


val <T> Class<T>.resolveName: String get() = typeName.substringAfterLast('.')


fun UUID.asLogId(): LogId = LogId(this)


inline class LogId(val id: UUID) {
    constructor() : this(UUID.randomUUID())
}
