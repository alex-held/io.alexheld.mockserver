package io.alexheld.mockserver.matcher

import io.alexheld.mockserver.domain.matcher.*
import io.alexheld.mockserver.domain.models.*
import io.alexheld.mockserver.testUtil.*
import org.amshove.kluent.*
import org.junit.jupiter.params.*
import org.junit.jupiter.params.provider.*
import io.alexheld.mockserver.testUtil.*
import java.util.stream.*

class MatchTests {

    companion object {
        @JvmStatic
        fun bodyData(): Stream<Arguments> =
            Stream.of(
                // isMatch
                Arguments.of(Request(body = RequestBody("abc", true)), Request(body = RequestBody("abc", false)), true),
                Arguments.of(Request(body = RequestBody("abc", true)), Request(body = RequestBody("abc", true)), true),
                Arguments.of(Request(body = RequestBody("abc")), Request(body = RequestBody("ab")), true),
                Arguments.of(Request(body = RequestBody("abc")), Request(body = RequestBody("abc")), true),
                Arguments.of(Request(body = RequestBody("abc")), Request(body = RequestBody("bc")), true),

                // no match
                Arguments.of(Request(body = RequestBody("abc", true)), Request(body = RequestBody("a")), false),
                Arguments.of(Request(body = RequestBody("abc", false)), Request(body = RequestBody("d")), false),
                Arguments.of(Request(body = RequestBody("", false)), Request(body = RequestBody("abc")), false)
            )


        @JvmStatic
        fun methodData(): Stream<Arguments> = Stream.of(
                Arguments.of(Request(method = "POST"), Request(method = "POST"), true),
                Arguments.of(Request(), Request(method = "GET"), true),
                Arguments.of(Request(method = "POST"), Request(), false),
                Arguments.of(Request(method = "POST"), Request(method = "GET"), false)
        )

        @JvmStatic
        fun pathData(): Stream<Arguments> = Stream.of(
            Arguments.of(Request(path = "/some/path"), Request(path = "/some/path"), true),
            Arguments.of(Request(), Request(path = "/some/path"), true),
            Arguments.of(Request(path = "/some/path"), Request(), false),
            Arguments.of(Request(path = "/some/path"), Request(path = "/another/path"), false)
        )

        @JvmStatic
        fun cookiesData(): Stream<Arguments> = Stream.of(
            // match
            Arguments.of(Request(cookies = mutableMapOf(Pair("Content-Type", "application/json"))), Request(cookies = mutableMapOf(Pair("Content-Type", "application/json"))), true),
            Arguments.of(Request(cookies = mutableMapOf(Pair("Content-Type", "application/json"), Pair("Content-Length","100"))), Request(cookies = mutableMapOf(Pair("Content-Type", "application/json"), Pair("Content-Length","100"))), true),
            Arguments.of(Request(), Request(cookies = mutableMapOf(Pair("Content-Type", "application/json"))), true),

            // no match
            Arguments.of(Request(cookies = mutableMapOf(Pair("Content-Type", "application/json"))), Request(), false),
            Arguments.of(Request(cookies = mutableMapOf(Pair("Content-Type", "application/json"))), Request(cookies = mutableMapOf(Pair("Content-Type", "plain/text"))), false),
            Arguments.of(Request(cookies = mutableMapOf(Pair("Content-Type", "application/json"))), Request(cookies = mutableMapOf(Pair("Content-Length", "100"))), false)
        )

        @JvmStatic
        fun headersData(): Stream<Arguments> = Stream.of(
            // match
            Arguments.of(Request(headers = mutableMapOf(
                    Pair("Content-Type", mutableSetOf("application/json")))), Request(headers = mutableMapOf(
                    Pair("Content-Type", mutableSetOf("application/json")))), true),

            Arguments.of(
                Request(headers = mutableMapOf(
                    Pair("Content-Type", mutableSetOf("application/json", "plain/text")),
                    Pair("Content-Length", mutableSetOf("100"))
                )),
                Request(headers = mutableMapOf(
                    Pair("Content-Type", mutableSetOf("application/json", "plain/text")),
                    Pair("Content-Length", mutableSetOf("100")))), true),

            Arguments.of(Request(headers = mutableMapOf(
                    Pair("Content-Type", mutableSetOf("application/json", "plain/text")))), Request(headers = mutableMapOf(
                    Pair("Content-Type", mutableSetOf("application/json", "plain/text")))), true),
            Arguments.of(Request(), Request(headers = mutableMapOf(Pair("Content-Type", mutableSetOf("application/json", "plain/text")))), true),

            // no match
            Arguments.of(Request(headers = mutableMapOf(Pair("Content-Type", mutableSetOf("application/json")))), Request(), false),
            Arguments.of(Request(headers = mutableMapOf(
                    Pair("Content-Type", mutableSetOf("application/json", "plain/text")))), Request(headers = mutableMapOf(
                    Pair("Content-Length", mutableSetOf("200")))), false),
            Arguments.of(
                Request(headers = mutableMapOf(
                    Pair("Content-Type", mutableSetOf("application/json", "plain/text")),
                    Pair("Content-Length", mutableSetOf("100"))
                )),
                Request(headers = mutableMapOf(
                    Pair("Content-Length", mutableSetOf("100")))), false)
        )
    }

    @ParameterizedTest
    @MethodSource("bodyData")
    fun `should match body`(a: Request, b: Request, isMatch: Boolean) {
        // Act
        val result = Match.requests(a, b)

        // Assert
        result.isSuccess().shouldBeEqualTo(isMatch)
        result.errors.shouldBeEmptyWhen(isMatch)
    }

    @ParameterizedTest
    @MethodSource("methodData")
    fun `should match method`(a: Request, b: Request, isMatch: Boolean) {
        // Act
        val result = Match.requests(a, b)

        // Assert
        result.isSuccess().shouldBeEqualTo(isMatch)
        result.errors.shouldBeEmptyWhen(isMatch)
    }



    @ParameterizedTest
    @MethodSource("pathData")
    fun `should match path`(a: Request, b: Request, isMatch: Boolean) {
        // Act
        val result = Match.requests(a, b)

        // Assert
        result.isSuccess().shouldBeEqualTo(isMatch)
        result.errors.shouldBeEmptyWhen(isMatch)
    }

    @ParameterizedTest
    @MethodSource("cookiesData")
    fun `should match cookies`(a: Request, b: Request, isMatch: Boolean) {
        // Act
        val result = Match.requests(a, b)

        // Assert
        result.isSuccess().shouldBeEqualTo(isMatch)
        result.errors.shouldBeEmptyWhen(isMatch)
    }

    @ParameterizedTest
    @MethodSource("headersData")
    fun `should match headers`(a: Request, b: Request, isMatch: Boolean) {

        // Act
        val result = Match.requests(a, b)

        // Assert
        result.isSuccess().shouldBeEqualTo(isMatch)
        result.errors.shouldBeEmptyWhen(isMatch)
    }

}
