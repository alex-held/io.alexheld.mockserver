package io.alexheld.mockserver.config

import io.alexheld.mockserver.domain.repositories.*
import io.alexheld.mockserver.domain.services.*
import io.alexheld.mockserver.logging.*
import org.kodein.di.*
import org.kodein.di.generic.*
import org.slf4j.*


object ModulesConfig {

    val CoreModule: Kodein = Kodein {
        bind<SetupRepository>() with singleton { SetupRepositoryImpl() }
        bind<SetupService>() with singleton { SetupServiceImpl(instance()) }
        bind<MockServerLogger>() with singleton { MockServerLogger() }
        bind<Scheduler>() with singleton { Scheduler(instance()) }
        bind<LoggingHandler>() with singleton { LoggingHandler(instance()) }
        bind<StandardOutConsoleHandler>() with singleton { StandardOutConsoleHandler() }
        bind<HttpStateHandler>() with singleton { HttpStateHandler(instance(), instance(), instance(), instance()) }
        bind<Logger>() with singleton {
            LoggerFactory.getILoggerFactory().getLogger(Logger.ROOT_LOGGER_NAME)
        }
    }
}
