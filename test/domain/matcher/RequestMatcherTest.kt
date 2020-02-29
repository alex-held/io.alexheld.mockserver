package domain.matcher

import io.alexheld.mockserver.domain.matcher.*
import io.alexheld.mockserver.domain.models.*
import org.junit.jupiter.api.Test
import kotlin.test.*

internal class RequestMatcherTest {

    @Test
    fun defaultShouldMatch() {
        val default = Request()
        val other = Request(method = "POST")

        val actual = default.matches(other)

        assertTrue(actual)
    }
}
