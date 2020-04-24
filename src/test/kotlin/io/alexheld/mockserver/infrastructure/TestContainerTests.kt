package io.alexheld.mockserver.infrastructure

import io.alexheld.mockserver.container.*
import org.amshove.kluent.*
import org.junit.jupiter.api.*

@testc
class TestContainerTests {


    val mongodb = MongoDBTestContainer()


    @Test
    fun shouldWork(){
        val value = true
        value.shouldBeTrue()
    }

/*


    @Test
    fun `should save setup into testContainer mongodb`() {

        mongodb.isRunning.shouldBeTrue()

        // Arrange
        val sut = MockServerRepositoryTests.MongoRepository(DbConfig(
            mongodb.getConnectionString(),
            "mockserver"))

        // Act
        val actual = sut.createSetup(Request(method = "POST"),
            Action("Placeholder...", 1))
        actual.dumpAsJson("createSetup")
    }
*/

}
