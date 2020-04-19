package io.alexheld.mockserver.infrastructure

import com.mongodb.client.*
import io.alexheld.mockserver.config.*
import io.alexheld.mockserver.domain.models.*
import io.alexheld.mockserver.domain.services.*
import io.alexheld.mockserver.logging.*
import io.alexheld.mockserver.logging.models.*
import io.alexheld.mockserver.testUtil.*
import org.bson.codecs.pojo.annotations.*
import org.bson.types.*
import org.junit.jupiter.api.*
import org.litote.kmongo.*
import java.time.*








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



/*
    @Test
    fun bla() {

        val setup = SetupEntity()
        setup.requestMatcher = Request(method = "POST")
        setup.actionable = Action("Placedholder..", 1)

        //  val mongoClient: MongoClient = MongoClient("mongodb+srv://adm:_rmq!nsApw-h!UTWyXDoN_CZB_XqsTcMr2@cloudatlas-xfber.gcp.mongodb.net/test?retryWrites=true&w=majority")

        val setups = getCollection<SetupEntity>("SetupsEntities", SetupEntity::class)

        setups.insertOne(setup)
        setups.find().last()
    }  */


    class MongoRepository(private val config: DbConfig) {


        private val client: MongoClient = KMongo.createClient(config.connectionString)
        private val db: MongoDatabase = client.getDatabase(config.database)

        val setups: MongoCollection<SetupEntity> = db.getCollection()
        val logs: MongoCollection<IdentifiableLog<DataContainerData>> = db.getCollection()

        fun createSetup(requestMatcher: Request?, actionable: Action) : Setup {
            val setupEntity = SetupEntity(requestMatcher = requestMatcher , actionable = actionable)
            setups.save(setupEntity)
            return Setup(setupEntity._id.toString(), setupEntity.createdAt, requestMatcher, actionable)
        }
    }


    @Test
    fun `should save setups to into mongodb`() {

        // Arrange
        val sut = MongoRepository(DbConfig("mongodb://localhost:27017", "mockserver"))

        // Act
        val actual = sut.createSetup(Request(method = "POST"), Action("Placeholder...", 1))
        actual.dumpAsJson("createSetup")
    }

    @Test
    fun `should read setups from mongodb`() {

        // Arrange
        val sut = MongoRepository(DbConfig("mongodb://localhost:27017", "mockserver"))

        // Act
        val actual = sut.createSetup(Request(method = "POST"), Action("Placeholder...", 1))
        actual.dumpAsJson("createSetup")
    }



    inline fun <reified T: Any> getCollection(name: String): MongoCollection<T> {
        val client = KMongo.createClient()
        val db = client.getDatabase("mockserver")
        return db.getCollection()
    }



    fun t() {

        val setup = Setup("1", Instant.EPOCH, Request(method = "POST"), Action("Placedholder.." + ".", 1))

        val log = IdentifiableLog.generateNew(ApiCategory.Log, LogMessageType.Request_Matched,
            RequestMatchedData(
                Request(method = "POST"),
                Setup("1", Instant.EPOCH, Request(method = "POST"), Action("Placedholder.." + ".", 1)),
                Action("Placedhold er...", 1)
            ), GenerationServiceImpl())


    }
}

@BsonDiscriminator()
data class SetupEntity(

    @BsonId
    val _id: ObjectId = ObjectId(),
    val createdAt: Instant = Instant.now(),
    val requestMatcher: Request?,
    val actionable: Action?
)
