package io.alexheld.mockserver.domain.models

import com.fasterxml.jackson.annotation.*
import io.alexheld.mockserver.domain.services.*
import java.time.*

open class Setup(
    var id: String,
    var timestamp: Instant,
    var request: Request?,
    var action: Action?
) {

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    constructor(id: String?, timestamp: Instant?, request: Request?, action: Action?, @JacksonInject gen: GenerationService) : this(
        id ?: gen.getId(),
        timestamp ?: gen.getTimestamp(),
        request,
        action
    )
}
