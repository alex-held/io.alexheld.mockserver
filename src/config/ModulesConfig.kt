package io.alexheld.mockserver.config

import com.google.inject.*
import io.alexheld.mockserver.web.controllers.*
import io.ktor.application.*
/*

import org.kodein.di.*
import org.kodein.di.generic.*

val CoreModule = Kodein.Module {
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

*/


public class Core(private val app: Application) : AbstractModule() {

    override fun configure() {
        bind(LoggingController::class.java)
    }

}
