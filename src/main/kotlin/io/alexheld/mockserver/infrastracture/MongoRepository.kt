package io.alexheld.mockserver.infrastracture

import com.mongodb.client.*
import com.mongodb.client.result.*
import io.alexheld.mockserver.config.*
import io.alexheld.mockserver.domain.models.*
import io.alexheld.mockserver.logging.*
import io.alexheld.mockserver.responses.*
import org.litote.kmongo.*
import org.litote.kmongo.id.*



class MongoDBContext(private val connection: MongoDBConnection) {
    val client: MongoClient = KMongo.createClient(connection.connectionString)
    val database: MongoDatabase = client.getDatabase(connection.database)

    val setups: MongoCollection<Setup> = database.getCollection()
    val logs: MongoCollection<IdentifiableLog<DataContainerData>> = database.getCollection()
}



 abstract class MongoRepository<TDocument: Any>(private val connection: MongoDBConnection) {

     protected val context = MongoDBContext(connection)

     protected abstract fun getCollection(): MongoCollection<TDocument>

     open fun save(document: TDocument): TDocument? {
         return try {
             getCollection().save(document)
             document
         }catch (exception: Exception) {
             null
         }
     }

     open fun findById(id: String) :  TDocument?  =  getCollection().findOneById(ObjectIdGenerator.create(id))
     open fun deleteById(id: String) :  DeleteResult  =  getCollection().deleteOneById(ObjectIdGenerator.create(id))
     fun count(): Long = getCollection().countDocuments()

     open fun findAll(): GenericResponse<List<TDocument>> {
         return try {
             GenericOkResponse(getCollection().find().toList())
         } catch (ex: java.lang.Exception) {
             GenericErrorResponse(RepositoryExceptionErrorResponse(ex.localizedMessage))
         }
     }

     enum class SaveResult {
         Create,
         Update,
         Fail
     }
 }

 class SetRepository(private val connection: MongoDBConnection) : MongoRepository<Setup>(connection)  {

    fun createSetup(requestMatcher: Request?, actionable: Action) : Setup? {
        val setupEntity = Setup()
        setupEntity.request = requestMatcher
        setupEntity.action = actionable
        return save(setupEntity)
    }

     override fun getCollection(): MongoCollection<Setup> = context.setups
 }


