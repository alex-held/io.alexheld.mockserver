package io.alexheld.mockserver.logging

import io.alexheld.mockserver.domain.models.*
import io.alexheld.mockserver.domain.services.*
import io.alexheld.mockserver.logging.models.*
import io.alexheld.mockserver.serialization.*
import io.alexheld.mockserver.testUtil.*
import org.amshove.kluent.*
import org.junit.jupiter.params.*
import org.junit.jupiter.params.provider.*
import java.time.*
import java.util.stream.*


class LogDataContainerTests : WithTestResources {

    override fun getRootPath(): String = "/logging/yaml"

    companion object {

        val gen: GenerationService = GenerationServiceImpl.createDebugGeneration(GenerationServiceImpl.GenerationStrategy.Debug_Constant)
        private fun <T : DataContainerData> createSubject(type: LogMessageType, container: T): IdentifiableLog<T> =
            IdentifiableLog.generateNew(ApiCategory.Log, LogMessageType.Operation, container, gen)

        @JvmStatic
        fun logDataContainerData(): Stream<Arguments> = Stream.of(
            Arguments.of(
                "setup-created.yaml", createSubject(LogMessageType.Setup_Created,
                    SetupCreatedData(Setup("1", Instant.EPOCH, Request(method = "POST"), Action("Placedholder...", 1))))
            ),
            Arguments.of(
                "request-matched.yaml", createSubject(LogMessageType.Request_Matched,
                    RequestMatchedData(
                        Request(method = "POST"),
                        Setup("1", Instant.EPOCH, Request(method = "POST"), Action("Placedholder.." + ".", 1)),
                        Action("Placedholder...", 1)
                    )
                )
            ),
            Arguments.of(
                "setup-deleted.yaml", createSubject(LogMessageType.Setup_Deleted,
                    SetupDeletedData(Setup("1", Instant.EPOCH, Request(path = "/some/api"), Action("Hello World!", 1))))
            ),
            Arguments.of(
                "exception.yaml", createSubject(LogMessageType.Exception,
                    ExceptionData("an error message"))
            ),
            Arguments.of(
                "log-deleted.yaml", createSubject(LogMessageType.Operation,
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
                "request-received.yaml", createSubject(LogMessageType.Request_Received,
                    RequestReceivedData(Request(method = "POST")))
            ),
            Arguments.of(
                "action.yaml", createSubject(LogMessageType.Action_Response,
                    ActionData(Action(message = "Hello World!", statusCode = 202)))
            )
        )
    }


    @ParameterizedTest
    @MethodSource("logDataContainerData")
    fun `should serialize IdentifableLog`(fileName: String, log: IdentifiableLog<*>) {

        // Arrange
        val expected = readResource(fileName)
        val actual = Yaml.serialize(log)

        println(actual)

        // Act
        actual.shouldNotBeBlank().shouldBeEqualTo(expected)
    }


}
