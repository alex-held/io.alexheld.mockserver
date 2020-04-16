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
            Arguments.of(createSubject(SetupCreatedData(Setup("1", Instant.EPOCH, Request(method = "POST"), Action("Placedholder...", 1))))),
            Arguments.of(createSubject(RequestMatchedData(Request(method = "POST"), Setup("1", Instant.EPOCH, Request(method = "POST"), Action("Placedholder...", 1)), Action("Placedholder...", 1)))),
            Arguments.of(createSubject(SetupDeletedData(Setup("1", Instant.EPOCH, Request(path = "/some/api"), Action("Hello World!", 1))))),
            Arguments.of(createSubject(ExceptionData("an error message"))),
            Arguments.of(createSubject(OperationData(ApiOperation.Delete, Operations.OperationMessages.Delete, mutableListOf(createSubject(SetupCreatedData(Setup("1", Instant.EPOCH, Request(method = "POST", path ="/some/api"), Action(message = "Hello World!", statusCode = 202)))))))),
            Arguments.of(createSubject(RequestReceivedData(Request(method="POST")))),
            Arguments.of(createSubject(ActionData(Action(message = "Hello World!", statusCode = 202))))
        )
    }


    @ParameterizedTest
    @MethodSource("logDataContainerData")
    fun  `should plug different data containers into same log template`(container: IdentifiableLog<*>) {

        // Arrange
        val subject = container
        println(Serializer.serialize(subject))

        // Act & Assert
        subject.apiCategory.shouldNotBeNull()
        subject.operation.shouldBeNull()
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
     /*   subject.either({ data ->
            flag = Serializer.serialize(data)
            println(flag)
        },
            { println("no value (this is wrong)") })*/


        flag.shouldNotBeNullOrBlank()
            .shouldNotBeEqualTo("empty")
            .shouldContainAll("Hello World!", "POST", "202", "/some/api")
    }


}
