package io.alexheld.mockserver.domain.models

import com.fasterxml.jackson.annotation.*
import io.alexheld.mockserver.serialization.*

data class Setup(
    @JsonProperty(required = false)
    var id: Int? = null,

    @JsonProperty(required = false)
    val request: Request? = null
) : MockSerializable{
    constructor() : this(null, null)
}
