package io.alexheld.mockserver.domain.services

import java.io.*

class FileSystemServiceImpl : FileSystemService {
    override fun readFile(path: String): String = File(path).readText()
}
