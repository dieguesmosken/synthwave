package com.example.auth

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.GetCredentialUnknownException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class GoogleAuthClientTest {

    @Test
    fun signIn_throwsGetCredentialException_returnsNull() = runTest {
        val mockContext = mockk<Context>(relaxed = true)
        val mockCredentialManager = mockk<CredentialManager>()
        val googleAuthClient = GoogleAuthClient(mockContext).apply {
            credentialManager = mockCredentialManager
        }

        // We mock the credentialManager to throw a GetCredentialUnknownException which is a subclass of GetCredentialException
        coEvery { mockCredentialManager.getCredential(any(), any<GetCredentialRequest>()) } throws GetCredentialUnknownException("Mock Exception")

        val result = googleAuthClient.signIn()

        assertNull(result)
    }

    @Test
    fun signIn_throwsException_returnsNull() = runTest {
        val mockContext = mockk<Context>(relaxed = true)
        val mockCredentialManager = mockk<CredentialManager>()
        val googleAuthClient = GoogleAuthClient(mockContext).apply {
            credentialManager = mockCredentialManager
        }

        // We mock the credentialManager to throw a general Exception
        coEvery { mockCredentialManager.getCredential(any(), any<GetCredentialRequest>()) } throws RuntimeException("Mock Runtime Exception")

        val result = googleAuthClient.signIn()

        assertNull(result)
    }
}
