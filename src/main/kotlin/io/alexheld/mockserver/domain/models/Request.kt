package io.alexheld.mockserver.domain.models

import com.fasterxml.jackson.annotation.*
import io.alexheld.mockserver.serialization.*

public data class Request(
    @JsonProperty(required = false)
    val method: String? = null,

    @JsonProperty(required = false)
    val path: String? = null,

    @JsonProperty(required = false)
    val headers: MutableMap<String, MutableSet<String>>? = null,

    @JsonProperty(required = false)
    val cookies: MutableMap<String, String>? = null,

    @JsonProperty(required = false)
    val body: RequestBody? = null
) : MockSerializable  {
    constructor() : this(null, null, null, null, null)
}
