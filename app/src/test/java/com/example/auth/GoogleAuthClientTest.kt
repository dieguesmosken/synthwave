package com.example.auth

import android.content.Context
import android.os.Bundle
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialUnknownException
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import io.mockk.spyk
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class GoogleAuthClientTest {

    private lateinit var context: Context
    private lateinit var credentialManager: CredentialManager
    private lateinit var googleAuthClient: GoogleAuthClient

    @Before
    fun setUp() {
        context = mockk()
        credentialManager = mockk()

        mockkObject(CredentialManager.Companion)
        every { CredentialManager.create(context) } returns credentialManager

        googleAuthClient = GoogleAuthClient(context)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `signIn returns null when GetCredentialException is thrown`() = runTest {
        coEvery {
            credentialManager.getCredential(any(), any<GetCredentialRequest>())
        } throws GetCredentialUnknownException("mock exception")

        val result = googleAuthClient.signIn()
        assertNull(result)
    }

    @Test
    fun `signIn returns null when unexpected Exception is thrown`() = runTest {
        coEvery {
            credentialManager.getCredential(any(), any<GetCredentialRequest>())
        } throws RuntimeException("mock unexpected exception")

        val result = googleAuthClient.signIn()
        assertNull(result)
    }

    @Test
    fun `signIn returns null when credential type is unexpected`() = runTest {
        val mockCredential = mockk<Credential>()
        every { mockCredential.type } returns "UNEXPECTED_TYPE"
        val mockResponse = mockk<GetCredentialResponse>()
        every { mockResponse.credential } returns mockCredential

        coEvery {
            credentialManager.getCredential(any(), any<GetCredentialRequest>())
        } returns mockResponse

        val result = googleAuthClient.signIn()
        assertNull(result)
    }

    @Test
    fun `signIn returns null when createFrom throws exception`() = runTest {
        val mockCredential = mockk<Credential>()
        every { mockCredential.type } returns GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL

        val mockBundle = Bundle()
        every { mockCredential.data } returns mockBundle

        val mockResponse = mockk<GetCredentialResponse>()
        every { mockResponse.credential } returns mockCredential

        coEvery {
            credentialManager.getCredential(any(), any<GetCredentialRequest>())
        } returns mockResponse

        val googleAuthClientSpy = spyk(googleAuthClient, recordPrivateCalls = true)
        every {
            googleAuthClientSpy["handleSignIn"](any<GetCredentialResponse>())
        } returns null

        val result = googleAuthClientSpy.signIn()
        assertNull(result)
    }

    @Test
    fun `signIn returns GoogleIdTokenCredential on success`() = runTest {
        val mockCredential = mockk<Credential>()
        every { mockCredential.type } returns GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL

        val mockBundle = Bundle()
        every { mockCredential.data } returns mockBundle

        val mockResponse = mockk<GetCredentialResponse>()
        every { mockResponse.credential } returns mockCredential

        coEvery {
            credentialManager.getCredential(any(), any<GetCredentialRequest>())
        } returns mockResponse

        val expectedGoogleCredential = mockk<GoogleIdTokenCredential>()
        every { expectedGoogleCredential.id } returns "test_user_id"

        val googleAuthClientSpy = spyk(googleAuthClient, recordPrivateCalls = true)
        every {
            googleAuthClientSpy["handleSignIn"](any<GetCredentialResponse>())
        } returns expectedGoogleCredential

        val result = googleAuthClientSpy.signIn()
        assertNotNull(result)
        assertEquals(expectedGoogleCredential, result)
    }
}
