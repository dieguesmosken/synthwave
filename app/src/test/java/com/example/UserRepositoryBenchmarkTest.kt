package com.example

import com.example.data.UserRepository
import org.junit.Test
import kotlin.system.measureTimeMillis

class UserRepositoryBenchmarkTest {

    @Test
    fun benchmarkUserRepositoryCreation() {
        // Warm up the class loader if needed (though we want to measure actual instantiation time)
        val iterations = 10
        var totalTime = 0L

        for (i in 1..iterations) {
            val time = measureTimeMillis {
                val repository = UserRepository()
                // Just force evaluation if it was lazy (it's not initially)
            }
            println("Iteration $i: $time ms")
            totalTime += time
        }

        println("Average creation time over $iterations iterations: ${totalTime / iterations} ms")
    }
}
