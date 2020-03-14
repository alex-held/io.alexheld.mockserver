package io.alexheld.mockserver.domain.repositories

import io.alexheld.mockserver.serialization.*

class LogRepositoryImpl : LogRepository {

    private val logs: MutableMap<String, YamlLog> = mutableMapOf()
    override fun list(): List<YamlLog> = logs.values.toList()
    override fun delete(id: String): YamlLog? = logs.remove(id)

    override fun add(log: YamlLog): YamlLog {
        logs[log.id] = log
        return log
    }

}
