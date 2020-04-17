package io.alexheld.mockserver.configuration.file

import io.alexheld.mockserver.domain.models.*

data class FileConfiguration(
    val setups: MutableList<Setup> = mutableListOf()
)




