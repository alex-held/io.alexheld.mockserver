package io.alexheld.mockserver.domain.services

import io.alexheld.mockserver.testUtil.*
import org.amshove.kluent.*
import java.time.*
import kotlin.test.*

class GenerationServiceTests {

    private fun createSubject(generationStrategy: GenerationServiceImpl.GenerationStrategy): GenerationService=  GenerationServiceImpl(generationStrategy)

    @Test
    fun `when GenerationStrategy is Debug_Constant, it should generate constant id`(){

        // Arrange
        val expected = "5ea36d460adb9d50a96ec60c"
        val sut = createSubject(GenerationServiceImpl.GenerationStrategy.Debug_Constant)

        // Act
        val actual = sut.newId<GenerationServiceTests>()

        // Assert
        actual.toString().shouldBeEqualTo(expected)
    }

    @Test
    fun `when GenerationStrategy is Debug_Constant, it should generate constant timestamp`(){

        // Arrange
        val expected = Instant.EPOCH
        val sut = createSubject(GenerationServiceImpl.GenerationStrategy.Debug_Constant)

        // Act
        val actual = sut.getTimestamp()

        // Assert
        actual.shouldBeEqualTo(expected)
    }

    @Test
    fun `when GenerationStrategy is Default it should generate random UUID`(){

        // Arrange
        val sut = createSubject(GenerationServiceImpl.GenerationStrategy.Default)

        // Act
        val actual = sut.newId<GenerationServiceTests>()

        // Assert
        actual.toString().shouldNotBeNullOrEmpty()
    }

    @Test
    fun `when GenerationStrategy is Default it should generate current timestamp`(){

        // Arrange
        val sut = createSubject(GenerationServiceImpl.GenerationStrategy.Default)

        // Act
        val now = Instant.now()
        Thread.sleep(500)
        val actual = sut.getTimestamp()

        // Assert
        val differenceInMilliseconds = actual.toEpochMilli().minus(now.toEpochMilli())
        differenceInMilliseconds.shouldBeGreaterThan(499)
        differenceInMilliseconds.shouldBeLessThan(1000)
    }

    @Test
    fun `getTimestampString() should be ISO-8601`(){

        // Arrange
        val iso8601Regex = "^(-?(?:[1-9][0-9]*)?[0-9]{4})-(1[0-2]|0[1-9])-(3[01]|0[1-9]|[12][0-9])T(2[0-3]|[01][0-9]):([0-5][0-9]):([0-5][0-9])(\\\\.[0-9]+)?(Z)?\$"
        val sut = createSubject(GenerationServiceImpl.GenerationStrategy.Default)

        // Act
        val actual = sut.getTimestampString()
        actual.dump("ISO8601 TimestampString")

        // Assert
        Regex(iso8601Regex).matches(actual).shouldBeTrue()
    }
}
