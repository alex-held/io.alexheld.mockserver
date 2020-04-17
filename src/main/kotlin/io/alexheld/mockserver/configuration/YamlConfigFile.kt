package io.alexheld.mockserver.configuration

import io.alexheld.mockserver.domain.models.*







open class SetupConfiguration : ArrayList<Setup>()


data class YamlConfigFile(val file: String) : SetupConfiguration()

