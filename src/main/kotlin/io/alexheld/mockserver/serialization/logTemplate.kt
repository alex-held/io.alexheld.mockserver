
import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.databind.annotation.*


public enum class NodeType {
    Container,
    Literal
}



@JsonSerialize
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public abstract class Node(
    val id: String,

    @JsonIgnore
    public val nodeType: NodeType,

    @JsonIgnore
    private val properties:  MutableMap<String, Any> = mutableMapOf()) {


    public fun addChild(child: Node)  {
        properties[child.id] = child
    }

    @JsonAnyGetter
    open fun any(): Map<String, Any> =  properties
}

@JsonSerialize
@JsonPropertyOrder(value = ["event", "id", "timestamp"])
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public open class ContainerNode(val type: String, id: String, val timestamp: String) : Node(id, NodeType.Container) { }


@JsonSerialize
@JsonPropertyOrder(value = ["event", "id", "timestamp"])
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public open class LogNode(type: String, id: String, timestamp: String) : ContainerNode(type, id, timestamp) { }


@JsonInclude(JsonInclude.Include.NON_EMPTY)
public open class LiteralNode(id: String) : Node(id, NodeType.Literal) { }



@JsonSerialize
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder(value = ["apiVersion", "kind", "type", "id", "timestamp"])
@JsonRootName("YAML")
public class YamlDocument(
        val apiVersion: String,
        val kind: String,
        type: String,
        id: String,
        timestamp: String) : ContainerNode(type,id, timestamp)  {}
