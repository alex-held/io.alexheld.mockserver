package io.alexheld.mockserver.domain.models

import com.fasterxml.jackson.annotation.*

data class Setup(
    @JsonProperty(required = false) var id: Int? = null,

    @JsonProperty(required = false) val request: Request? = null
) {
    constructor() : this(null, null)
}
