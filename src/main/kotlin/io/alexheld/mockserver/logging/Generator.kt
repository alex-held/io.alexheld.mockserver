package io.alexheld.mockserver.logging

import java.time.*
import java.util.*


interface GenerationService {
    fun getId(): String
    fun getTimestamp(): Instant
    fun getTimestampString(): String = getTimestamp().toString()
}

class Generator : GenerationService {

   companion object {

       var Constant_Debug_Id: String = "00000000-0000-0000-0000-000000000000"
       var Constant_Debug_Timestamp: Instant = Instant.EPOCH

       fun createDebugGeneration(strategy: GenerationStrategy = GenerationStrategy.Debug_Constant): GenerationService {
           val generationService = Generator()
           generationService.generationStrategy = strategy
           return generationService
       }
   }

    enum class GenerationStrategy(val isDebug: Boolean) {
        Default(false),
        Debug_Constant(true),
        Debug_Counter(true)
    }

    private var idCounter: Int = 1
    var generationStrategy: GenerationStrategy = GenerationStrategy.Default
    private var debugInstant: Instant = Instant.parse("0001-01-01T00:00:00Z")

    private fun resolveIdString(): String = idCounter.toString()

    override fun getId(): String {
       return when(generationStrategy){
            GenerationStrategy.Default -> UUID.randomUUID().toString()
            GenerationStrategy.Debug_Constant -> Constant_Debug_Id
            GenerationStrategy.Debug_Counter -> {
                val id= resolveIdString()
                ++idCounter
                id
            }
        }
    }

    override fun getTimestamp(): Instant {
        return when(generationStrategy){
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

