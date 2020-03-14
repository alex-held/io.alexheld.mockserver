package io.alexheld.mockserver.domain.services

import io.alexheld.mockserver.config.*
import io.alexheld.mockserver.domain.repositories.*
import io.alexheld.mockserver.logging.*
import io.alexheld.mockserver.serialization.*
import org.apache.logging.log4j.*
import org.apache.logging.log4j.kotlin.*

class LogServiceImpl(private val logRepository: LogRepository) : LogService {

    override fun list(): List<YamlLog> = logRepository.list()
    override fun delete(id: String): YamlLog? = logRepository.delete(id)
    override fun add(log: YamlLog) = logRepository.add(log)

    fun tryLog(log: YamlLog, level: Level = Level.INFO): String? {
        if (MockConfig.LOG_LEVEL <= level)
            return null
        return log.writeLogMessage(level)
    }
}


public fun YamlLog.writeLogMessage(level: Level = Level.INFO): String {
    val formatted = YAMLFormatter.serialize(this)
    logger().log(level, formatted)
    return formatted
}
