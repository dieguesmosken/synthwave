package com.example

import com.example.ui.viewmodels.MusicViewModel
import org.junit.Test
import kotlin.system.measureTimeMillis
import java.io.File

class MusicViewModelBenchmarkTest {
    @Test
    fun benchmarkViewModelInit() {
        val iterations = 100

        // Warmup
        for (i in 1..10) {
            MusicViewModel()
        }

        val time = measureTimeMillis {
            for (i in 1..iterations) {
                MusicViewModel()
            }
        }
        val result = "MusicViewModel initialization took $time ms for $iterations iterations.\n" +
                     "Average time: ${time.toDouble() / iterations} ms per initialization.\n"
        println(result)
        File("benchmark_result.txt").writeText(result)
    }
}
