package com.example.ui.viewmodels

import com.example.data.SpotifyApi
import com.example.data.SpotifySearchResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MusicViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `search with API exception uses fallback mock data`() = runTest {
        // Arrange
        val mockQuery = "test query"

        val failingSpotifyApi = object : SpotifyApi {
            override suspend fun searchTracks(
                token: String,
                query: String,
                type: String,
                limit: Int
            ): SpotifySearchResponse {
                throw Exception("Mock API Error")
            }
        }

        val viewModel = MusicViewModel(injectedSpotifyApi = failingSpotifyApi)

        // Act
        viewModel.search(mockQuery)

        // Advance coroutines
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        val searchResults = viewModel.searchResults.value
        assertEquals(1, searchResults.size)

        val firstResult = searchResults.first()
        assertEquals("1", firstResult.id)
        assertEquals("Mock Search Result - $mockQuery", firstResult.name)
        assertEquals(1, firstResult.artists.size)
        assertEquals("Mock Artist", firstResult.artists.first().name)
        assertEquals(1, firstResult.album.images.size)
        assertEquals("https://picsum.photos/seed/$mockQuery/100", firstResult.album.images.first().url)
        assertNull(firstResult.preview_url)
    }
}
