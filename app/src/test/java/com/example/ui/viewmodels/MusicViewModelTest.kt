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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
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

    private class FakeSpotifyApi(var shouldFail: Boolean = false) : SpotifyApi {
        override suspend fun searchTracks(
            token: String,
            query: String,
            type: String,
            limit: Int
        ): SpotifySearchResponse {
            if (shouldFail) {
                throw RuntimeException("Simulated API Error")
            }

            val mockTrack = SpotifyTrack(
                id = "test_id",
                name = "Test Track $query",
                artists = listOf(SpotifyArtist("Test Artist")),
                album = SpotifyAlbum(listOf(SpotifyImage("http://test.url"))),
                preview_url = "http://test.preview"
            )
            return SpotifySearchResponse(SpotifyTracks(listOf(mockTrack)))
        }
    }

    @Test
    fun `search with blank query returns empty list`() = runTest(testDispatcher) {
        val viewModel = MusicViewModel(FakeSpotifyApi())

        viewModel.search("   ")

        assertEquals(emptyList<SpotifyTrack>(), viewModel.searchResults.value)
        assertFalse(viewModel.isLoading.value)
    }

    @Test
    fun `search with valid query returns successful results`() = runTest(testDispatcher) {
        val viewModel = MusicViewModel(FakeSpotifyApi(shouldFail = false))

        viewModel.search("Valid Query")

        // Wait for coroutines to complete
        advanceUntilIdle()

        assertFalse(viewModel.isLoading.value)
        val results = viewModel.searchResults.value
        assertEquals(1, results.size)
        assertEquals("Test Track Valid Query", results[0].name)
    }

    @Test
    fun `search with failing API returns fallback mock data`() = runTest(testDispatcher) {
        val viewModel = MusicViewModel(FakeSpotifyApi(shouldFail = true))

        viewModel.search("Failing Query")

        // Wait for coroutines to complete
        advanceUntilIdle()

        assertFalse(viewModel.isLoading.value)
        val results = viewModel.searchResults.value
        assertEquals(1, results.size)
        assertEquals("Mock Search Result - Failing Query", results[0].name)
    }

    @Test
    fun `search updates loading state correctly`() = runTest(testDispatcher) {
        val viewModel = MusicViewModel(FakeSpotifyApi())

        val loadingStates = mutableListOf<Boolean>()
        val job = launch(kotlinx.coroutines.test.UnconfinedTestDispatcher(testScheduler)) {
            viewModel.isLoading.toList(loadingStates)
        }

        viewModel.search("Query")

        // With UnconfinedTestDispatcher for the collection job,
        // it starts collecting immediately.
        // We advance the coroutine exactly enough to run the internal task.
        advanceUntilIdle()

        // Should have captured initial (false), true, and false
        assertTrue(loadingStates.contains(true))
        assertEquals(false, viewModel.isLoading.value)

        job.cancel()
    }
}
