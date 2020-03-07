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
