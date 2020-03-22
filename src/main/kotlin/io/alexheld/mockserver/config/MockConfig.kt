package io.alexheld.mockserver.config

import org.apache.logging.log4j.*


class MockConfig {

    companion object {
        const val ADMIN_PORT = 9090
        const val MOCK_PORT = 8080

        var MAX_LOGS = 5000
        var LOG_LEVEL = Level.INFO
    }
}
