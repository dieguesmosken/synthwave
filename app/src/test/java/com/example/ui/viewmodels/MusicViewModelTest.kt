package com.example.ui.viewmodels

import org.junit.Assert.assertTrue
import org.junit.Test

class MusicViewModelTest {

    @Test
    fun `search with blank query sets searchResults to empty list`() {
        // Arrange
        val viewModel = MusicViewModel()

        // Act
        viewModel.search("   ")

        // Assert
        assertTrue("searchResults should be empty for a blank query", viewModel.searchResults.value.isEmpty())
    }

    @Test
    fun `search with empty query sets searchResults to empty list`() {
        // Arrange
        val viewModel = MusicViewModel()

        // Act
        viewModel.search("")

        // Assert
        assertTrue("searchResults should be empty for an empty query", viewModel.searchResults.value.isEmpty())
    }
}
