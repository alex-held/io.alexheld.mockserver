/*
package io.alexheld.mockserver.infrastracture

import com.mongodb.client.*
import io.alexheld.mockserver.api.setups.*
import io.alexheld.mockserver.domain.models.*
import io.alexheld.mockserver.responses.*
import java.time.*


*/
/**
 *
 *//*

interface RepositoryBase<T> {
    fun getAll(): GenericResponse<List<T>>
    fun first(predicate: Predicate<T>): GenericResponse<T>
    fun where(predicate: Predicate<T>): GenericResponse<List<T>>
    fun delete(requestMatcher: Request): GenericResponse<List<T>>
}


class Repository(*/
/*private val context: DbContext*//*
) : MockServerRepository {

    // create codec registry for POJOs
*/
/*    var pojoCodecRegistry: CodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
        fromProviders(PojoCodecProvider.builder().automatic(true).build()))*//*


    val mongoClient = MongoClients.create()

    // get handle to "mockserver" database
    */
/*var database: MongoDatabase = mongoClient.getDatabase("mockserver")
        .withCodecRegistry(pojoCodecRegistry)
*//*

    // get a handle to the "setuo" collection
    var setups: MongoCollection<Setup> = mongoClient
        .getDatabase("mockserver")
        .getCollection("Setup", Setup::class.java)

    */
/**
     * Finds all [Setup] stored on the server
     *//*

    override fun getAllSetups(): GenericResponse<List<Setup>> {
        return try {
            val match = setups.find().toList()
            GenericOkResponse(match)
        } catch (ex: Exception) {
            GenericErrorResponse(OperationFailedErrorResponse(ex.localizedMessage))
        }
    }

    */
/**
     * Finds the first [Setup] which matches the provided [predicate]
     *//*

    override fun findFirstSetup(predicate: Predicate<Setup>): GenericResponse<Setup> {
        return try {
            val match = setups.find().first(predicate)
            GenericOkResponse(match)
        } catch (ex: Exception) {
            GenericErrorResponse(OperationFailedErrorResponse(ex.localizedMessage))
        }
    }

    */
/*
     * Finds the first [Setup] which matches the provided [Predicate]
     *//*

    override fun findSetups(predicate: Predicate<Setup>): GenericResponse<List<Setup>> {
        return try {
            val match = setups.find().filter(predicate).toList()
            return GenericOkResponse(match)
        } catch (ex: Exception) {
            GenericErrorResponse(OperationFailedErrorResponse(ex.localizedMessage))
        }
    }


    */
/**
     * Finds the first [Setup] which matches the provided [Predicate]
     *//*

    override fun createSetup(cmd: CreateSetupCommand): GenericResponse<Setup> {
        return try {

            val setup = Setup(
                id = "1",
                timestamp = Instant.now(),
                request = cmd.request,
                action = cmd.action
            )


            return GenericOkResponse(setup)
        } catch (ex: Exception) {
            GenericErrorResponse(OperationFailedErrorResponse(ex.localizedMessage))
        }
    }

    override fun deleteSetup(cmd: DeleteSetupCommand): GenericResponse<List<Setup>> {
        return try {
            val deleted = setups.find()//((Setup::action eq cmd.action) eq (Setup::request eq cmd.request))
            return GenericOkResponse(deleted.toList())
        } catch (ex: Exception) {
            GenericErrorResponse(OperationFailedErrorResponse(ex.localizedMessage))
        }
    }

}
*/
