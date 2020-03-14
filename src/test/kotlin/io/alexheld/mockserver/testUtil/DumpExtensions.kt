package io.alexheld.mockserver.testUtil

fun String.dump(name: String) {
    val size = name.toList().map { c -> "" }.joinToString("-")

    println("--- $name ---")
    println(this)
    println("----$size----")
}
