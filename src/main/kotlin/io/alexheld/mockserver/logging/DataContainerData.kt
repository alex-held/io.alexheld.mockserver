package io.alexheld.mockserver.logging

import com.fasterxml.jackson.annotation.*

interface DataContainerData {

    @JsonIgnore
    fun getType(): LogMessageType
}

fun DataContainerData.isOperation(): Boolean = getType() == LogMessageType.Operation
