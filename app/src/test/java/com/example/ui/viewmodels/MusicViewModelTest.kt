package com.example.ui.viewmodels

import com.example.data.SpotifyAlbum
import com.example.data.SpotifyApi
import com.example.data.SpotifyArtist
import com.example.data.SpotifyImage
import com.example.data.SpotifySearchResponse
import com.example.data.SpotifyTrack
import com.example.data.SpotifyTracks
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class MusicViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    class FakeSpotifyApi : SpotifyApi {
        var shouldThrowException = false
        var lastQuery: String? = null
        var lastToken: String? = null

        override suspend fun searchTracks(
            token: String,
            query: String,
            type: String,
            limit: Int
        ): SpotifySearchResponse {
            lastToken = token
            lastQuery = query
            if (shouldThrowException) {
                throw IOException("Network error")
            }
            val track = SpotifyTrack(
                id = "123",
                name = "Test Track",
                artists = listOf(SpotifyArtist("Test Artist")),
                album = SpotifyAlbum(listOf(SpotifyImage("url"))),
                preview_url = "preview"
            )
            return SpotifySearchResponse(SpotifyTracks(listOf(track)))
        }
    }

    @Test
    fun `search with blank query clears results`() = runTest {
        val fakeApi = FakeSpotifyApi()
        val viewModel = MusicViewModel(fakeApi)

        // First set some results
        fakeApi.shouldThrowException = false
        viewModel.search("test")
        assertTrue(viewModel.searchResults.value.isNotEmpty())

        // Then search with blank
        viewModel.search("   ")

        assertTrue(viewModel.searchResults.value.isEmpty())
        assertFalse(viewModel.isLoading.value)
    }

    @Test
    fun `search with valid query updates results`() = runTest {
        val fakeApi = FakeSpotifyApi()
        val viewModel = MusicViewModel(fakeApi)
        viewModel.setToken("TEST_TOKEN")

        viewModel.search("song")

        assertEquals("Bearer TEST_TOKEN", fakeApi.lastToken)
        assertEquals("song", fakeApi.lastQuery)
        assertEquals(1, viewModel.searchResults.value.size)
        assertEquals("Test Track", viewModel.searchResults.value[0].name)
        assertFalse(viewModel.isLoading.value)
    }

    @Test
    fun `search with valid query but api throws exception falls back to mock data`() = runTest {
        val fakeApi = FakeSpotifyApi()
        fakeApi.shouldThrowException = true
        val viewModel = MusicViewModel(fakeApi)

        viewModel.search("error query")

        assertEquals(1, viewModel.searchResults.value.size)
        assertEquals("Mock Search Result - error query", viewModel.searchResults.value[0].name)
        assertFalse(viewModel.isLoading.value)
    }
}
