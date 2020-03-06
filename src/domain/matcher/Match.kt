package io.alexheld.mockserver.domain.matcher

import io.alexheld.mockserver.domain.models.*


class Match {

    private val _errors: MutableList<MatchError<*>> = mutableListOf()
    val errors: List<MatchError<*>> = _errors

    fun isSuccess() = !errors.any()
    fun <T> addMatchError(member: String, expected: T, value: T) = _errors.add(MatchError(member, expected, value))


    companion object {

        fun requests(a: Request, b: Request): Match {
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
    if (body == null) return true
    if (other?.body == null) return false
    if (exact) return body == other.body
    return body.contains(other.body)
}


fun MutableMap<String, String>.withoutCookies(map: Map<String, String>?): Map<String, String> {
    return this
        .toList()
        .filter {
            try {
                return@filter (map ?: emptyMap()).getValue(it.first) != it.second
            } catch (e: Exception) {
                return@filter true
            }
        }
        .toMap()
}

fun MutableMap<String, MutableSet<String>>.withoutHeaders(map: Map<String, MutableSet<String>>?): Map<String, MutableSet<String>> {
    return this
        .toList()
        .filter {
            try {
                return@filter !it.second.containsAll((map ?: emptyMap())[it.first]!!)
            } catch (e: Exception) {
                return@filter true
            }
        }
        .toMap()
}


