package io.alexheld.mockserver.config

import org.apache.logging.log4j.*


public class MockConfig {

    companion object {
        public const val ADMIN_PORT = 9090
        public const val MOCK_PORT = 8080

        public var MAX_LOGS = 5000
        public var LOG_LEVEL = Level.INFO
    }
}
