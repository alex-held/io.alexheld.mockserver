package io.alexheld.mockserver.logging

import io.alexheld.mockserver.logging.models.*


/**
 * As unique identifiable Log.
 */
data class IdentifiableLog<TData : DataContainerData>(
    var id: String,
    var instant: String,
    override var apiCategory: ApiCategory,
    var type: LogMessageType,
    var operation: ApiOperation?,
    override var data: TData
) : WithCategory, DataContainer<TData> {

    companion object {


        fun <T : DataContainerData> generateNew(apiCategory: ApiCategory, type: LogMessageType, data: T): IdentifiableLog<T> {
            val id = Generator.getId()
            val time = Generator.getTimestampString()
            val log = IdentifiableLog<T>(id, time.toString(), apiCategory, type, null, data)

            log.data = data
            log.type = data.getType()

            if (data is OperationData) {
                log.operation = data.apiOperation
            }

            return log
        }


    }
}
