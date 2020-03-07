package io.alexheld.mockserver.domain.matcher

data class MatchError<T>(val member: String, val expected: T, val value: T)
