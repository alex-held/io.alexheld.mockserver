package io.alexheld.mockserver.domain.matcher

import com.fasterxml.jackson.module.kotlin.*
import io.alexheld.mockserver.domain.models.*
import java.io.*


fun RequestBody.matches(other: RequestBody?): Boolean {
    if (other?.body == null) return false

    return when (body) {
        null -> true
        else -> when (exact) {
            true -> body == other.body
            else -> body.contains(other.body)
        }
    }
}


fun MutableMap<String, String>.withoutCookies(map: Map<String, String>?): Map<String, String> {

    val safeMap = map ?: emptyMap()
    val removableList = this.toList().toMutableList()
    removableList.removeIf {
        try {
            println(jacksonObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(removableList))
            println("Attempting to remove:\nKey=${it.first}; Value=${jacksonObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(it.second)}")
            return@removeIf removableList.contains(it)
        } catch (e: Exception) {
            println("Message=${e.localizedMessage}")
            return@removeIf false
        }
    }
    return removableList.toMap().toMutableMap()
}

fun MutableMap<String, MutableSet<String>>.withoutHeaders(map: Map<String, MutableSet<String>>?): Map<String, MutableSet<String>> {

    val safeMap = map ?: emptyMap()
    val removableList = this.toList().toMutableList()
    removableList.removeIf {
        try {
            println(jacksonObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(removableList))
            println("Attempting to remove:\nKey=${it.first}; Value=${jacksonObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(it.second)}")

            val mutableMap = safeMap.toMutableMap()
            if (mutableMap.containsKey(it.first)) {
                val headerValues = mutableMap[it.first] ?: throw InvalidObjectException("")
                return@removeIf it.second.containsAll(headerValues)
            }

            return@removeIf false
        } catch (e: Exception) {
            println("Message=${e.localizedMessage}")
            return@removeIf false
        }
    }
    return removableList.toMap().toMutableMap()
}


fun Request.matches(other: Request?): Boolean {

    if (other == null) return true
    if (method != null && method != other.method) return false
    if (path != null && path != other.path) return false
    if (body != null && !body.matches(other.body)) return false

    if (!cookies.isNullOrEmpty()) {
        val unmatchedCookies = cookies.withoutCookies(other.cookies)
        if (unmatchedCookies.any()) return false
    }

    if (!headers.isNullOrEmpty()) {
        val unmatchedHeaders = headers.withoutHeaders(other.headers)
        if (unmatchedHeaders.any()) return false
    }

    return true
}
