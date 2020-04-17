package io.alexheld.mockserver.testUtil

import java.io.*

interface WithTestResources {

    fun getRootPath(): String

    fun streamResourceFile(fileName: String): InputStream = this::class.java.getResourceAsStream("${getRootPath()}/$fileName")

    fun readResource(file: String): String {
        val stream = streamResourceFile(file)
        val reader = InputStreamReader(stream)
        return reader.readText()
    }
}
