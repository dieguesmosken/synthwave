package com.example.auth

import android.content.Context
import android.os.Bundle
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import android.util.Log

class GoogleAuthClientTest {

    private lateinit var googleAuthClient: GoogleAuthClient
    private lateinit var mockContext: Context
    private lateinit var mockCredentialManager: CredentialManager

    @Before
    fun setup() {
        mockContext = mockk(relaxed = true)
        mockCredentialManager = mockk(relaxed = true)
        googleAuthClient = GoogleAuthClient(mockContext, mockCredentialManager)
        mockkStatic(Log::class)
        every { Log.e(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
        every { Log.d(any(), any()) } returns 0
    }

    @After
    fun teardown() {
        unmockkStatic(Log::class)
        unmockkObject(GoogleIdTokenCredential.Companion)
    }

    @Test
    fun signIn_success_returnsGoogleIdTokenCredential() = runTest {
        val mockResponse = mockk<GetCredentialResponse>()
        val mockCredential = mockk<Credential>()
        val mockGoogleIdTokenCredential = mockk<GoogleIdTokenCredential>()
        val mockBundle = mockk<Bundle>(relaxed = true)

        every { mockCredential.type } returns GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        every { mockCredential.data } returns mockBundle
        every { mockResponse.credential } returns mockCredential

        every { mockBundle.getString(any()) } returns "mock_string"
        every { mockBundle.getString(any(), any()) } returns "mock_string"

        mockkObject(GoogleIdTokenCredential.Companion)
        every { GoogleIdTokenCredential.Companion.createFrom(any()) } returns mockGoogleIdTokenCredential
        every { mockGoogleIdTokenCredential.id } returns "test_id"

        coEvery { mockCredentialManager.getCredential(any(), any<GetCredentialRequest>()) } returns mockResponse

        val result = googleAuthClient.signIn()

        assertEquals(mockGoogleIdTokenCredential, result)
    }

    @Test
    fun signIn_getCredentialException_returnsNull() = runTest {
        coEvery { mockCredentialManager.getCredential(any(), any<GetCredentialRequest>()) } throws mockk<GetCredentialException>(relaxed = true)

        val result = googleAuthClient.signIn()

        assertNull(result)
    }

    @Test
    fun signIn_genericException_returnsNull() = runTest {
        coEvery { mockCredentialManager.getCredential(any(), any<GetCredentialRequest>()) } throws Exception("Test Exception")

        val result = googleAuthClient.signIn()

        assertNull(result)
    }

    @Test
    fun signIn_unexpectedCredentialType_returnsNull() = runTest {
        val mockResponse = mockk<GetCredentialResponse>()
        val mockCredential = mockk<Credential>()

        every { mockCredential.type } returns "UNEXPECTED_TYPE"
        every { mockResponse.credential } returns mockCredential

        coEvery { mockCredentialManager.getCredential(any(), any<GetCredentialRequest>()) } returns mockResponse

        val result = googleAuthClient.signIn()

        assertNull(result)
    }

    @Test
    fun signIn_invalidGoogleIdTokenResponse_returnsNull() = runTest {
        val mockResponse = mockk<GetCredentialResponse>()
        val mockCredential = mockk<Credential>()
        val mockBundle = mockk<Bundle>(relaxed = true)

        every { mockCredential.type } returns GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        every { mockCredential.data } returns mockBundle
        every { mockResponse.credential } returns mockCredential

        every { mockBundle.getString(any()) } returns "mock_string"
        every { mockBundle.getString(any(), any()) } returns "mock_string"

        mockkObject(GoogleIdTokenCredential.Companion)
        every { GoogleIdTokenCredential.Companion.createFrom(any()) } throws Exception("Invalid token data")

        coEvery { mockCredentialManager.getCredential(any(), any<GetCredentialRequest>()) } returns mockResponse

        val result = googleAuthClient.signIn()

        assertNull(result)
    }
}
