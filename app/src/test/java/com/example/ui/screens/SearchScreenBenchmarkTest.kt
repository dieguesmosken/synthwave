package com.example.ui.screens

import org.junit.Test
import kotlin.system.measureNanoTime

class SearchScreenBenchmarkTest {
    @Test
    fun benchmarkZip() {
        val categories = listOf("Synthwave", "Pop", "Eletrônica", "Rock", "Hip Hop", "K-Pop", "Jazz", "Funk")
        val colorList = listOf("0xFF0052CC", "0xFFFF007F", "0xFF9D00FF", "0xFFD32F2F", "0xFF4DD0E1", "0xFFFF9800", "0xFF8BC34A", "0xFFE91E63")
        val categoriesWithColors = categories.zip(colorList)

        // Warmup
        for (i in 1..10_000) {
            categories.zip(colorList)
            val x = categoriesWithColors
        }

        val iterations = 1_000_000

        val timeZip = measureNanoTime {
            for (i in 1..iterations) {
                val x = categories.zip(colorList)
            }
        }

        val timePreZipped = measureNanoTime {
            for (i in 1..iterations) {
                val x = categoriesWithColors
            }
        }

        println("Time with zip: ${timeZip / 1_000_000.0} ms")
        println("Time pre-zipped: ${timePreZipped / 1_000_000.0} ms")
    }
}
