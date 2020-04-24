package io.alexheld.mockserver.logging

import io.alexheld.mockserver.domain.services.*
import io.alexheld.mockserver.logging.models.*
import org.litote.kmongo.*
import java.time.*


/**
 * As unique identifiable Log.
 */
data class IdentifiableLog<TData : DataContainerData>(
    var id: Id<IdentifiableLog<TData>> = newId(),
    var instant: Instant,
    override var apiCategory: ApiCategory,
    var type: LogMessageType,
    var operation: ApiOperation?,
    override var data: TData
) : WithCategory, DataContainer<TData> {

    companion object {

        fun <T: DataContainerData> parse(id: String?, timestamp: String?, apiCategory: ApiCategory, type: LogMessageType, data: T, gen: GenerationService): IdentifiableLog<T> {

            val log = IdentifiableLog<T>(gen.newId(id), Instant.parse(timestamp) ?: gen.getTimestamp(), apiCategory, type, null, data)

            log.data = data
            log.type = data.getType()

            if (data is OperationData) {
                log.operation = data.apiOperation
            }

            return log
        }

         fun <T: DataContainerData> generateNew(apiCategory: ApiCategory, type: LogMessageType, data: T, gen: GenerationService): IdentifiableLog<T> {
            val id = gen.newId<IdentifiableLog<T>>()
            val time = gen.getTimestamp()
            val log = IdentifiableLog(id, time, apiCategory, type, null, data)

            log.data = data
            log.type = data.getType()

            if (data is OperationData) {
                log.operation = data.apiOperation
            }

            return log
        }
    }
}
