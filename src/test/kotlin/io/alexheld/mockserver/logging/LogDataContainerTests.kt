package io.alexheld.mockserver.logging

import io.alexheld.mockserver.domain.models.*
import io.alexheld.mockserver.logging.models.*
import io.alexheld.mockserver.serialization.*
import io.alexheld.mockserver.testUtil.*
import org.amshove.kluent.*
import org.junit.jupiter.api.*
import org.junit.jupiter.params.*
import org.junit.jupiter.params.provider.*
import java.time.*
import java.util.stream.*


class LogDataContainerTests : WithTestResources {

    override fun getRootPath(): String = "/logging/yaml"

    companion object {

        @BeforeAll
        @JvmStatic
        fun before() {
            Generator.enableDebugGeneration = true
        }

        @AfterAll
        @JvmStatic
        fun after() {
            Generator.enableDebugGeneration = false
        }

        private fun <T : DataContainerData> createSubject(type: LogMessageType, container: T, operation: ApiOperation? = null): IdentifiableLog<T> =
            IdentifiableLog.generateNew(ApiCategory.Log, LogMessageType.Operation, container, operation)

        @JvmStatic
        fun logDataContainerData(): Stream<Arguments> = Stream.of(
            Arguments.of(
                "setup-created", createSubject(LogMessageType.Setup_Created,
                    SetupCreatedData(Setup("1", Instant.EPOCH, Request(method = "POST"), Action("Placedholder...", 1))))
            ),
            Arguments.of(
                "request-matched", createSubject(LogMessageType.Request_Matched,
                    RequestMatchedData(
                        Request(method = "POST"),
                        Setup("1", Instant.EPOCH, Request(method = "POST"), Action("Placedholder.." + ".", 1)),
                        Action("Placedholder...", 1)
                    )
                )
            ),
            Arguments.of(
                "setup-deleted", createSubject(LogMessageType.Setup_Deleted,
                    SetupDeletedData(Setup("1", Instant.EPOCH, Request(path = "/some/api"), Action("Hello World!", 1))))
            ),
            Arguments.of(
                "exception", createSubject(LogMessageType.Exception,
                    ExceptionData("an error message"))
            ),
            Arguments.of(
                "log-deleted", createSubject(LogMessageType.Operation,
                    OperationData(ApiOperation.Delete, mutableListOf(
                        createSubject(LogMessageType.Setup_Created,
                            SetupCreatedData(
                                Setup(
                                    "1",
                                    Instant.EPOCH,
                                    Request(method = "POST", path = "/some/api"),
                                    Action(message = "Hello World!", statusCode = 202)
                                )
                            )
                        )
                    )
                    )
                )
            ),
            Arguments.of(
                "request-received", createSubject(LogMessageType.Request_Received,
                    RequestReceivedData(Request(method = "POST")))
            ),
            Arguments.of(
                "action", createSubject(LogMessageType.Action_Response,
                    ActionData(Action(message = "Hello World!", statusCode = 202)))
            )
        )
    }


    @ParameterizedTest
    @MethodSource("logDataContainerData")
    fun `should serialize IdentifableLog`(fileName: String, log: IdentifiableLog<*>) {

        // Arrange
        val expected = readResource(fileName)
        val subject = Yaml.getWriterFor(log::class.java)
        val actual = subject.writeValueAsString(log)

        println(actual)

        // Act
        actual.shouldNotBeBlank().shouldBeEqualTo(expected)
    }


}
