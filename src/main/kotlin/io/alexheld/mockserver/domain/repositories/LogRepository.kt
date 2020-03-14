package io.alexheld.mockserver.domain.repositories

import io.alexheld.mockserver.serialization.*

interface LogRepository {
    fun list(): List<YamlLog>
    fun delete(id: String): YamlLog?
    fun add(log: YamlLog): YamlLog
}
