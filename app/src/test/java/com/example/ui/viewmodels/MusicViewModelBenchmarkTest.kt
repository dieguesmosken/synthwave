package com.example.ui.viewmodels

import org.junit.Test
import kotlin.system.measureTimeMillis

class MusicViewModelBenchmarkTest {

    @Test
    fun benchmarkViewModelInstantiation() {
        // Warmup
        for (i in 1..10) {
            val vm = MusicViewModel()
        }

        // Measure
        val iterations = 1000
        val time = measureTimeMillis {
            for (i in 1..iterations) {
                val vm = MusicViewModel()
            }
        }
        println("MusicViewModel instantiation time for $iterations iterations: $time ms")
        println("Average time per instantiation: ${time / iterations.toDouble()} ms")
    }
}
