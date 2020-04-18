package io.alexheld.mockserver.config

data class AppConfig(
    val logConfig: String,
    val db: DbConfig,
    val port: Int
)


val MOCK_ENVIRONMENT_DEV = AppConfig(
    logConfig = "log4j2-local.yaml",
    db = DbConfig(
        url = "jdbc:h2:~/conduit-db/conduit",
        database = "org.h2.Driver"
    ),
    port = 9000
)
