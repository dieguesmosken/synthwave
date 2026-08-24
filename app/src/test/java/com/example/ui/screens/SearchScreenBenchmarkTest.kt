package com.example.ui.screens

import androidx.compose.ui.graphics.Color
import org.junit.Test
import kotlin.system.measureTimeMillis

class SearchScreenBenchmarkTest {

    val categories = listOf("Synthwave", "Pop", "Eletrônica", "Rock", "Hip Hop", "K-Pop", "Jazz", "Funk")
    val colorList = listOf(Color(0xFF0052CC), Color(0xFFFF007F), Color(0xFF9D00FF), Color(0xFFD32F2F), Color(0xFF4DD0E1), Color(0xFFFF9800), Color(0xFF8BC34A), Color(0xFFE91E63))

    val precomputedList = categories.zip(colorList)

    @Test
    fun benchmarkZipping() {
        // Warmup
        for (i in 0..10_000) {
            val a = categories.zip(colorList)
        }

        val iterations = 10_000_000
        val timeWithZip = measureTimeMillis {
            for (i in 0..iterations) {
                val result = categories.zip(colorList)
            }
        }

        // Warmup
        for (i in 0..10_000) {
            val a = precomputedList
        }

        val timePrecomputed = measureTimeMillis {
            for (i in 0..iterations) {
                val result = precomputedList
            }
        }

        println("Time with zip per recomposition simulation ($iterations iterations): $timeWithZip ms")
        println("Time with precomputed per recomposition simulation ($iterations iterations): $timePrecomputed ms")
    }
}
