package io.alexheld.mockserver.web.controllers

import io.alexheld.mockserver.logging.*

class LoggingController {

    public val logs: MutableList<Log> = mutableListOf()

    public fun list(): MutableList<Log> = logs


    public fun add(log: Log): Unit {
        logs.add(log)
    }
}
package io.alexheld.mockserver.web.controllers

