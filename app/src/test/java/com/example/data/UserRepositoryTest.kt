package com.example.data

import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.After
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull

class UserRepositoryTest {

    private lateinit var userRepository: UserRepository
    private lateinit var mockClient: MongoClient
    private lateinit var mockDatabase: MongoDatabase
    private lateinit var mockCollection: MongoCollection<UserModel>
    @Before
    fun setup() {
        mockkStatic(android.util.Log::class)
        every { android.util.Log.d(any(), any()) } returns 0
        every { android.util.Log.e(any(), any(), any()) } returns 0

        mockClient = mockk()
        mockDatabase = mockk()
        mockCollection = mockk()

        every { mockClient.getDatabase("soundwave") } returns mockDatabase
        every { mockDatabase.getCollection<UserModel>("users", UserModel::class.java) } returns mockCollection

        userRepository = UserRepository(mockClient)
    }


    @After
    fun teardown() {
        unmockkStatic(android.util.Log::class)
    }

    @Test
    fun saveUser_success_insertsIntoCollection() = runTest {
        // Arrange
        val testUser = UserModel(_id = "1", name = "Test User", email = "test@example.com")
        coEvery { mockCollection.insertOne(testUser, any()) } returns mockk()

        // Act
        userRepository.saveUser(testUser)

        // Assert
        coVerify(exactly = 1) { mockCollection.insertOne(testUser, any()) }
    }

    @Test
    fun saveUser_error_handlesException() = runTest {
        // Arrange
        val testUser = UserModel(_id = "1", name = "Test User", email = "test@example.com")
        coEvery { mockCollection.insertOne(testUser, any()) } throws Exception("Database error")

        // Act
        userRepository.saveUser(testUser)

        // Assert
        coVerify(exactly = 1) { mockCollection.insertOne(testUser, any()) }
    }

    @Test
    fun getUser_returnsNull() = runTest {
        // Act
        val result = userRepository.getUser("1")

        // Assert
        assertNull(result)
    }
}
