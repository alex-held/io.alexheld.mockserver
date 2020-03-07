package io.alexheld.mockserver.logging

import java.util.*


val <T> Class<T>.resolveName: String get() = typeName.substringAfterLast('.')


fun UUID.asLogId(): LogId = LogId(this)


inline class LogId(val id: UUID) {
    companion object {
        public fun From(id: String): LogId  = From(UUID.fromString(id))
        public fun From(uuid: UUID): LogId = uuid.asLogId()
    }
    constructor() : this(UUID.randomUUID())
}
