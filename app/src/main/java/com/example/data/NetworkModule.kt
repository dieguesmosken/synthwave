package com.example.data

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object NetworkModule {

    val moshi: Moshi by lazy {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.spotify.com/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(OkHttpClient.Builder().build())
            .build()
    }

    val spotifyApi: SpotifyApi by lazy {
        retrofit.create(SpotifyApi::class.java)
    }
}
