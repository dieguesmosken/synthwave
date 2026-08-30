package com.example.ui.viewmodels

import com.example.MainDispatcherRule
import com.example.data.SpotifyAlbum
import com.example.data.SpotifyApi
import com.example.data.SpotifyArtist
import com.example.data.SpotifyImage
import com.example.data.SpotifySearchResponse
import com.example.data.SpotifyTrack
import com.example.data.SpotifyTracks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MusicViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: MusicViewModel
    private lateinit var mockSpotifyApi: SpotifyApi

    @Before
    fun setUp() {
        mockSpotifyApi = mockk()
        viewModel = MusicViewModel().apply {
            spotifyApi = mockSpotifyApi
            setToken("TEST_TOKEN")
        }
    }

    @Test
    fun `search with empty query clears results`() = runTest {
        viewModel.search("")

        advanceUntilIdle()
        val results = viewModel.searchResults.value
        assertTrue(results.isEmpty())
        assertFalse(viewModel.isLoading.value)
    }

    @Test
    fun `search with valid query updates results`() = runTest {
        val mockTrack = SpotifyTrack(
            id = "1",
            name = "Test Track",
            artists = listOf(SpotifyArtist("Test Artist")),
            album = SpotifyAlbum(listOf(SpotifyImage("url"))),
            preview_url = "preview_url"
        )
        val mockResponse = SpotifySearchResponse(SpotifyTracks(listOf(mockTrack)))

        coEvery {
            mockSpotifyApi.searchTracks(any(), eq("test query"), any(), any())
        } returns mockResponse

        viewModel.search("test query")

        advanceUntilIdle()
        val results = viewModel.searchResults.value
        assertEquals(1, results.size)
        assertEquals("Test Track", results[0].name)
        assertFalse(viewModel.isLoading.value)
    }

    @Test
    fun `search error returns fallback mock data`() = runTest {
        coEvery {
            mockSpotifyApi.searchTracks(any(), eq("error query"), any(), any())
        } throws Exception("Network error")

        viewModel.search("error query")

        advanceUntilIdle()
        val results = viewModel.searchResults.value
        assertEquals(1, results.size)
        assertEquals("Mock Search Result - error query", results[0].name)
        assertFalse(viewModel.isLoading.value)
    }
}
