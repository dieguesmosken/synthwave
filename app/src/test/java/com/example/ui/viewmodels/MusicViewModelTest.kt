package com.example.ui.viewmodels

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MusicViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `search with blank query clears results immediately`() = runTest {
        // Arrange
        val viewModel = MusicViewModel()

        // We use reflection to set the internal state to avoid depending on network logic
        // because we are testing the synchronous early-return path for blank queries.
        val searchResultsField = MusicViewModel::class.java.getDeclaredField("_searchResults")
        searchResultsField.isAccessible = true
        @Suppress("UNCHECKED_CAST")
        val stateFlow = searchResultsField.get(viewModel) as kotlinx.coroutines.flow.MutableStateFlow<List<com.example.data.SpotifyTrack>>
        stateFlow.value = listOf(
            com.example.data.SpotifyTrack(
                id = "1",
                name = "Test",
                artists = emptyList(),
                album = com.example.data.SpotifyAlbum(emptyList()),
                preview_url = null
            )
        )

        assertFalse("Expected results to not be empty after initial state manipulation", viewModel.searchResults.value.isEmpty())

        // Act - search with blank query
        viewModel.search("   ")
        advanceUntilIdle()

        // Assert - verify it cleared the results
        assertTrue("Search results should be empty for a blank query", viewModel.searchResults.value.isEmpty())
    }
}
