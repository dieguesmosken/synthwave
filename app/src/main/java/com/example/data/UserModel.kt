package com.example.data

data class UserModel(
    val _id: String = "",
    val name: String = "",
    val email: String = "",
    val favoriteSongs: List<String> = emptyList()
)
