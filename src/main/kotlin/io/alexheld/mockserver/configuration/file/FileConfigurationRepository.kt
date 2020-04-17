package io.alexheld.mockserver.configuration.file

import com.fasterxml.jackson.databind.*
import io.alexheld.mockserver.domain.models.*
import io.alexheld.mockserver.domain.services.*
import io.alexheld.mockserver.serialization.*

class FileConfigurationRepository(val filepath: String, private val fileSystem: FileSystemService, val gen: GenerationService) {

    private fun deserializeSetups(yaml: String): List<Setup> {
        val reader = Yaml.mapper
            .setInjectableValues(
                InjectableValues.Std()
                    .addValue(GenerationService::class.java, gen))
            .readerFor(Setup::class.java)

        return reader.readValues<Setup>(yaml).readAll()
    }


    fun get(): FileConfiguration {
        val config = FileConfiguration()
        val fileContent = fileSystem.readFile(filepath)
        val setups = deserializeSetups(fileContent)
        config.setups.addAll(setups)
        return config
    }
}
