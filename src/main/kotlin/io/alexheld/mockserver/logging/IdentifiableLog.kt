package io.alexheld.mockserver.logging

import com.fasterxml.jackson.annotation.*
import java.time.*


/**
 * As unique identifiable Log.
 */
data class IdentifiableLog<TData: DataContainerData>  @JsonCreator(mode = JsonCreator.Mode.DEFAULT) constructor(
    @JacksonInject var id: String,
    @JacksonInject var time: String,
    @JacksonInject override var apiCategory: ApiCategory,
    @JacksonInject var type: LogMessageType,
    @JacksonInject  override var data: TData?,
    @JacksonInject var operation: ApiOperation?
) : WithCategory, DataContainer<TData> {


    val instant get() = Instant.parse(this.time)


    fun either(onValue: (TData) -> Unit, onEmpty: () -> Unit) =
        if (data is TData) onValue(data!!)
        else onEmpty()

    companion object {

        fun create(m: MutableMap<String, Any?>): IdentifiableLog<*> {
            val apiCategory = enumValueOf<ApiCategory>(m["apiCategory"] as String)
            val type = enumValueOf<LogMessageType>(m["type"] as String)
            val instant =Instant.parse(m["instant"] as String)
            val apiOperation = m["operation"] as String?

            return IdentifiableLog(
                m["id"] as String,
                instant.toString(),
                apiCategory,
                type,
                m["data"] as DataContainerData?,
                if(apiOperation != null) enumValueOf<ApiOperation>(apiOperation) else null)
        }


/*

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        @JvmStatic
        fun create2(
            @JacksonInject id: String,
            @JacksonInject instant: Instant,
            @JacksonInject apiCategory: ApiCategory,
            @JacksonInject type: LogMessageType,
            @JacksonInject data: DataContainerData?,
            @JacksonInject operation: ApiOperation?
        ): IdentifiableLog<*> {
            return IdentifiableLog(id, instant, apiCategory, type, data, operation)
        }
*/

             //   enumValueOf<ApiCategory>(apiCategory),

        fun <T : DataContainerData> generateNew(apiCategory: ApiCategory, type: LogMessageType, data: T?, operation: ApiOperation? = null): IdentifiableLog<T>  {
            val id = Generator.getId()
            val time = Generator.getTimestampString()
            val log = IdentifiableLog<T>(id, time.toString(), apiCategory, type, null, null)

            log.operation = operation
            log.data = data
            return log
        }
    }
}
