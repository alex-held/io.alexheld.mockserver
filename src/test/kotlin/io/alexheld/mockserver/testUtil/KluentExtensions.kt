package io.alexheld.mockserver.testUtil

import org.amshove.kluent.*


fun <I : Iterable<*>> I.shouldBeEmptyWhen(condition: Boolean) = shouldHaveSizeWhen(condition, 0, 1)

fun <I : Iterable<*>> I.shouldHaveSizeWhen(condition: Boolean, expectedSizeTrue: Int, expectedSizeFalse: Int) = apply {
    val actualSize = this.count()
    var expectedSize = expectedSizeTrue
    if (!condition) expectedSize = expectedSizeFalse
    should("Expected collection size to be $expectedSize but was $actualSize") {
        actualSize == expectedSize
    }
}

fun String.shouldBeEqualWhenTrimmed(expected: String) {
    val formatted = this
    println("Expected:\n----------------------------\n\n$expected\n")
    println("Formatted:\n----------------------------\n\n$formatted\n")
    formatted.trim(' ').trim('\n').shouldBeEqualTo(expected)
}
