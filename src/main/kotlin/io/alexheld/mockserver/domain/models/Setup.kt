package io.alexheld.mockserver.domain.models

import com.fasterxml.jackson.annotation.*
import io.alexheld.mockserver.domain.services.*
import org.bson.codecs.pojo.annotations.*
import org.litote.kmongo.*
import java.time.*

@BsonDiscriminator()
open class Setup(
    @BsonId
    var id: Id<Setup> = newId(),
    var timestamp: Instant? = Instant.now(),
    var request: Request?,
    var action: Action?
) {

    constructor() : this(newId(), Instant.now(), null, null)

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    constructor(id: String?, timestamp: Instant?, request: Request?, action: Action?, @JacksonInject gen: GenerationService) : this(
        gen.newId<Setup>(id),
        timestamp ?: gen.getTimestamp(),
        request,
        action
    )

}
