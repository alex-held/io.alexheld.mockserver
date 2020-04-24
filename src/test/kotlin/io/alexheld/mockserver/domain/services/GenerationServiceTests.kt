package io.alexheld.mockserver.domain.services

import io.alexheld.mockserver.testUtil.*
import org.amshove.kluent.*
import kotlin.test.Test
import java.time.*
import java.util.*

class GenerationServiceTests {

    private fun createSubject(generationStrategy: GenerationServiceImpl.GenerationStrategy): GenerationService=  GenerationServiceImpl(generationStrategy)

    @Test
    fun `when GenerationStrategy is Debug_Constant, it should generate constant id`(){

        // Arrange
        val expected = "00000000-0000-0000-0000-000000000000"
        val sut = createSubject(GenerationServiceImpl.GenerationStrategy.Debug_Constant)

        // Act
        val actual = sut.getId()

        // Assert
        actual.shouldBeEqualTo(expected)
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
    fun `when GenerationStrategy is Debug_Counter, it should generate increasing timestamps`(){

        // Arrange
        val sut = createSubject(GenerationServiceImpl.GenerationStrategy.Debug_Counter)

        // Act
        val first = sut.getTimestamp()
        val second = sut.getTimestamp()

        // Assert
        first.isBefore(second).shouldBeTrue()
    }

    @Test
    fun `when GenerationStrategy is Debug_Counter, it should generate increasing ids`(){

        // Arrange
        val sut = createSubject(GenerationServiceImpl.GenerationStrategy.Debug_Counter)

        // Act
        val first = sut.getId()
        val second = sut.getId()

        // Assert
        first.toInt().shouldBeEqualTo(1)
        second.toInt().shouldBeEqualTo(2)
        second.toInt().`should be greater than`(first.toInt())
    }

    @Test
    fun `when GenerationStrategy is Default it should generate random UUID`(){

        // Arrange
        val sut = createSubject(GenerationServiceImpl.GenerationStrategy.Default)

        // Act
        val actual = sut.getId()

        // Assert
        UUID.fromString(actual).shouldNotBeNull()
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
