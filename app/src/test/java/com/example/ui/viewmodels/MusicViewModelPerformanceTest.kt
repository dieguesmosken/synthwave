package com.example.ui.viewmodels

import org.junit.Test
import kotlin.system.measureTimeMillis

class MusicViewModelPerformanceTest {

    @Test
    fun benchmarkViewModelInstantiation() {
        // Warm up
        for (i in 1..10) {
            MusicViewModel()
        }

        val iterations = 100
        val timeInMillis = measureTimeMillis {
            for (i in 1..iterations) {
                MusicViewModel()
            }
        }

        println("BENCHMARK: Instantiating MusicViewModel $iterations times took $timeInMillis ms")
        // Fail the test if it takes an unreasonably long time, or just let it print
    }
}
