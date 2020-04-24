package io.alexheld.mockserver.infrastructure

import com.mongodb.client.*
import io.alexheld.mockserver.domain.models.*
import io.alexheld.mockserver.infrastracture.*
import io.alexheld.mockserver.testUtil.*
import io.alexheld.mockserver.testUtil.container.*
import org.amshove.kluent.*
import org.junit.jupiter.api.*
import org.litote.kmongo.*
import org.litote.kmongo.util.*
import org.testcontainers.junit.jupiter.*


@Testcontainers
class SetupRepositoryTests {

    @Container
    val mongo = MongoDBTestContainer()

    @Test
    fun `should save setups to into mongodb`() {

        // Arrange
        val sut =  SetRepository(mongo)

        // Act
        val actual = sut.createSetup(Request(method = "POST"), Action("Placeholder...", 1))
        actual?.dumpAsJson("createSetup")
    }


    @Test
    fun `should save setupdoc to into mongodb`() {

        // Arrange
        val sut =  SetRepository(mongo)

        // Act
        val actual = sut.createSetup( Request(method = "POST"), Action("Placeholder...", 1))
        val actual2 = sut.createSetup( Request(method = "POST"), Action("Placeholder...", 1))

        val find = sut.deleteById(actual?.idValue.toString())

        actual?.dumpAsJson("1")
        sut.count().shouldBeEqualTo(1)
    }


    @Test
    fun `should read setups from mongodb`() {

        // Arrange
        val sut = SetRepository(mongo)

        // Act
        val actual = sut.createSetup(Request(method = "POST"), Action("Placeholder...", 1))
        actual?.dumpAsJson("createSetup")
    }



    inline fun <reified T: Any> getCollection(name: String): MongoCollection<T> {
        val client = KMongo.createClient()
        val db = client.getDatabase("mockserver")
        return db.getCollection()
    }

}
