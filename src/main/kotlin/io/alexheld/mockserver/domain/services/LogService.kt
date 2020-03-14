package io.alexheld.mockserver.domain.services

import io.alexheld.mockserver.serialization.*

interface LogService {

    fun list(): List<YamlLog>
    fun delete(id: String): YamlLog?
    fun add(log: YamlLog): YamlLog
    fun add(factory: (YamlLogFactory) -> YamlLog) = add(factory(YamlLogFactory))
}
