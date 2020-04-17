package io.alexheld.mockserver.configuration.file

import com.fasterxml.jackson.databind.*
import io.alexheld.mockserver.domain.models.*
import io.alexheld.mockserver.logging.*
import io.alexheld.mockserver.serialization.*
import java.io.*

data class FileConfiguration(
    val setups: MutableList<Setup> = mutableListOf()
)

interface FileSystemService {
    fun readFile(path: String): String
}


class FileSystemServiceImpl : FileSystemService {
    override fun readFile(path: String): String = File(path).readText()
}

class FileConfigurationRepository(val filepath: String, val fileSystem: FileSystemService, val gen: GenerationService) {

    private fun deserializeSetups(yaml: String): List<Setup> {
        val reader = Yaml.mapper
            .setInjectableValues(InjectableValues.Std().addValue(GenerationService::class.java, gen))
            .readerFor(Setup::class.java)

        val setups = reader.readValues<Setup>(yaml).readAll()
        return setups
    }


    fun get(): FileConfiguration {
        val config = FileConfiguration()
        val fileContent = fileSystem.readFile(filepath)
        val setups = deserializeSetups(fileContent)
        config.setups.addAll(setups)
        return config
    }
}
