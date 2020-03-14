package io.alexheld.mockserver.serialization

import org.snakeyaml.engine.v2.api.*
import java.io.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class StreamToStringWriter(val writer: StringWriter = StringWriter()) : StreamDataWriter {

    /**
     * Flushes this stream by writing any buffered output to the underlying stream.
     */
    override fun flush() {}


    override fun write(str: String?) = writer.write(str)

    override fun write(str: String?, off: Int, len: Int) = writer.write(str, off, len)

}
