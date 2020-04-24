package io.alexheld.mockserver.config

data class AppConfig(
    val logConfig: String,
    val db: MongoDBConfig,
    val port: Int
)


val MOCK_ENVIRONMENT_DEV = AppConfig(
    logConfig = "log4j2-local.yaml",
    db = MongoDBConfig(
        connectionString = "jdbc:h2:~/conduit-db/conduit",
        database = "org.h2.Driver"
    ),
    port = 9000
)
