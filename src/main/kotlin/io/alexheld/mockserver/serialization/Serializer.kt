package io.alexheld.mockserver.serialization

import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.core.*
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.deser.*
import com.fasterxml.jackson.databind.jsontype.*
import com.fasterxml.jackson.module.kotlin.*


class PlainTextProblemHandler : DeserializationProblemHandler() {


    /**
     * Deprecated variant of
     * [ ][.handleUnexpectedToken]
     */
    override fun handleUnexpectedToken(ctxt: DeserializationContext?, targetType: Class<*>?, t: JsonToken?, p: JsonParser?, failureMsg: String?): Any {
        return super.handleUnexpectedToken(ctxt, targetType, t, p, failureMsg)
    }

    /**
     * Method called when a property name from input cannot be converted to a
     * non-Java-String key type (passed as `rawKeyType`) due to format problem.
     * Handler may choose to do one of 3 things:
     *
     *  * Indicate it does not know what to do by returning [.NOT_HANDLED]
     *
     *  * Throw a [IOException] to indicate specific fail message (instead of
     * standard exception caller would throw
     *
     *  * Return actual key value to use as replacement, and continue processing.
     *
     *
     *
     * @param failureMsg Message that will be used by caller (by calling
     * [DeserializationContext.weirdKeyException])
     * to indicate type of failure unless handler produces key to use
     *
     * @return Either [.NOT_HANDLED] to indicate that handler does not know
     * what to do (and exception may be thrown), or value to use as key (possibly
     * `null`
     *
     * @since 2.8
     */
    override fun handleWeirdKey(ctxt: DeserializationContext?, rawKeyType: Class<*>?, keyValue: String?, failureMsg: String?): Any {
        return super.handleWeirdKey(ctxt, rawKeyType, keyValue, failureMsg)
    }

    /**
     * Method called when a JSON Object property with an unrecognized
     * name is encountered.
     * Content (supposedly) matching the property are accessible via
     * parser that can be obtained from passed deserialization context.
     * Handler can also choose to skip the content; if so, it MUST return
     * true to indicate it did handle property successfully.
     * Skipping is usually done like so:
     * <pre>
     * parser.skipChildren();
    </pre> *
     *
     *
     * Note: [com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES])
     * takes effect only **after** handler is called, and only
     * if handler did **not** handle the problem.
     *
     * @param beanOrClass Either bean instance being deserialized (if one
     * has been instantiated so far); or Class that indicates type that
     * will be instantiated (if no instantiation done yet: for example
     * when bean uses non-default constructors)
     * @param p Parser to use for handling problematic content
     *
     * @return True if the problem is resolved (and content available used or skipped);
     * false if the handler did not anything and the problem is unresolved. Note that in
     * latter case caller will either throw an exception or explicitly skip the content,
     * depending on configuration.
     */
    override fun handleUnknownProperty(ctxt: DeserializationContext?, p: JsonParser?, deserializer: JsonDeserializer<*>?, beanOrClass: Any?, propertyName: String?): Boolean {
        return super.handleUnknownProperty(ctxt, p, deserializer, beanOrClass, propertyName)
    }

    /**
     * Method called when instance creation for a type fails due to an exception.
     * Handler may choose to do one of following things:
     *
     *  * Indicate it does not know what to do by returning [.NOT_HANDLED]
     *
     *  * Throw a [IOException] to indicate specific fail message (instead of
     * standard exception caller would throw
     *
     *  * Return actual instantiated value (of type `targetType`) to use as
     * replacement, and continue processing.
     *
     *  * Return `null` to use null as value but not to try further
     * processing (in cases where properties would otherwise be bound)
     *
     *
     *
     * @param instClass Type that was to be instantiated
     * @param argument (optional) Additional argument that was passed to creator, if any
     * @param t Exception that caused instantiation failure
     *
     * @return Either [.NOT_HANDLED] to indicate that handler does not know
     * what to do (and exception may be thrown), or value to use (possibly
     * `null`
     *
     * @since 2.8
     */
    override fun handleInstantiationProblem(ctxt: DeserializationContext?, instClass: Class<*>?, argument: Any?, t: Throwable?): Any {
        return super.handleInstantiationProblem(ctxt, instClass, argument, t)
    }

    /**
     * Handler method called if an expected type id for a polymorphic value is
     * not found and no "default type" is specified or allowed.
     * Handler may choose to do one of following things:
     *
     *  * Indicate it does not know what to do by returning `null`
     *
     *  * Indicate that nothing should be deserialized, by return `Void.class`
     *
     *  * Throw a [IOException] to indicate specific fail message (instead of
     * standard exception caller would throw
     *
     *  * Return actual resolved type to use for this particular case.
     *
     *
     *
     * @param ctxt Deserialization context to use for accessing information or
     * constructing exception to throw
     * @param baseType Base type to use for resolving subtype id
     * @param failureMsg Informational message that would be thrown as part of
     * exception, if resolution still fails
     *
     * @return Actual type to use, if resolved; `null` if handler does not know what
     * to do; or `Void.class` to indicate that nothing should be deserialized for
     * type with the id (which caller may choose to do... or not)
     *
     * @since 2.9
     */
    override fun handleMissingTypeId(ctxt: DeserializationContext?, baseType: JavaType?, idResolver: TypeIdResolver?, failureMsg: String?): JavaType {
        return super.handleMissingTypeId(ctxt, baseType, idResolver, failureMsg)
    }

    /**
     * Method called when a String value
     * cannot be converted to a non-String value type due to specific problem
     * (as opposed to String values never being usable).
     * Handler may choose to do one of 3 things:
     *
     *  * Indicate it does not know what to do by returning [.NOT_HANDLED]
     *
     *  * Throw a [IOException] to indicate specific fail message (instead of
     * standard exception caller would throw
     *
     *  * Return actual converted value (of type `targetType`) to use as
     * replacement, and continue processing.
     *
     *
     *
     * @param failureMsg Message that will be used by caller (by calling
     * [DeserializationContext.weirdNumberException])
     * to indicate type of failure unless handler produces key to use
     *
     * @return Either [.NOT_HANDLED] to indicate that handler does not know
     * what to do (and exception may be thrown), or value to use as (possibly
     * `null`)
     *
     * @since 2.8
     */
    override fun handleWeirdStringValue(ctxt: DeserializationContext?, targetType: Class<*>?, valueToConvert: String?, failureMsg: String?): Any {
        return super.handleWeirdStringValue(ctxt, targetType, valueToConvert, failureMsg)
    }

    /**
     * Method called when a numeric value (integral or floating-point from input
     * cannot be converted to a non-numeric value type due to specific problem
     * (as opposed to numeric values never being usable).
     * Handler may choose to do one of 3 things:
     *
     *  * Indicate it does not know what to do by returning [.NOT_HANDLED]
     *
     *  * Throw a [IOException] to indicate specific fail message (instead of
     * standard exception caller would throw
     *
     *  * Return actual converted value (of type `targetType`) to use as
     * replacement, and continue processing.
     *
     *
     *
     * @param failureMsg Message that will be used by caller (by calling
     * [DeserializationContext.weirdNumberException])
     * to indicate type of failure unless handler produces key to use
     *
     * @return Either [.NOT_HANDLED] to indicate that handler does not know
     * what to do (and exception may be thrown), or value to use as (possibly
     * `null`)
     *
     * @since 2.8
     */
    override fun handleWeirdNumberValue(ctxt: DeserializationContext?, targetType: Class<*>?, valueToConvert: Number?, failureMsg: String?): Any {
        return super.handleWeirdNumberValue(ctxt, targetType, valueToConvert, failureMsg)
    }

    /**
     * Method called when instance creation for a type fails due to lack of an
     * instantiator. Method is called before actual deserialization from input
     * is attempted, so handler may do one of following things:
     *
     *  * Indicate it does not know what to do by returning [.NOT_HANDLED]
     *
     *  * Throw a [IOException] to indicate specific fail message (instead of
     * standard exception caller would throw
     *
     *  * Handle content to match (by consuming or skipping it), and return actual
     * instantiated value (of type `targetType`) to use as replacement;
     * value may be `null` as well as expected target type.
     *
     *
     *
     * @param instClass Type that was to be instantiated
     * @param p Parser to use for accessing content that needs handling, to either
     * use it or skip it (latter with [JsonParser.skipChildren].
     *
     * @return Either [.NOT_HANDLED] to indicate that handler does not know
     * what to do (and exception may be thrown), or value to use (possibly
     * `null`
     *
     * @since 2.9
     */
    override fun handleMissingInstantiator(ctxt: DeserializationContext?, instClass: Class<*>?, valueInsta: ValueInstantiator?, p: JsonParser?, msg: String?): Any {
        return super.handleMissingInstantiator(ctxt, instClass, valueInsta, p, msg)
    }

    /**
     * @since 2.8
     */
    override fun handleMissingInstantiator(ctxt: DeserializationContext?, instClass: Class<*>?, p: JsonParser?, msg: String?): Any {
        return super.handleMissingInstantiator(ctxt, instClass, p, msg)
    }

    /**
     * Handler method called if resolution of type id from given String failed
     * to produce a subtype; usually because logical id is not mapped to actual
     * implementation class.
     * Handler may choose to do one of following things:
     *
     *  * Indicate it does not know what to do by returning `null`
     *
     *  * Indicate that nothing should be deserialized, by return `Void.class`
     *
     *  * Throw a [IOException] to indicate specific fail message (instead of
     * standard exception caller would throw
     *
     *  * Return actual resolved type to use for type id.
     *
     *
     *
     * @param ctxt Deserialization context to use for accessing information or
     * constructing exception to throw
     * @param baseType Base type to use for resolving subtype id
     * @param subTypeId Subtype id that failed to resolve
     * @param failureMsg Informational message that would be thrown as part of
     * exception, if resolution still fails
     *
     * @return Actual type to use, if resolved; `null` if handler does not know what
     * to do; or `Void.class` to indicate that nothing should be deserialized for
     * type with the id (which caller may choose to do... or not)
     *
     * @since 2.8
     */
    override fun handleUnknownTypeId(ctxt: DeserializationContext?, baseType: JavaType?, subTypeId: String?, idResolver: TypeIdResolver?, failureMsg: String?): JavaType {
        return super.handleUnknownTypeId(ctxt, baseType, subTypeId, idResolver, failureMsg)
    }

    /**
     * Method called when an embedded (native) value ([JsonToken.VALUE_EMBEDDED_OBJECT])
     * cannot be converted directly into expected value type (usually POJO).
     * Handler may choose to do one of 3 things:
     *
     *  * Indicate it does not know what to do by returning [.NOT_HANDLED]
     *
     *  * Throw a [IOException] to indicate specific fail message (instead of
     * standard exception caller would throw
     *
     *  * Return actual converted value (of type `targetType`) to use as
     * replacement, and continue processing.
     *
     *
     *
     * @return Either [.NOT_HANDLED] to indicate that handler does not know
     * what to do (and exception may be thrown), or value to use (possibly
     * `null`)
     *
     * @since 2.9
     */
    override fun handleWeirdNativeValue(ctxt: DeserializationContext?, targetType: JavaType?, valueToConvert: Any?, p: JsonParser?): Any {
        return super.handleWeirdNativeValue(ctxt, targetType, valueToConvert, p)
    }

    /**
     * Method that deserializers should call if the first token of the value to
     * deserialize is of unexpected type (that is, type of token that deserializer
     * cannot handle). This could occur, for example, if a Number deserializer
     * encounter [JsonToken.START_ARRAY] instead of
     * [JsonToken.VALUE_NUMBER_INT] or [JsonToken.VALUE_NUMBER_FLOAT].
     *
     *  * Indicate it does not know what to do by returning [.NOT_HANDLED]
     *
     *  * Throw a [IOException] to indicate specific fail message (instead of
     * standard exception caller would throw
     *
     *  * Handle content to match (by consuming or skipping it), and return actual
     * instantiated value (of type `targetType`) to use as replacement;
     * value may be `null` as well as expected target type.
     *
     *
     *
     * @param failureMsg Message that will be used by caller
     * to indicate type of failure unless handler produces value to use
     *
     * @return Either [.NOT_HANDLED] to indicate that handler does not know
     * what to do (and exception may be thrown), or value to use (possibly
     * `null`
     *
     * @since 2.10
     */
    override fun handleUnexpectedToken(ctxt: DeserializationContext?, targetType: JavaType?, t: JsonToken?, p: JsonParser?, failureMsg: String?): Any {

        // Unrecognized token 'returning': was expecting (JSON String, Number, Array, Object or token 'null', 'true' or 'false')
        //if(t!!.isScalarValue)
        if (failureMsg?.matches(Regex("Unrecognized toke '.*': was expecting (JSON String, Number, Array, Object or token 'null', 'true' or 'false')", RegexOption.DOT_MATCHES_ALL)) == true) {
            val next = p?.nextValue()
            return null!!
        } else {
            return super.handleUnexpectedToken(ctxt, targetType, t, p, failureMsg)

        }

    }
}







object Serializer {

    private val logReader = jacksonObjectMapper()
            .registerKotlinModule()
            .reader()
            //.withHandler(PlainTextProblemHandler())
            .with(DeserializationFeature.WRAP_EXCEPTIONS, DeserializationFeature.READ_ENUMS_USING_TO_STRING)
            .without(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, DeserializationFeature.FAIL_ON_TRAILING_TOKENS)
            .with(JsonParser.Feature.ALLOW_COMMENTS)
            .with(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES)
            .with(JsonParser.Feature.ALLOW_SINGLE_QUOTES)


    private val logWriter = jacksonObjectMapper()
        .setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL)
        .registerKotlinModule()
        .writerWithDefaultPrettyPrinter()
        .with(JsonGenerator.Feature.IGNORE_UNKNOWN)
        .withFeatures(
            SerializationFeature.FAIL_ON_SELF_REFERENCES,
            SerializationFeature.INDENT_OUTPUT,
            SerializationFeature.WRITE_ENUMS_USING_TO_STRING,
            SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .with(JsonGenerator.Feature.IGNORE_UNKNOWN)



    fun<TSerializable: MockSerializable> serialize(log: TSerializable): String = logWriter.writeValueAsString(log)
    fun<TSerializable: MockSerializable> deserialize(json: String): TSerializable = logReader.readValue<TSerializable>(json)!!
}

