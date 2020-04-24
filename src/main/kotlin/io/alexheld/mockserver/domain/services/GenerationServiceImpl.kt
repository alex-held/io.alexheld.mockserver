package io.alexheld.mockserver.domain.services

import java.time.*


class GenerationServiceImpl(override val strategy: GenerationStrategy = GenerationStrategy.Default) : GenerationService {


    companion object {

        val Constant_Debug_Timestamp: Instant = Instant.EPOCH

        fun createDebugGeneration(strategy: GenerationStrategy = GenerationStrategy.Debug_Constant): GenerationService {
            return GenerationServiceImpl(strategy)
        }
    }

    enum class GenerationStrategy(val isDebug: Boolean) {
        Default(false),
        Debug_Constant(true),
    }

    private var debugInstant: Instant = Instant.parse("0001-01-01T00:00:00Z")

    override fun getTimestamp(): Instant {
        return when (strategy) {
            GenerationStrategy.Default -> Instant.now()
            GenerationStrategy.Debug_Constant -> Constant_Debug_Timestamp
        }
    }
}

