package io.alexheld.mockserver.logging

import io.alexheld.mockserver.logging.models.*
import java.time.*


/**
 * As unique identifiable Log.
 */
data class IdentifiableLog<TData: DataContainerData> (
    var id: String,
    var instant: String,
    override var apiCategory: ApiCategory,
    var type: LogMessageType,
    var operation: ApiOperation?,
    override var data: TData
) : WithCategory, DataContainer<TData> {

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
                if(apiOperation != null) enumValueOf<ApiOperation>(apiOperation) else null,
                m["data"] as DataContainerData
              )
        }



        fun <T : DataContainerData> generateNew(apiCategory: ApiCategory, type: LogMessageType, data: T, operation: ApiOperation? = null): IdentifiableLog<T>  {
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
