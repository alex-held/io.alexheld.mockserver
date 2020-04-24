package io.alexheld.mockserver.container

import io.alexheld.mockserver.config.*
import io.alexheld.mockserver.domain.models.*
import io.alexheld.mockserver.infrastructure.*
import io.alexheld.mockserver.testUtil.*
import org.amshove.kluent.*

import kotlin.test.Test


class TestContainerTests {


    val mongodb = MongoDBTestContainer()


    @Test
    fun shouldWork(){
        val value = true
        println("The value is '$value'.")
        value.shouldBeTrue()
    }



    @Test
    fun `should save setup into testContainer mongodb`() {

        mongodb.isRunning.shouldBeTrue()

        // Arrange
        val sut = MockServerRepositoryTests.MongoRepository(DbConfig(
            mongodb.getConnectionString(),
            "mockserver"))

        // Act
        val actual = sut.createSetup(Request(method = "POST"), Action("Placeholder...", 1))
        actual.dumpAsJson("createSetup")
    }

}
