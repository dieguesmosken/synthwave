package com.example.data

import com.mongodb.client.model.InsertOneOptions
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class UserRepositoryTest {

    private lateinit var mockClient: MongoClient
    private lateinit var mockDatabase: MongoDatabase
    private lateinit var mockCollection: MongoCollection<UserModel>
    private lateinit var userRepository: UserRepository

    @Before
    fun setup() {
        mockClient = mockk()
        mockDatabase = mockk()
        mockCollection = mockk()

        // Setup the mock chain: client -> database -> collection
        every { mockClient.getDatabase("soundwave") } returns mockDatabase
        every { mockDatabase.getCollection<UserModel>("users") } returns mockCollection

        userRepository = UserRepository(client = mockClient)
    }

    @Test
    fun saveUser_successfulInsertion() = runTest {
        // Arrange
        val testUser = UserModel(_id = "1", name = "Test User", email = "test@example.com")
        coEvery { mockCollection.insertOne(testUser, any()) } returns mockk()

        // Act
        userRepository.saveUser(testUser)

        // Assert
        coVerify(exactly = 1) { mockCollection.insertOne(testUser, any()) }
    }

    @Test
    fun saveUser_handlesException() = runTest {
        // Arrange
        val testUser = UserModel(_id = "2", name = "Error User", email = "error@example.com")
        coEvery { mockCollection.insertOne(testUser, any()) } throws Exception("MongoDB Error")

        // Act
        userRepository.saveUser(testUser)

        // Assert
        coVerify(exactly = 1) { mockCollection.insertOne(testUser, any()) }
        // The test simply passes if no exception propagates and crashes the test,
        // which matches the expected behavior of the catch block in the repository.
    }

    @Test
    fun getUser_returnsNull() = runTest {
        // Act
        val result = userRepository.getUser("any_id")

        // Assert
        assertNull(result)
    }
}
