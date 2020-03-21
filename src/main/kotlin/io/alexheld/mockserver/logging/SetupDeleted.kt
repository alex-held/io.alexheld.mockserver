package io.alexheld.mockserver.logging

import io.alexheld.mockserver.domain.models.*

class SetupDeletedLog : LogDTO {

    var setup: Setup by content

    constructor(content: MutableMap<String, Any?> = mutableMapOf()) : super(content)
    constructor(setup: Setup) : this(mutableMapOf(
        "setup" to setup
    ))
}
