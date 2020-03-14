package io.alexheld.mockserver.logging


public enum class LogMessageType(public val type: String) {
    //RUNNABLE, TRACE, DEBUG, INFO, WARN, EXCEPTION, CLEARED, RETRIEVED,
    Setup_Created("Setup created"),
    Setup_Deleted("Setup deleted"),
    Setup_Deletion_Failed("Setup deletion failed"),
    Request_Received("Request received"),
    Request_Matched("Request matched"),
    Action_Response("Respond with HttpResponse"),
    Operation("SERVER_CONFIGURATION")
    /// VERIFICATION, VERIFICATION_FAILED, SERVER_CONFIGURATION,
}
