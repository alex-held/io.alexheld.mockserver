package io.alexheld.mockserver.domain.models

import com.fasterxml.jackson.annotation.*
import io.alexheld.mockserver.serialization.*
import io.ktor.http.*

data class Setup(
    @JsonProperty(required = false)
    var id: Int? = null,

    @JsonProperty(required = false)
    val request: Request? = null,

    @JsonProperty()
    val action: Action = Action.Default
) : MockSerializable{
    constructor() : this(null, null)
}


public data class Action(

    @JsonProperty(required = false)
    val message: String? = null,

    @JsonProperty(required = false    )
    val statusCode: HttpStatusCode = HttpStatusCode.NotFound
) : MockSerializable  {

    companion object {
        public val Default get() = Action("DEFAULT RESPONSE MESSAGE", statusCode = HttpStatusCode.MultiStatus)
    }
}
