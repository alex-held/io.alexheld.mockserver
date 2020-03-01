package domain.matcher

import io.alexheld.mockserver.domain.matcher.*
import io.alexheld.mockserver.domain.models.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

internal class RequestMatcherTest {

    @Test
    fun matches_returns_true_when_method_is_valid_match() {
        val default = Request(method = "POST")
        val other = Request(method = "POST")
        val actual = default.matches(other)
        assertTrue(actual)
    }

    @Test
    fun matches_returns_false_when_method_is_invalid() {
        val default = Request(method = "GET")
        val other = Request(method = "POST")
        val actual = default.matches(other)
        assertFalse(actual)
    }

    @Test
    fun matches_returns_true_when_path_is_valid_match() {
        val default = Request(method = "/some/path")
        val other = Request(method = "/some/path")
        val actual = default.matches(other)
        assertTrue(actual)
    }

    @Test
    fun matches_returns_false_when_path_is_invalid() {
        val default = Request(method = "/some/path")
        val other = Request(method = "/another/one")
        val actual = default.matches(other)
        assertFalse(actual)
    }


    @Test
    fun matches_returns_true_when_body_is_valid_match() {
        val default = Request(body = RequestBody(exact = true, body = "a-b-c"))
        val other = Request(body = RequestBody(exact = true, body = "a-b-c"))
        val actual = default.matches(other)
        assertTrue(actual)
    }

    @Test
    fun matches_returns_false_when_body_is_invalid() {
        val default = Request(body = RequestBody(exact = true, body = "a-b-c"))
        val other = Request(body = RequestBody(body = "a-b"))
        val actual = default.matches(other)
        assertFalse(actual)
    }

    @Test
    fun matches_returns_true_when_headers_are_valid_match() {
        val headers = mutableMapOf<String, MutableSet<String>>()
        headers["A"] = mutableSetOf("1", "2")
        headers["B"] = mutableSetOf("1")
        headers["C"] = mutableSetOf("1", "2", "3")
        val default = Request(headers = headers)
        val other = Request(headers = headers)
        val actual = default.matches(other)
        assertTrue(actual)
    }

    @Test
    fun matches_returns_false_when_headers_is_invalid() {
        val headers = mutableMapOf<String, MutableSet<String>>()
        headers["A"] = mutableSetOf("1", "2")
        headers["B"] = mutableSetOf("1")
        headers["C"] = mutableSetOf("1", "2", "3")
        val default = Request(headers = headers)
        val other = Request(headers = headers.minus("B").toMutableMap())
        val actual = default.matches(other)
        assertFalse(actual)
    }
/*

    @Test() //TODO: Enable test again
    fun matches_returns_true_when_cookies_are_valid_match() {
        val cookies = mutableMapOf<String, String>()
        cookies["A"] = "1"
        cookies["B"] = "2"
        cookies["C"] = "3"
        val default = Request(cookies = cookies)
        val other = Request(cookies = cookies)
        val actual = default.matches(other)
        assertTrue(actual)
    }
*/

    @Test
    fun matches_returns_false_when_cookies_is_invalid() {
        val cookies = mutableMapOf<String, String>()
        cookies["A"] = "1"
        cookies["B"] = "2"
        cookies["C"] = "3"
        val default = Request(cookies = cookies)
        val other = Request(cookies = cookies.minus("B").toMutableMap())
        val actual = default.matches(other)
        assertFalse(actual)
    }
}
