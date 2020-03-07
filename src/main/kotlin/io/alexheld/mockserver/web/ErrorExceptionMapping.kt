package io.alexheld.mockserver.web

internal data class ErrorResponse(val errors: Map<String, List<String?>>)

object ErrorExceptionMapping
