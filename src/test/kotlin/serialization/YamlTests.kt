package serialization

import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.module.kotlin.*
import io.alexheld.mockserver.domain.models.*
import io.alexheld.mockserver.logging.*
import io.alexheld.mockserver.logging.models.*
import io.alexheld.mockserver.serialization.*
import io.alexheld.mockserver.testUtil.*
import org.amshove.kluent.*
import org.junit.jupiter.api.*
import org.junit.jupiter.params.*
import org.junit.jupiter.params.provider.*
import java.io.*
import java.time.*
import java.util.stream.*

class YamlTests {

    private val root = "/serialization/yaml"
    private fun streamYamlFile(fileName: String): InputStream = this::class.java.getResourceAsStream("$root/$fileName.yaml")

    private fun readYaml(file: String): String {
        val stream = streamYamlFile(file)
        val reader = InputStreamReader(stream)
        return reader.readText()
    }

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

        private fun <T : DataContainerData> createSubject(container: T, operation: ApiOperation? = null): IdentifiableLog<T> =
            IdentifiableLog.generateNew(ApiCategory.Log, LogMessageType.Operation, container, operation)

        @JvmStatic
        fun logDataContainerData(): Stream<Arguments> = Stream.of(
            Arguments.of(
                "setup-created",
                createSubject(SetupCreatedData(Setup("1", Instant.EPOCH, Request(method = "POST"), Action("Placedholder...", 1))))
            ),
            Arguments.of(
                "setup-created",
                createSubject(
                    RequestMatchedData(
                        Request(method = "POST"),
                        Setup("1", Instant.EPOCH, Request(method = "POST"), Action("Placedholder.." + ".", 1)),
                        Action("Placedholder...", 1)
                    )
                )
            ),
            Arguments.of(
                "setup-created",
                createSubject(SetupDeletedData(Setup("1", Instant.EPOCH, Request(path = "/some/api"), Action("Hello World!", 1))))
            ),
            Arguments.of(
                "setup-created",
                createSubject(SetupDeletionFailedData(Exception("test-123"), "an error message"))
            ),
            Arguments.of(
                "setup-created",
                createSubject(
                    LogDeletedData(
                        mutableListOf(
                            createSubject(
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
                "setup-created",
                createSubject(RequestReceivedData(Request(method = "POST")))
            ),
            Arguments.of(
                "setup-created",
                createSubject(ActionData(Action(message = "Hello World!", statusCode = 202)))
            )
        )
    }


    @ParameterizedTest
    @MethodSource("logDataContainerData")
    fun `should serialize IdentifableLog`(fileName: String, log: IdentifiableLog<*>) {

        // Arrange
        val exptected = readYaml(fileName)
        val subject = Yaml.getWriterFor(log::class.java)
        val actual = subject.writeValueAsString(log)

        println(actual)

        // Act
        actual.shouldNotBeBlank().shouldBeEqualWhenTrimmed(exptected)
    }


    @ParameterizedTest
    @MethodSource("logDataContainerData")
    fun `should deserialize yaml to IdentifableLog`(fileName: String, log: IdentifiableLog<*>) {

        // Arrange
        val yaml = readYaml(fileName)
        val subject = Yaml.getReader2(log::class.java)
        val actual = subject.readValue(yaml, log::class.java)

        println(actual)

        // Act
        actual.shouldNotBeNull()
    }


    @Test
    fun `should deserialize yaml to setupCreatedL`() {

        // Arrange
        val yaml = readYaml("setup-created")

        val log = createSubject(SetupCreatedData(Setup("1", Instant.EPOCH, Request(method = "POST"), Action("Placedholder...", 1))))
        val subject = ObjectMapper(Yaml.mapper.factory)
        val actual = subject.readValue<IdentifiableLog<SetupCreatedData>>(yaml)

        println(actual)

        // Act
        actual.shouldNotBeNull()
    }
}
