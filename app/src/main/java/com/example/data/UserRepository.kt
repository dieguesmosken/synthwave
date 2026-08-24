package com.example.data

import android.util.Log
import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.ServerApi
import com.mongodb.ServerApiVersion
import com.mongodb.kotlin.client.coroutine.MongoClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bson.Document

class UserRepository {

    companion object {
        private const val TAG = "UserRepository"
    }

    private val connectionString = "mongodb+srv://synthwave_db_user:<db_password>@synthwave-cluster0-aws.qhia3jr.mongodb.net/?appName=synthwave-Cluster0-AWS"
    private val client: MongoClient

    init {
        val serverApi = ServerApi.builder()
            .version(ServerApiVersion.V1)
            .build()

        val settings = MongoClientSettings.builder()
            .applyConnectionString(ConnectionString(connectionString))
            .serverApi(serverApi)
            .build()

        client = MongoClient.create(settings)
    }

    suspend fun saveUser(user: UserModel) {
        withContext(Dispatchers.IO) {
            try {
                val database = client.getDatabase("soundwave")
                val collection = database.getCollection<UserModel>("users")

                collection.insertOne(user)
                Log.d(TAG, "User saved to MongoDB: ${user.name}")
            } catch (e: Exception) {
                Log.e(TAG, "Error saving to MongoDB: ${e.message}", e)
            }
        }
    }

    suspend fun getUser(id: String): UserModel? {
        // Implementation here if needed in future
        return null
    }
}
