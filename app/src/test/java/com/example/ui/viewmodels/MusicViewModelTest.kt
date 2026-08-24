package com.example.ui.viewmodels

import com.example.data.SpotifyAlbum
import com.example.data.SpotifyApi
import com.example.data.SpotifyArtist
import com.example.data.SpotifyImage
import com.example.data.SpotifySearchResponse
import com.example.data.SpotifyTrack
import com.example.data.SpotifyTracks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
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
    fun `search with exception populates searchResults with mock data`() = runTest {
        // Arrange
        val viewModel = MusicViewModel()

        // Inject a fake SpotifyApi that throws an exception
        val failingApi = object : SpotifyApi {
            override suspend fun searchTracks(
                token: String,
                query: String,
                type: String,
                limit: Int
            ): SpotifySearchResponse {
                throw RuntimeException("Network Error")
            }
        }
        viewModel.spotifyApi = failingApi

        val query = "testQuery"

        // Act
        viewModel.search(query)
        testDispatcher.scheduler.advanceUntilIdle() // Wait for coroutine to finish

        // Assert
        val results = viewModel.searchResults.value
        assertEquals(1, results.size)

        val mockItem = results[0]
        assertEquals("1", mockItem.id)
        assertEquals("Mock Search Result - $query", mockItem.name)

        assertEquals(1, mockItem.artists.size)
        assertEquals("Mock Artist", mockItem.artists[0].name)

        assertEquals(1, mockItem.album.images.size)
        assertEquals("https://picsum.photos/seed/$query/100", mockItem.album.images[0].url)

        assertEquals(null, mockItem.preview_url)
    }
}
