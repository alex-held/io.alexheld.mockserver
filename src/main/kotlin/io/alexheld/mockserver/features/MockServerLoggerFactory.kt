package io.alexheld.mockserver.features

class MockServerLoggerFactory {
    inline fun <reified T> createLogger(): MockServerLogger<T> =
        MockServerLogger<T>()
}
