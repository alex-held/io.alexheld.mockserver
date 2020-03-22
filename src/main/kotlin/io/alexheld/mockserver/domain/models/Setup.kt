package io.alexheld.mockserver.domain.models

import java.time.*


data class Setup(
    var id: String,
    var timestamp: Instant,
    var request: Request?,
    var action: Action
)


