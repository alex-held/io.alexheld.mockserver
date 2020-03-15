package io.alexheld.mockserver.serialization.yaml

import org.snakeyaml.engine.v2.api.*
import org.snakeyaml.engine.v2.nodes.*

class MockServerConstructor : ConstructNode {

    /**
     * Construct a Java instance with all the properties injected when it is possible.
     *
     * @param node composed Node
     * @return a complete Java instance or empty collection instance if it is recursive
     */
    override fun construct(node: Node?): Any {
        val scalar = node as ScalarNode
        return scalar.value
    }

}




