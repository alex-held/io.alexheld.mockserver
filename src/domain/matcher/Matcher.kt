package io.alexheld.mockserver.domain.matcher

import io.alexheld.mockserver.domain.models.*

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


fun <TKey, TInner> MutableMap<TKey, TInner>.without(map: Map<TKey, TInner>?): Map<TKey, TInner> {
    if (map == null) return this
    for ((key, value) in map) {
        remove(key, value)
    }
    return this
}

fun Request.matches(other: Request?): Boolean {

    if (other == null) return true
    if (method != null && method != other.method) return false
    if (path != null && path != other.path) return false
    if (body != null && !body.matches(other.body)) return false

    if (!cookies.isNullOrEmpty()) {
        val unmatchedCookies = cookies.without(other.cookies)
        if (unmatchedCookies.any()) return false
    }

    if (!headers.isNullOrEmpty()) {
        val unmatchedHeaders = headers.without(other.headers)
        if (unmatchedHeaders.any()) return false
    }

    return true
}
