package io.alexheld.mockserver.config


data class MongoDBConfig(override val connectionString: String, override val database: String) : MongoDBConnection

interface MongoDBConnection {
    val connectionString: String
    val database: String
}
