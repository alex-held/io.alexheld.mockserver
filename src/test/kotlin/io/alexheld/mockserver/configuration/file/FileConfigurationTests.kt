package io.alexheld.mockserver.configuration.file

import io.alexheld.mockserver.logging.*
import io.alexheld.mockserver.testUtil.*
import io.mockk.*
import org.amshove.kluent.*
import org.junit.jupiter.api.*


class FileConfigurationTests : WithTestResources {

    private fun createSubject(content: String, str: Generator.GenerationStrategy? = null) =
        FileConfigurationRepository("/some/path/to/config/dir/with/setups.yaml", createMockFileSystem(content),  Generator.createDebugGeneration(str ?: Generator
            .GenerationStrategy.Debug_Constant))


    private fun createMockFileSystem(content: String): FileSystemService {
        val fs = mockk<FileSystemService>()
        every { fs.readFile(any()) } returns content
        return fs
    }


    @Test
    fun `get() should read setups from configuration file`(){

      /*  val genMock: GenerationService = mock()
        whenever(genMock.getId()).thenReturn("1", "2", "3", "4", "5")
        whenever(genMock.getTimestamp()).doReturnConsecutively(listOf(
            "0001-01-01T00:00:00Z",
            "0002-01-01T00:00:00Z",
            "0003-01-01T00:00:00Z",
            "0004-01-01T00:00:00Z",
            "0005-01-01T00:00:00Z").map { Instant.parse(it) })*/
        // Arrange
        val subject = createSubject(readResource("setups.yaml"), Generator.GenerationStrategy.Debug_Counter)

        // Act
        val config = subject.get()
        config.dumpAsJson("FileConfiguration")

        // Assert
        config.setups.shouldHaveSize(5)
        config.setups[0].id.shouldBeEqualTo("1")
        config.setups[4].id.shouldBeEqualTo("4")
    }



    @Test
    fun `should deserializer`(){

        // Arrange
        val subject = createSubject(
"""
---
id: 101010
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
""", Generator.GenerationStrategy.Debug_Counter)

        // Act
        val config = subject.get()
        config.dumpAsJson("FileConfiguration")

        // Assert
        config.shouldNotBeNull()
        config.setups[0].id.shouldBeEqualTo("101010")
        config.setups[0].timestamp.toString().shouldBeEqualTo("0001-01-01T00:00:00Z")
        config.setups[1].id.shouldBeEqualTo("1")
        config.setups[1].timestamp.toString().shouldBeEqualTo("1970-01-01T00:00:00Z")
    }



    @Test
    fun `when config does not provide Id, it should generate an Id`(){
        // Arrange
        val subject = createSubject(
"""
timestamp: 1970-01-01T00:00:00Z
action:
  statusCode: 400
""",Generator.GenerationStrategy.Debug_Counter)

        // Act
        val config = subject.get()
        config.dumpAsJson("FileConfiguration")

        // Assert
        config.shouldNotBeNull()
        config.setups[0].id.shouldBeEqualTo("1")
    }



    @Test
    fun `when config does contain an Id, it should generate an Id`(){

        // Arrange
        val subject = createSubject(
"""
---
timestamp: 1970-01-01T00:00:00Z
action:
  statusCode: 400
""",
            Generator.GenerationStrategy.Debug_Counter)

        // Act
        val config = subject.get()
        config.dumpAsJson("FileConfiguration")

        // Assert
        config.shouldNotBeNull()
        config.setups[0].id.shouldBeEqualTo("1")
        config.setups[0].timestamp.toString().shouldBeEqualTo("1970-01-01T00:00:00Z")
    }



    @Test
    fun `when config contains an Id, it should not override it`(){

        // Arrange
        val subject = createSubject("""
---
id: 101010
timestamp: 1970-01-01T00:00:00Z
action:
  statusCode: 400
""", Generator.GenerationStrategy.Debug_Counter)

        // Act
        val config = subject.get()
        config.dumpAsJson("FileConfiguration")

        // Assert
        config.shouldNotBeNull()
        config.setups[0].id.shouldBeEqualTo("101010")
        config.setups[0].timestamp.toString().shouldBeEqualTo("1970-01-01T00:00:00Z")
    }


    @Test
    fun `when config does not contain a timestamp, it should generate one`(){

        // Arrange
        val subject = createSubject(
"""
id: 1234
action:
  statusCode: 400
""", Generator.GenerationStrategy.Debug_Counter)

        // Act
        val config = subject.get()
        config.dumpAsJson("FileConfiguration")

        // Assert
        config.shouldNotBeNull()
        config.setups[0].id.shouldBeEqualTo("1234")
        config.setups[0].timestamp.toString().shouldBeEqualTo("0001-01-01T00:00:00Z")
    }


    @Test
    fun `when config contains a timestamp, it should not override it`(){

        // Arrange
        val subject = createSubject(
"""
timestamp: 1970-01-01T00:00:00Z
action:
  statusCode: 400
""", Generator.GenerationStrategy.Debug_Counter)


        // Act
        val config = subject.get()
        config.dumpAsJson("FileConfiguration")

        // Assert
        config.shouldNotBeNull()
        config.setups[0].id.shouldBeEqualTo("1")
        config.setups[0].timestamp.toString().shouldBeEqualTo("1970-01-01T00:00:00Z")
    }

    override fun getRootPath(): String = "/configuration"
}

