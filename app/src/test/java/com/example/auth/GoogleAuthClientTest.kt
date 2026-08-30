package com.example.auth

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import androidx.credentials.Credential
import android.os.Bundle

@RunWith(RobolectricTestRunner::class)
@Config(manifest=Config.NONE)
@OptIn(ExperimentalCoroutinesApi::class)
class GoogleAuthClientTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var context: Context
    private lateinit var credentialManager: CredentialManager
    private lateinit var googleAuthClient: GoogleAuthClient

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        context = mockk(relaxed = true)

        credentialManager = mockk(relaxed = true)

        mockkObject(CredentialManager.Companion)
        every { CredentialManager.create(any()) } returns credentialManager

        googleAuthClient = GoogleAuthClient(context)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun signIn_withInvalidCredentialType_returnsNull() = runTest {
        val mockCredential = mockk<Credential>(relaxed = true) {
            every { type } returns "some_other_type"
        }
        val mockResponse = mockk<GetCredentialResponse>(relaxed = true) {
            every { credential } returns mockCredential
        }

        coEvery { credentialManager.getCredential(any(), any<GetCredentialRequest>()) } returns mockResponse

        val result = googleAuthClient.signIn()

        assertNull(result)
    }

    @Test
    fun signIn_withGetCredentialException_returnsNull() = runTest {
        val exception = mockk<GetCredentialException>(relaxed = true)

        coEvery {
            credentialManager.getCredential(any(), any<GetCredentialRequest>())
        } throws exception

        val result = googleAuthClient.signIn()

        assertNull(result)
    }

    @Test
    fun signIn_withUnexpectedException_returnsNull() = runTest {
        coEvery {
            credentialManager.getCredential(any(), any<GetCredentialRequest>())
        } throws RuntimeException("Unexpected error")

        val result = googleAuthClient.signIn()

        assertNull(result)
    }

    @Test
    fun signIn_withValidGoogleIdToken_returnsGoogleIdTokenCredential() = runTest {
        val bundle = Bundle().apply {
            putString("com.google.android.libraries.identity.googleid.BUNDLE_KEY_ID", "test_id")
            putString("com.google.android.libraries.identity.googleid.BUNDLE_KEY_ID_TOKEN", "test_token")
        }

        val mockCredential = mockk<Credential>(relaxed = true) {
            every { type } returns GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
            every { data } returns bundle
        }
        val mockResponse = mockk<GetCredentialResponse>(relaxed = true) {
            every { credential } returns mockCredential
        }

        coEvery { credentialManager.getCredential(any(), any<GetCredentialRequest>()) } returns mockResponse

        val result = googleAuthClient.signIn()

        assertNotNull(result)
        assertEquals("test_id", result?.id)
        assertEquals("test_token", result?.idToken)
    }

    @Test
    fun signIn_withInvalidBundleData_returnsNull() = runTest {
        val bundle = Bundle().apply {
            putString("com.google.android.libraries.identity.googleid.BUNDLE_KEY_ID", "test_id")
            // missing token -> invalid bundle
        }

        val mockCredential = mockk<Credential>(relaxed = true) {
            every { type } returns GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
            every { data } returns bundle
        }
        val mockResponse = mockk<GetCredentialResponse>(relaxed = true) {
            every { credential } returns mockCredential
        }

        coEvery { credentialManager.getCredential(any(), any<GetCredentialRequest>()) } returns mockResponse

        val result = googleAuthClient.signIn()

        assertNull(result)
    }
}
