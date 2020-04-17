package io.alexheld.mockserver.features

import io.alexheld.mockserver.responses.*

class HttpDataRepository : RequestScopedDataRepository {

    override fun <T> add(key: String, value: T): Response {
        TODO("Not yet implemented")
    }

    override fun delete(key: String): Response {
        TODO("Not yet implemented")
    }

    override fun <T> get(key: String): GenericResponse<T> {
        TODO("Not yet implemented")
    }

}
