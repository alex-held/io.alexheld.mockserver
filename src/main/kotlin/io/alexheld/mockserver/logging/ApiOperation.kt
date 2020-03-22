package io.alexheld.mockserver.logging


class Operations {

    interface OperationMessage {
        val message: OperationMessages
        fun getFormattedMessage(): String = message.message
    }

    enum class OperationMessages(val message: String, vararg args: Any) {
        List("Listing <Object>"),
        Delete("Listing <Object>"),
        Inspect("Inspecting <Object>")
    }
}


enum class ApiOperation {
    List,
    Delete,
    Inspect
}
