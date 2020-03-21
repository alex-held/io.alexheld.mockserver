
import io.alexheld.mockserver.logging.*
import java.time.*


interface WithContentMap {
    val content: MutableMap<String, Any?>}


interface WithLogMetadata : WithContentMap {

    var id: String
        get() = content["id"] as String
        set(value) { content["id"] = value }

    var timestamp: Instant
        get() = content["timestamp"] as Instant
        set(value) { content["timestamp"] = value}

    var type: LogMessageType
        get() = enumValueOf(content["type"] as String)
        set(value) { content["type"] = value.name }
}
