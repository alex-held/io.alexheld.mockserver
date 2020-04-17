package io.alexheld.mockserver.config

import io.alexheld.mockserver.domain.repositories.*
import io.alexheld.mockserver.domain.services.*
import org.koin.dsl.*
import org.koin.experimental.builder.*

object ModulesConfig {
    val setupModule = module {
        singleBy<SetupRepository, SetupRepositoryImpl>()
        singleBy<SetupService, SetupServiceImpl>()
        single { GenerationServiceImpl(GenerationServiceImpl.GenerationStrategy.Default) }
        single { LogFactory(get()) }
        singleBy<LogRepository, LogRepositoryImpl>()
        singleBy<LogService, LogServiceImpl>()
    }
}
