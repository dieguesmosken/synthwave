package com.example.data

import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.client.result.InsertOneResult
import com.mongodb.client.model.InsertOneOptions
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.bson.BsonString
import org.bson.BsonValue

class UserRepositoryTest {

    private lateinit var userRepository: UserRepository
    private lateinit var mockMongoClient: MongoClient
    private lateinit var mockDatabase: MongoDatabase
    private lateinit var mockCollection: MongoCollection<UserModel>

    @Before
    fun setup() {
        mockMongoClient = mockk()
        mockDatabase = mockk()
        mockCollection = mockk()

        every { mockMongoClient.getDatabase(any()) } returns mockDatabase
        every { mockDatabase.getCollection<UserModel>(any()) } returns mockCollection

        userRepository = UserRepository(mockMongoClient)
    }

    @Test
    fun testSaveUser() = runTest {
        val testUser = UserModel(_id = "1", name = "Test User", email = "test@test.com")
        val insertResult = InsertOneResult.acknowledged(BsonString("1") as BsonValue)

        coEvery { mockCollection.insertOne(any(), any<InsertOneOptions>()) } returns insertResult

        userRepository.saveUser(testUser)

        coVerify { mockCollection.insertOne(testUser, any<InsertOneOptions>()) }
    }

    @Test
    fun testSaveUserException() = runTest {
        val testUser = UserModel(_id = "1", name = "Test User", email = "test@test.com")

        coEvery { mockCollection.insertOne(any(), any<InsertOneOptions>()) } throws RuntimeException("MongoDB connection error")

        userRepository.saveUser(testUser)

        coVerify { mockCollection.insertOne(testUser, any<InsertOneOptions>()) }
    }
}
