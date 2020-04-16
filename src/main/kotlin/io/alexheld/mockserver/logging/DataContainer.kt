package io.alexheld.mockserver.logging

import com.fasterxml.jackson.annotation.*


interface DataContainer<TData: DataContainerData> {
    var data: TData
}


interface DataContainerData {

    @JsonIgnore
    fun getType(): LogMessageType
}
