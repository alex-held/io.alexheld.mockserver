package io.alexheld.mockserver.logging

import io.alexheld.mockserver.domain.repositories.*


class HttpStateHandler(
    private val logger: MockServerLogger,
    val scheduler: Scheduler,
    val repository: SetupRepository,
    val config: ConfigurationProperties
) {
    fun log(log: Log): Unit = logger.logEvent(log)
}
