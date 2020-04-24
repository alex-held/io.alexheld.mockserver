package io.alexheld.mockserver.configuration.file

import io.alexheld.mockserver.domain.services.*
import io.alexheld.mockserver.testUtil.*
import io.mockk.*
import org.amshove.kluent.*
import org.junit.jupiter.api.*


class   FileConfigurationTests : WithTestResources {

    private fun createSubject(content: String, str: GenerationServiceImpl.GenerationStrategy? = null) =
        FileConfigurationRepository("/some/path/to/config/dir/with/setups.yaml",
            createMockFileSystem(content),
            GenerationServiceImpl.createDebugGeneration(str ?: GenerationServiceImpl
                .GenerationStrategy.Debug_Constant))


    private fun createMockFileSystem(content: String): FileSystemService {
        val fs = mockk<FileSystemService>()
        every { fs.readFile(any()) } returns content
        return fs
    }


    @Test
    fun `get() should read setups from configuration file`() {
        // Arrange
        val subject = createSubject(readResource("setups.yaml"), GenerationServiceImpl.GenerationStrategy.Debug_Constant)

        // Act
        val config = subject.get()
        config.dumpAsJson("FileConfiguration")

        // Assert
        config.setups.shouldHaveSize(5)
        config.setups[0].id.toString().shouldNotBeNullOrEmpty()
        config.setups[0].id.toString().shouldNotBeNullOrEmpty()
    }


    @Test
    fun `should deserializer`() {

        // Arrange
        val subject = createSubject(
            """
---
id: 5ea36d460adb9d50a96ec60c
request:
  method: GET
action:
  statusCode: 400
---
timestamp: 1970-01-01T00:00:00Z
request:
  method: GET
action:
  statusCode: 400
""", GenerationServiceImpl.GenerationStrategy.Debug_Constant)

        // Act
        val config = subject.get()
        config.dumpAsJson("FileConfiguration")

        // Assert
        config.shouldNotBeNull()
        config.setups[0].id.toString().shouldBeEqualTo("5ea36d460adb9d50a96ec60c")
        config.setups[0].timestamp.toString().shouldBeEqualTo("1970-01-01T00:00:00Z")
        config.setups[1].id.toString().shouldNotBeNullOrEmpty()
        config.setups[1].timestamp.toString().shouldBeEqualTo("1970-01-01T00:00:00Z")
    }


    @Test
    fun `when config does not provide Id, it should generate an Id`() {
        // Arrange
        val subject = createSubject(
            """
timestamp: 1970-01-01T00:00:00Z
action:
  statusCode: 400
""", GenerationServiceImpl.GenerationStrategy.Debug_Constant)

        // Act
        val config = subject.get()
        config.dumpAsJson("FileConfiguration")

        // Assert
        config.shouldNotBeNull()
        config.setups[0].id.toString().shouldNotBeNullOrEmpty()
    }


    @Test
    fun `when config does contain an Id, it should generate an Id`() {

        // Arrange
        val subject = createSubject(
            """
---
timestamp: 1970-01-01T00:00:00Z
action:
  statusCode: 400
""",
            GenerationServiceImpl.GenerationStrategy.Debug_Constant)

        // Act
        val config = subject.get()
        config.dumpAsJson("FileConfiguration")

        // Assert
        config.shouldNotBeNull()
        config.setups[0].id.toString().shouldNotBeNullOrEmpty()
        config.setups[0].timestamp.toString().shouldBeEqualTo("1970-01-01T00:00:00Z")
    }


    @Test
    fun `when config contains an Id, it should not override it`() {

        // Arrange
        val subject = createSubject("""
---
id: 5ea36d460adb9d50a96ec60c
timestamp: 1970-01-01T00:00:00Z
action:
  statusCode: 400
""", GenerationServiceImpl.GenerationStrategy.Debug_Constant)

        // Act
        val config = subject.get()
        config.dumpAsJson("FileConfiguration")

        // Assert
        config.shouldNotBeNull()
        config.setups[0].id.toString().shouldBeEqualTo("5ea36d460adb9d50a96ec60c")
        config.setups[0].timestamp.toString().shouldBeEqualTo("1970-01-01T00:00:00Z")
    }


    @Test
    fun `when config does not contain a timestamp, it should generate one`() {

        // Arrange
        val subject = createSubject(
            """
id: 5ea36d460adb9d50a96ec60c
action:
  statusCode: 400
""", GenerationServiceImpl.GenerationStrategy.Debug_Constant)

        // Act
        val config = subject.get()
        config.dumpAsJson("FileConfiguration")

        // Assert
        config.shouldNotBeNull()
        config.setups[0].id.toString().shouldBeEqualTo("5ea36d460adb9d50a96ec60c")
        config.setups[0].timestamp.toString().shouldBeEqualTo("1970-01-01T00:00:00Z")
    }


    @Test
    fun `when config contains a timestamp, it should not override it`() {

        // Arrange
        val subject = createSubject(
            """
timestamp: 1970-01-01T00:00:00Z
action:
  statusCode: 400
""", GenerationServiceImpl.GenerationStrategy.Debug_Constant)


        // Act
        val config = subject.get()
        config.dumpAsJson("FileConfiguration")

        // Assert
        config.shouldNotBeNull()
        config.setups[0].id.toString().shouldNotBeNullOrEmpty()
        config.setups[0].timestamp.toString().shouldBeEqualTo("1970-01-01T00:00:00Z")
    }

    override fun getRootPath(): String = "/configuration"
}

