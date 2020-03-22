package io.alexheld.mockserver;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.alexheld.mockserver.logging.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

/**
 * As unique identifiable Log.
 */
public class IdentifiableLog2<TData extends DataContainerData> implements WithCategory, DataContainer<TData> {

    @JsonCreator
    public IdentifiableLog2(String id, String instant, ApiCategory apiCategory, LogMessageType type, TData data , ApiOperation operation) {

        this.id = id;
        this.instant = Instant.parse(instant);
        this.apiCategory = apiCategory;
        this.type = type;
        this.data = data;
        this.operation = operation;
    }

    @JsonProperty("id")
    public String id;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    public Instant instant;

    @JsonProperty("apiCategory")
    public ApiCategory apiCategory;

    @JsonProperty("type")
    public LogMessageType type;

    @JsonProperty("data")
    public TData data;

    @JsonProperty("operation")
    public ApiOperation operation;

    @Nullable
    @Override
    public TData getData() {
        return data;
    }

    @Override
    public void setData(@Nullable TData data) {
        this.data = data;
    }

    @NotNull
    @Override
    public ApiCategory getApiCategory() {
        return apiCategory;
    }
}



/*


    @Nullable
    @Override
    public TData getData() {
        return null;
    }

    @Override
    public void setData(@Nullable TData data) {

    }

    @NotNull
    @Override
    public ApiCategory getApiCategory() {
        return null;
    } : this(id, Instant.parse(instant), apiCategory, type, data, operation)




        fun either(onValue: (TData) -> Unit, onEmpty: () -> Unit) =
        if (data is TData) onValue(data!!)
        else onEmpty()

        companion object {

@JsonCreator
@JvmStatic
        fun create(m: MutableMap<String, Any?>): IdentifiableLog<*> {
        val apiCategory = enumValueOf<ApiCategory>(m["apiCategory"] as String)
        val type = enumValueOf<LogMessageType>(m["type"] as String)
        val instant =Instant.parse(m["instant"] as String)
        val apiOperation = m["operation"] as String?

        return IdentifiableLog(
        m["id"] as String,
        instant,
        apiCategory,
        type,
        m["data"] as DataContainerData?,
        if(apiOperation != null) enumValueOf<ApiOperation>(apiOperation) else null)
        }

        fun <T : DataContainerData> generateNew(apiCategory: ApiCategory, type: LogMessageType, data: T?, operation: ApiOperation? = null): IdentifiableLog<T>  {
        val id = Generator.getId()
        val time = Generator.getTimestampString()
        val log = IdentifiableLog<T>(id, time, apiCategory, type)

        log.operation = operation
        log.data = data
        return log
        }

        }
*/
