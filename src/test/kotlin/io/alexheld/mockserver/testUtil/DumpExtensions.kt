package io.alexheld.mockserver.testUtil

fun String.dump(name: String) {
    val size = name.toList().joinToString("-") { _ -> "" }

    println("--- $name ---")
    println(this)
    println("----$size----")
}

