package io.alexheld.mockserver.serialization.yaml

import org.gradle.internal.impldep.com.google.gson.*
import org.gradle.internal.impldep.com.google.gson.annotations.*
import org.gradle.internal.impldep.com.google.gson.stream.*

@JsonAdapter(IntOrString.IntOrStringAdapter::class)
data class IntOrString(val isInteger: Boolean, val integerValue: Int?, val stringValue: String?) {

    constructor(value: String) : this(false, null, value)
    constructor(value: Int) : this(true, value, null)

    override fun toString(): String = if (isInteger) {
        integerValue.toString()
    } else {
        stringValue.toString()
    }


    companion object IntOrStringAdapter : TypeAdapter<IntOrString>() {

        override fun read(jsonReader: JsonReader?): IntOrString {
            return when (val nextToken = jsonReader!!.peek()) {
                JsonToken.NUMBER -> {
                    IntOrString(jsonReader.nextInt())
                }
                JsonToken.STRING -> {
                    IntOrString(jsonReader.nextString())
                }
                else -> {
                    throw IllegalStateException("Could not deserialize to IntOrString. Was " + nextToken)
                }
            }
        }

        override fun write(jsonWriter: JsonWriter, intOrString: IntOrString) {
            if (intOrString.isInteger) {
                jsonWriter.value(intOrString.integerValue)
            } else {
                jsonWriter.value(intOrString.stringValue)
            }
        }
    }
}
