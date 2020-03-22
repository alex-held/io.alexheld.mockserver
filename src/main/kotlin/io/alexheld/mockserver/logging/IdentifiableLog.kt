package io.alexheld.mockserver.logging

import java.time.*


/**
 * As unique identifiable Log.
 */
open class IdentifiableLog<TData: DataContainerData>(
    val id: String,
    val instant: Instant,
    override val apiCategory: ApiCategory,
    val type: LogMessageType,
    override var data: TData? = null,
    var operation: ApiOperation? = null) : WithCategory, DataContainer<TData> {

    constructor(apiCategory: ApiCategory, type: LogMessageType) :
            this(Generator.getId(), Generator.getTimestampString(), apiCategory, type)

    constructor(apiCategory: ApiCategory, type: LogMessageType,
                data: TData? = null, operation: ApiOperation? = null) : this(
        Generator.getId(), Generator.getTimestampString(), apiCategory, type, data, operation)



    fun either(onValue: (TData) -> Unit, onEmpty: () -> Unit) =
        if (data is TData) onValue(data!!)
        else onEmpty()


    companion object {

        fun <T : DataContainerData> generateNew(apiCategory: ApiCategory, type: LogMessageType, data: T?, operation: ApiOperation? = null): IdentifiableLog<T>  {
            val id = Generator.getId()
            val time = Generator.getTimestampString()
            val log = IdentifiableLog<T>(id, time, apiCategory, type)

            log.operation = operation
            log.data = data
            return log
        }
    }
}
