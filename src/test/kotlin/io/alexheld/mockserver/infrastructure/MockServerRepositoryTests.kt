package io.alexheld.mockserver.infrastructure

import com.mongodb.MongoClient
import com.mongodb.client.*
import io.alexheld.mockserver.domain.models.*
import io.alexheld.mockserver.domain.services.*
import io.alexheld.mockserver.logging.*
import io.alexheld.mockserver.logging.models.*
import org.amshove.kluent.*
import org.bson.codecs.pojo.annotations.*
import org.joda.time.*
import org.junit.jupiter.api.*
import java.time.*
import java.time.Instant


class MockServerRepositoryTests {

/*
    @Test
    fun `should be able to create a new Setup`() {

        // Arrange
        val sut = Repository()

        // Act
        sut.createSetup(CreateSetupCommand(Request(), Action("Hello MongoDb!", 200)))

        // Assert
        val response = sut.getAllSetups()
        val setups = response.data
        setups?.shouldNotBeEmpty()
    }*/

    abstract class MongoEntityBase {

        @BsonId()
        protected var _id: Int? = null

        @BsonProperty()
        val createdAt: DateTime = DateTime.now()


    }
    data class SetupEntity(val requestMatcher: Request? = null, val actionable: Action)

    @Test
    fun bla() {

        val setup = SetupEntity("1", Instant.EPOCH, Request(method = "POST"), Action("Placedholder.." + ".", 1))

      //  val mongoClient: MongoClient = MongoClient("mongodb+srv://adm:_rmq!nsApw-h!UTWyXDoN_CZB_XqsTcMr2@cloudatlas-xfber.gcp.mongodb.net/test?retryWrites=true&w=majority")
        val mongoClient: MongoClient = MongoClient()
        val database: MongoDatabase = mongoClient.getDatabase("mockserver")
        val setups = database.getCollection("Setups", Setup::class.java)

        setups.insertOne()
        collections.shouldNotBeEmpty()
    }


    fun t() {

        val setup =  Setup("1", Instant.EPOCH, Request(method = "POST"), Action("Placedholder.." + ".", 1))

        val log = IdentifiableLog.generateNew(ApiCategory.Log, LogMessageType.Request_Matched,
            RequestMatchedData(
                Request(method = "POST"),
                Setup("1", Instant.EPOCH, Request(method = "POST"), Action("Placedholder.." + ".", 1)),
                Action("Placedholder...", 1)
            ), GenerationServiceImpl())


    }
}
