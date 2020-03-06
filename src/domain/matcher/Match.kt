package io.alexheld.mockserver.domain.matcher

import com.fasterxml.jackson.module.kotlin.*
import io.alexheld.mockserver.domain.models.*
import java.io.*


class Match(private val _errors: MutableList<MatchError<*>>) {
    private constructor() : this(mutableListOf())

    public fun isSuccess() = !errors.any()
    public val errors: List<MatchError<*>> = _errors

    fun <T> addMatchError(member: String, expected: T, value: T) = _errors.add(MatchError(member, expected, value))


    companion object {

        fun Requests(a: Request, b: Request): Match {
            val match = Match()

            if (a.method != null && a.method != b.method)
                match.addMatchError(Request::method.name, a.method, b.method)

            if (a.path != null && a.path != b.path)
                match.addMatchError(Request::path.name, a.path, b.path)

            if (a.body != null && !a.body.matches(b.body))
                match.addMatchError(Request::body.name, a.body, b.body)

            if (!a.cookies.isNullOrEmpty() && a.cookies.withoutCookies(b.cookies).any()) {
                match.addMatchError(Request::cookies.name, a.cookies, b.cookies)
            }

            if (!a.headers.isNullOrEmpty() && a.headers.withoutHeaders(b.headers).any()) {
                match.addMatchError(Request::headers.name, a.headers, b.headers)
            }

            return match
        }
    }
}


fun RequestBody.matches(other: RequestBody?): Boolean {
    if(body == null) return true
    if (other?.body == null) return false
    if (exact) return body == other.body
    return body.contains(other.body)
}


fun MutableMap<String, String>.withoutCookies(map: Map<String, String>?): Map<String, String> {

    val safeMap = map ?: emptyMap()
    val removableList = this.toList().toMutableList()
    removableList.removeIf {
        try {
            if (!safeMap.containsKey(it.first))  return@removeIf false
            val value = safeMap.getValue(it.first)
            val equals = value == it.second
            return@removeIf equals
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


