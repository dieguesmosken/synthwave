package com.example.data

import com.mongodb.client.model.InsertOneOptions
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.assertTrue
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import org.junit.After
import org.junit.Before

class UserRepositoryTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun saveUser_handlesException() = runTest(testDispatcher) {
        val mockClient = mockk<MongoClient>()
        val mockDatabase = mockk<MongoDatabase>()
        val mockCollection = mockk<MongoCollection<UserModel>>()

        every { mockClient.getDatabase("soundwave") } returns mockDatabase
        every { mockDatabase.getCollection<UserModel>("users") } returns mockCollection
        coEvery { mockCollection.insertOne(any<UserModel>(), any<InsertOneOptions>()) } throws RuntimeException("MongoDB connection failed")
        coEvery { mockCollection.insertOne(any<UserModel>()) } throws RuntimeException("MongoDB connection failed")

        val userRepository = UserRepository(mockClient)
        val user = UserModel(_id = "1", name = "Test User")

        val originalOut = System.out
        val originalErr = System.err
        val outContent = ByteArrayOutputStream()
        val errContent = ByteArrayOutputStream()

        try {
            System.setOut(PrintStream(outContent))
            System.setErr(PrintStream(errContent))

            userRepository.saveUser(user)
        } finally {
            System.setOut(originalOut)
            System.setErr(originalErr)
        }

        val outString = outContent.toString()
        val errString = errContent.toString()

        assertTrue("Output did not contain expected error message. Output was: $outString", outString.contains("Error saving to MongoDB: MongoDB connection failed"))
        assertTrue("Stacktrace was not printed. Output was: $errString", errString.contains("java.lang.RuntimeException: MongoDB connection failed"))
    }
}
