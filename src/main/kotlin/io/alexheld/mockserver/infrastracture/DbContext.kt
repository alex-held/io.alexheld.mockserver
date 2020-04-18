/*
package io.alexheld.mockserver.infrastracture

import com.mongodb.*
import com.mongodb.client.*
import io.alexheld.mockserver.config.*
import io.alexheld.mockserver.domain.models.*
import org.bson.*
import org.bson.codecs.configuration.*
import org.bson.codecs.configuration.CodecRegistries.*
import org.bson.codecs.pojo.*


class DbContext (val config: DbConfig) {

    companion object {
        fun connectOrCreate(config: DbConfig): DbContext {
            return DbContext(config)
        }
    }

    var pojoCodecRegistry: CodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
        fromProviders(PojoCodecProvider.builder().automatic(true).build()))

    var settings = MongoClientSettings.builder()
        .codecRegistry(pojoCodecRegistry)
        .build()

    private val client = MongoClients.create()
    private val database = client.getDatabase(config.database)

    val setups = database.getCollection("Setup", Setup::class.java)
    val logs: MongoCollection<Document> = database.getCollection("Logs")
}
*/
