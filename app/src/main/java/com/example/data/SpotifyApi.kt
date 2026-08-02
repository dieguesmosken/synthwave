package com.example.data

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

// Spotify API response models
data class SpotifySearchResponse(val tracks: SpotifyTracks)
data class SpotifyTracks(val items: List<SpotifyTrack>)
data class SpotifyTrack(
    val id: String,
    val name: String,
    val artists: List<SpotifyArtist>,
    val album: SpotifyAlbum,
    val preview_url: String?
)
data class SpotifyArtist(val name: String)
data class SpotifyAlbum(val images: List<SpotifyImage>)
data class SpotifyImage(val url: String)

interface SpotifyApi {
    @GET("v1/search")
    suspend fun searchTracks(
        @Header("Authorization") token: String,
        @Query("q") query: String,
        @Query("type") type: String = "track",
        @Query("limit") limit: Int = 20
    ): SpotifySearchResponse
}
