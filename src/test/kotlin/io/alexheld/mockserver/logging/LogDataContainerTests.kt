package io.alexheld.mockserver.logging

import io.alexheld.mockserver.domain.models.*
import io.alexheld.mockserver.logging.models.*
import io.alexheld.mockserver.serialization.*
import org.amshove.kluent.*
import org.junit.jupiter.api.*
import org.junit.jupiter.params.*
import org.junit.jupiter.params.provider.*
import java.time.*
import java.util.stream.*


class LogDataContainerTests {

    companion object {
        private fun <T : DataContainerData> createSubject(container: T, operation: ApiOperation? = null): IdentifiableLog<T> =
            IdentifiableLog.generateNew(ApiCategory.Log, LogMessageType.Operation, container, operation)

        @JvmStatic
        fun logDataContainerData(): Stream<Arguments> = Stream.of(
            Arguments.of(createSubject(SetupCreatedData(Setup("1", Instant.EPOCH,Request(method = "POST", path = "/some/api"), Action(message = "Hello World!", statusCode = 202))))),
            Arguments.of(createSubject(SetupDeletedData(Setup("1", Instant.EPOCH, Request(method = "POST", path = "/some/api"), Action(message = "Hello World!", statusCode = 202))))),
            Arguments.of(createSubject(SetupDeletionFailedData(Exception("test-123"), "an error message"))),
            Arguments.of(createSubject(LogDeletedData(mutableListOf(createSubject(SetupCreatedData(Setup("1", Instant.EPOCH, Request(method = "POST", path = "/some/api"), Action(message = "Hello World!", statusCode = 202))))))))
        )
    }


    @ParameterizedTest
    @MethodSource("logDataContainerData")
    fun  `should plug different data containers into same log template`(container: DataContainer<*>) {

        // Arrange
        val expected = SetupCreatedData(
            Setup(
                "1", Instant.EPOCH,
                Request(method = "POST", path = "/some/api"),
                Action(message = "Hello World!", statusCode = 202)
            )
        )

        val subject = createSubject(expected)

        // Act & Assert
        subject.apiCategory.shouldBeEqualTo(ApiCategory.Log)
        subject.operation.shouldBeNull()
        subject.data.shouldBeEqualTo(expected)
    }


    @Test
    fun `should execute either side when model contains data`() {

        // Arrange
        val setupCreatedData = SetupCreatedData(
            Setup(
                "1", Instant.EPOCH,
                Request(method = "POST", path = "/some/api"),
                Action(message = "Hello World!", statusCode = 202)
            )
        )

        var flag = "empty"
        val subject = IdentifiableLog.generateNew(ApiCategory.Setup, LogMessageType.Setup_Created, setupCreatedData)

        // Act & Assert
        subject.either({ data ->
            flag = Serializer.serialize(data)
            println(flag)
        },
            { println("no value (this is wrong)") })


        flag.shouldNotBeNullOrBlank()
            .shouldNotBeEqualTo("empty")
            .shouldContainAll("Hello World!", "POST", "202", "/some/api")
    }


}
