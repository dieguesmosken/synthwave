package com.example.ui.viewmodels

import com.example.data.SpotifyAlbum
import com.example.data.SpotifyApi
import com.example.data.SpotifyArtist
import com.example.data.SpotifyImage
import com.example.data.SpotifySearchResponse
import com.example.data.SpotifyTrack
import com.example.utils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class MusicViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `search handles exception and sets mock data`() = runTest {
        // Given
        val failingSpotifyApi = object : SpotifyApi {
            override suspend fun searchTracks(
                token: String,
                query: String,
                type: String,
                limit: Int
            ): SpotifySearchResponse {
                throw IOException("Network error")
            }
        }

        val viewModel = MusicViewModel(spotifyApi = failingSpotifyApi)
        val query = "Test Song"

        // When
        viewModel.search(query)

        // Then
        val results = viewModel.searchResults.value
        assertEquals(1, results.size)
        val track = results.first()
        assertEquals("1", track.id)
        assertEquals("Mock Search Result - $query", track.name)
        assertEquals("Mock Artist", track.artists.first().name)
        assertEquals("https://picsum.photos/seed/$query/100", track.album.images.first().url)
    }
}
