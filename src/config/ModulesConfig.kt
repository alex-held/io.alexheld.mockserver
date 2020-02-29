package io.alexheld.mockserver.config

import io.alexheld.mockserver.domain.repositories.*
import io.alexheld.mockserver.domain.services.*
import org.koin.core.*
import org.koin.core.module.*
import org.koin.dsl.*
import org.koin.experimental.builder.*


object ModulesConfig {

    @JvmStatic
    val setupModule: Module = module {
        singleBy<SetupService, SetupServiceImpl>()
        singleBy<SetupRepository, SetupRepositoryImpl>()
    }

    fun registerModules(koin: KoinApplication): KoinApplication {
        return koin.modules(setupModule)
    }
}
