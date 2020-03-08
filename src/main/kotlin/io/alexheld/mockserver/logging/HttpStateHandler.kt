package io.alexheld.mockserver.logging

import io.alexheld.mockserver.domain.repositories.*
import io.alexheld.mockserver.domain.services.*


class HttpStateHandler(
    private val logger: LogService,
    val scheduler: Scheduler,
    val repository: SetupRepository,
    val config: ConfigurationProperties
) { }
