val list = listOf("home", "search", "library", "profile")
val set = setOf("home", "search", "library", "profile")

fun benchmark(name: String, action: () -> Unit) {
    // Warmup
    for (i in 0..10000) {
        action()
    }

    val start = System.nanoTime()
    for (i in 0..10000000) {
        action()
    }
    val end = System.nanoTime()
    println("$name: ${(end - start) / 1000000.0} ms")
}

val currentRoute = "profile"

benchmark("List Any") {
    list.any { it == currentRoute }
}

benchmark("Set Contains") {
    set.contains(currentRoute)
}
