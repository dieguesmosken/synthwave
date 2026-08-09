package com.example.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.SpotifyApi
import com.example.data.SpotifyTrack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class MusicViewModel(
    private val spotifyApi: SpotifyApi = createSpotifyApi()
) : ViewModel() {

    companion object {
        fun createSpotifyApi(): SpotifyApi {
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.spotify.com/")
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .client(OkHttpClient.Builder().build())
                .build()

            return retrofit.create(SpotifyApi::class.java)
        }
    }

    private val _searchResults = MutableStateFlow<List<SpotifyTrack>>(emptyList())
    val searchResults: StateFlow<List<SpotifyTrack>> = _searchResults

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // In a real application, you would fetch and refresh this token via Spotify's OAuth 2.0 flow
    private var accessToken = "MOCK_SPOTIFY_TOKEN"

    fun setToken(token: String) {
        this.accessToken = token
    }

    fun search(query: String) {
        if (query.isBlank()) {
            _searchResults.value = emptyList()
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            try {
                // If using a real token, it should be formatted as "Bearer $accessToken"
                // This is a placeholder since the token is mock
                val response = spotifyApi.searchTracks("Bearer $accessToken", query)
                _searchResults.value = response.tracks.items
            } catch (e: Exception) {
                e.printStackTrace()
                // Provide some fallback mock data if real API fails due to invalid token
                _searchResults.value = listOf(
                    SpotifyTrack(
                        id = "1",
                        name = "Mock Search Result - $query",
                        artists = listOf(com.example.data.SpotifyArtist("Mock Artist")),
                        album = com.example.data.SpotifyAlbum(listOf(com.example.data.SpotifyImage("https://picsum.photos/seed/$query/100"))),
                        preview_url = null
                    )
                )
            } finally {
                _isLoading.value = false
            }
        }
    }
}
