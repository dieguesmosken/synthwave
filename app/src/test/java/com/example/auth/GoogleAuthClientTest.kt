package com.example.auth

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCustomException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.lang.reflect.Field

@RunWith(RobolectricTestRunner::class)
@Config(manifest=Config.NONE)
class GoogleAuthClientTest {

    private lateinit var context: Context
    private lateinit var credentialManager: CredentialManager
    private lateinit var googleAuthClient: GoogleAuthClient

    @Before
    fun setup() {
        context = mockk(relaxed = true)
        credentialManager = mockk(relaxed = true)
        googleAuthClient = GoogleAuthClient(context)

        // Use reflection to inject the mocked credentialManager
        val credentialManagerField: Field = GoogleAuthClient::class.java.getDeclaredField("credentialManager")
        credentialManagerField.isAccessible = true
        credentialManagerField.set(googleAuthClient, credentialManager)
    }

    @Test
    fun signIn_throwsGetCredentialException_returnsNull() = runTest {
        coEvery {
            credentialManager.getCredential(any(), any<GetCredentialRequest>())
        } throws GetCredentialCustomException("Test Error")

        val result = googleAuthClient.signIn()

        assertNull(result)
    }

    @Test
    fun signIn_throwsGenericException_returnsNull() = runTest {
        coEvery {
            credentialManager.getCredential(any(), any<GetCredentialRequest>())
        } throws Exception("Test Error")

        val result = googleAuthClient.signIn()

        assertNull(result)
    }
}
