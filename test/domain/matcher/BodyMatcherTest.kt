package domain.matcher

import io.alexheld.mockserver.domain.matcher.*
import io.alexheld.mockserver.domain.models.*
import org.junit.*

class BodyMatcherTest {

    @Test
    fun shouldMatchWhenBodyIsExactlyTheSame() {
        // Arrange
        val requestBody = RequestBody(body = "abc", exact = true)
        val upstream = RequestBody(body = "abc")

        // Act
        val actual = requestBody.matches(upstream)

        // Assert
        Assert.assertTrue(actual)
    }

    @Test
    fun shouldNotMatchWhenBodyIsNotTheSame() {
        // Arrange
        val requestBody = RequestBody(body = "ac", exact = true)
        val upstream = RequestBody(body = "abc")

        // Act
        val actual = requestBody.matches(upstream)

        // Assert
        Assert.assertFalse(actual)
    }


    @Test
    fun shouldMatchWhenBodyDoesContainSubstring() {
        // Arrange
        val requestBody = RequestBody(body = "abc", exact = false)
        val upstream = RequestBody(body = "a")

        // Act
        val actual = requestBody.matches(upstream)

        // Assert
        Assert.assertTrue(actual)
    }

    @Test
    fun shouldNotMatchWhenBodyDoesNotContainSubstring() {
        // Arrange
        val requestBody = RequestBody(body = "a", exact = false)
        val upstream = RequestBody(body = "abc")

        // Act
        val actual = requestBody.matches(upstream)

        // Assert
        Assert.assertFalse(actual)
    }
}
