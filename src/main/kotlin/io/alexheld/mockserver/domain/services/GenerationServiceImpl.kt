package io.alexheld.mockserver.domain.services

import java.time.*
import java.util.*


class GenerationServiceImpl() : GenerationService {

    constructor(generationStrategy: GenerationStrategy) : this(){
        this.generationStrategy = generationStrategy
    }

    companion object {

        const val Constant_Debug_Id: String = "00000000-0000-0000-0000-000000000000"
        val Constant_Debug_Timestamp: Instant = Instant.EPOCH

        fun createDebugGeneration(strategy: GenerationStrategy = GenerationStrategy.Debug_Constant): GenerationService {
            return GenerationServiceImpl(strategy)
        }
    }

    enum class GenerationStrategy(val isDebug: Boolean) {
        Default(false),
        Debug_Constant(true),
        Debug_Counter(true)
    }

    private var idCounter: Int = 1
    private var generationStrategy: GenerationStrategy = GenerationStrategy.Default
    private var debugInstant: Instant = Instant.parse("0001-01-01T00:00:00Z")
    private fun resolveIdString(): String = idCounter.toString()

    override fun getId(): String {
        return when (generationStrategy) {
            GenerationStrategy.Default -> UUID.randomUUID().toString()
            GenerationStrategy.Debug_Constant -> Constant_Debug_Id
            GenerationStrategy.Debug_Counter -> {
                val id = resolveIdString()
                ++idCounter
                id
            }
        }
    }

    override fun getTimestamp(): Instant {
        return when (generationStrategy) {
            GenerationStrategy.Default -> Instant.now()
            GenerationStrategy.Debug_Constant -> Constant_Debug_Timestamp
            GenerationStrategy.Debug_Counter -> {
                val ts = debugInstant
                debugInstant = ts + Duration.ofDays(365)
                ts
            }
        }
    }
}

