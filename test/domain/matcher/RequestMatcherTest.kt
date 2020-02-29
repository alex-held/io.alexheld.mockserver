package domain.matcher

import io.alexheld.mockserver.domain.matcher.*
import io.alexheld.mockserver.domain.models.*
import org.junit.*

internal class RequestMatcherTest {

    @Test
    fun defaultShouldMatch() {
        val default = Request()
        val other = Request(method = "POST")

        val actual = default.matches(other)

        Assert.assertTrue(actual)
    }
}
