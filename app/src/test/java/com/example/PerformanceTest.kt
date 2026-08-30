package com.example

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.ui.test.junit4.createComposeRule
import org.robolectric.RobolectricTestRunner
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.system.measureTimeMillis
import android.util.Log

@RunWith(RobolectricTestRunner::class)
class PerformanceTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun benchmarkEagerColumn() {
        val largeList = List(1000) { "Item $it" }
        val time = measureTimeMillis {
            composeTestRule.setContent {
                LazyColumn {
                    item {
                        Column {
                            largeList.forEach { item ->
                                Text(text = item)
                            }
                        }
                    }
                }
            }
            composeTestRule.waitForIdle()
        }
        Log.d("PerformanceTest", "Eager Column time: $time ms")
        println("Eager Column time: $time ms")
    }

    @Test
    fun benchmarkLazyColumn() {
        val largeList = List(1000) { "Item $it" }
        val time = measureTimeMillis {
            composeTestRule.setContent {
                LazyColumn {
                    items(largeList) { item ->
                        Text(text = item)
                    }
                }
            }
            composeTestRule.waitForIdle()
        }
        Log.d("PerformanceTest", "Lazy Column time: $time ms")
        println("Lazy Column time: $time ms")
    }
}
