package com.example.data

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.net.HttpURLConnection

class SpotifyApiTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var api: SpotifyApi

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        api = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(SpotifyApi::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `searchTracks parses correct JSON response and constructs URL correctly`() = runBlocking {
        // Prepare mock JSON response
        val mockResponseJson = """
            {
              "tracks": {
                "items": [
                  {
                    "id": "1",
                    "name": "Test Song",
                    "artists": [
                      {
                        "name": "Test Artist"
                      }
                    ],
                    "album": {
                      "images": [
                        {
                          "url": "https://example.com/image.jpg"
                        }
                      ]
                    },
                    "preview_url": "https://example.com/preview.mp3"
                  }
                ]
              }
            }
        """.trimIndent()

        // Enqueue mock response
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(mockResponseJson)
        )

        // Make API call
        val token = "Bearer test_token"
        val query = "test query"
        val response = api.searchTracks(token, query)

        // Verify request
        val request = mockWebServer.takeRequest()
        assertEquals("/v1/search?q=test%20query&type=track&limit=20", request.path)
        assertEquals(token, request.getHeader("Authorization"))
        assertEquals("GET", request.method)

        // Verify parsed response
        assertEquals(1, response.tracks.items.size)
        val track = response.tracks.items[0]
        assertEquals("1", track.id)
        assertEquals("Test Song", track.name)
        assertEquals("https://example.com/preview.mp3", track.preview_url)
        assertEquals(1, track.artists.size)
        assertEquals("Test Artist", track.artists[0].name)
        assertEquals(1, track.album.images.size)
        assertEquals("https://example.com/image.jpg", track.album.images[0].url)
    }
}
