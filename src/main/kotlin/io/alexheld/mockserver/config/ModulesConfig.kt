package io.alexheld.mockserver.config

import io.alexheld.mockserver.domain.repositories.*
import io.alexheld.mockserver.domain.services.*
import org.koin.dsl.*
import org.koin.experimental.builder.*
import org.slf4j.*
import java.io.*

class MockLogger {

    fun log(message: String){
        println("[MOCK]\t\t$message")
    }
}

object ModulesConfig {
    val setupModule = module {
        singleBy<SetupRepository, SetupRepositoryImpl>()
        singleBy<SetupService, SetupServiceImpl>()
    }

    val logModule = module {
        singleBy<LogRepository, LogRepositoryImpl>()
        singleBy<LogService, LogServiceImpl>()
        single<MockLogger>()
    }

    val modules = listOf(setupModule, logModule)
}
