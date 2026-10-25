package com.example.data

import com.mongodb.client.model.InsertOneOptions
import com.mongodb.client.result.InsertOneResult
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.bson.BsonString
import org.junit.Test

class UserRepositoryTest {

    @Test
    fun `saveUser happy path - inserts user into database`() = runTest {
        // Arrange
        val client = mockk<MongoClient>()
        val database = mockk<MongoDatabase>()
        val collection = mockk<MongoCollection<UserModel>>()

        val user = UserModel(_id = "1", name = "Test User", email = "test@example.com")
        val insertResult = InsertOneResult.acknowledged(BsonString("1"))

        coEvery { client.getDatabase("soundwave") } returns database
        coEvery { database.getCollection<UserModel>("users") } returns collection
        coEvery { collection.insertOne(user, any()) } returns insertResult

        val repository = UserRepository(client)

        // Act
        repository.saveUser(user)

        // Assert
        coVerify(exactly = 1) { collection.insertOne(user, any()) }
    }

    @Test
    fun `saveUser error path - handles exception when inserting user`() = runTest {
        // Arrange
        val client = mockk<MongoClient>()
        val database = mockk<MongoDatabase>()
        val collection = mockk<MongoCollection<UserModel>>()

        val user = UserModel(_id = "1", name = "Test User", email = "test@example.com")

        coEvery { client.getDatabase("soundwave") } returns database
        coEvery { database.getCollection<UserModel>("users") } returns collection
        coEvery { collection.insertOne(user, any()) } throws Exception("Mocked exception")

        val repository = UserRepository(client)

        // Act
        // We verify that calling saveUser does not crash by letting it run without throwing
        repository.saveUser(user)

        // Assert
        coVerify(exactly = 1) { collection.insertOne(user, any()) }
    }
}
