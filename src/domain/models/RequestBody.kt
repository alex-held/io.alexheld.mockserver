package io.alexheld.mockserver.domain.models

import com.fasterxml.jackson.annotation.*

data class RequestBody(
    @JsonProperty(required = false)
    val body: String? = null,

    @JsonProperty(required = false)
    val exact: Boolean? = null
)


