package io.alexheld.mockserver.domain.services

interface FileSystemService {
    fun readFile(path: String): String
}

