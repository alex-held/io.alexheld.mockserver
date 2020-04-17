package io.alexheld.mockserver.features

import io.alexheld.mockserver.responses.*

interface RequestScopedDataRepository {
    fun <T> add(key: String, value: T): Response
    fun delete(key: String): Response
    fun <T> get(key: String): GenericResponse<T>
}
