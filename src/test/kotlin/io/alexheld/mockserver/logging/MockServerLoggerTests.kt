package io.alexheld.mockserver.logging

import org.amshove.kluent.*
import org.junit.jupiter.api.*

class MockServerLoggerTests {

    @Test
    fun should_name_logger_like_calling_type() {

        val logger = MockServerLogger()
        logger.name.shouldBe("MockServerLoggerTests")
    }
}
