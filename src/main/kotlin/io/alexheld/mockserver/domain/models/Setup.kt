package io.alexheld.mockserver.domain.models

import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.core.*
import com.fasterxml.jackson.databind.*
import io.alexheld.mockserver.logging.*
import java.time.*


class SetDeserializer(private val gen: GenerationService) : JsonDeserializer<Setup>() {


    data class SetupDesDto( var id: String?,
                            var timestamp: Instant?,
                            var request: Request? = null,
                            var action: Action? = null
    )

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext?): Setup {
        val dto = p.readValueAs(SetupDesDto::class.java)
        val setup = Setup(dto.id ?: gen.getId(), dto.timestamp ?: gen.getTimestamp(), dto.request, dto.action)
        return setup
    }

}


/*
@JsonDeserialize(builder = Setup.Builder::class)
*/
open class Setup(
// @JacksonInject("setupId", useInput = OptBoolean.TRUE)
    var id: String,
    var timestamp: Instant,
    var request: Request?,
    var action: Action?
)

{

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    constructor(id: String?, timestamp: Instant?, request: Request?, action: Action?, @JacksonInject gen: GenerationService) : this(
        id ?: gen.getId(),
        timestamp ?: gen.getTimestamp(),
        request,
        action
    )

    /*
    class Builder {
        var id: String = null
        lateinit var timestamp: Instant
        var request: Request? = null
        lateinit var action: Action

        fun withId(id: String): Builder{
            this.id = id
            return this
        }

        fun withTimestamp(timestamp: Instant): Builder{
            this.timestamp = timestamp
            return this
        }

        fun withRequest(request: Request?): Builder{
            this.request = request
            return this
        }
        fun withAction(action: Action): Builder{
            this.action = action
            return this
        }

        fun build(): Setup = Setup(id, timestamp, request, action)
    }*/
}
/*{
    companion object {

        @JsonCreator
        fun generate(@JsonProperty("request") request: Request?,@JsonProperty("action") action: Action, @JacksonInject gen: GenerationService): Setup {
            return Setup(gen.getId(), gen.getTimestamp(), request, action)
        }
    }
}


*/
