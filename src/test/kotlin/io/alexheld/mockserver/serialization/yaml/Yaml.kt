/*
package io.alexheld.mockserver.serialization.yaml

import org.yaml.snakeyaml.*
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.*
import org.yaml.snakeyaml.introspector.*

object Yaml {

    fun dump(type: Class<*>, any: Any): String = dump(type)
    fun dump(any: Any): String = getMapper(any::class.java).dumpAsMap(any)

    fun getMapper(type: Class<*>): Yaml {

        val y = Yaml(
            buildConstructor(type),
            MockServerRepresentation(),
            buildDumpOptions()
        )

        val delegatingNodeDescription = TypeDescription(
            DelegatingNode::class.java,
            DelegatingNode::class.java
        )
        delegatingNodeDescription.setExcludes("properties", "children")
        y.addTypeDescription(delegatingNodeDescription)
        y.setBeanAccess(BeanAccess.FIELD)

        return y
    }


    fun buildConstructor(type: Class<*>): Constructor {
        val c = Constructor(type)
        c.isAllowDuplicateKeys = false
        val propertyUtils = c.propertyUtils
        propertyUtils.isSkipMissingProperties = true
        return c
    }


    fun buildDumpOptions(indent: Int = 2): DumperOptions {
        val opt = DumperOptions()
        opt.defaultFlowStyle = DumperOptions.FlowStyle.BLOCK
        opt.indicatorIndent = 2
        opt.indent = indent + 2

        return opt
    }
}
*/
