package io.alexheld.mockserver.domain.services

import org.litote.kmongo.*
import org.litote.kmongo.id.*
import java.time.*

interface GenerationService {
    companion object {
        const val Constant_Debug_Id: String = "5ea36d460adb9d50a96ec60c"
    }
    val strategy: GenerationServiceImpl.GenerationStrategy

    fun<T> newId(id: String? = null): Id<T> {
        return if (id == null)
            if (strategy == GenerationServiceImpl.GenerationStrategy.Debug_Constant)
                IdGenerator.defaultGenerator.create(Constant_Debug_Id).cast()
            else
                IdGenerator.defaultGenerator.generateNewId()
        else
            IdGenerator.defaultGenerator.create(id).cast()
    }
    fun getTimestamp(): Instant
    fun getTimestampString(): String = Instant.ofEpochSecond(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)).toString()

}
