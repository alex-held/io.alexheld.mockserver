package io.alexheld.mockserver.logging


enum class ApiCategory {
    Log,
    Request,
    Setup
}

enum class ApiOperation {
    List,
    Delete,
    Create,
    Inspect
}

enum class LogMessageType {
    //RUNNABLE, TRACE, DEBUG, INFO, WARN, EXCEPTION, CLEARED, RETRIEVED, VERIFICATION, VERIFICATION_FAILED,
    Setup_Created ,
    Setup_Deleted ,
    Exception,
    Request_Received,
    Request_Matched ,
    Action_Response ,
    Operation
}
