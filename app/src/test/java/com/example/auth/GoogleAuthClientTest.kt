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
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class GoogleAuthClientTest {

    private lateinit var context: Context
    private lateinit var credentialManager: CredentialManager
    private lateinit var authClient: GoogleAuthClient

    @Before
    fun setup() {
        context = RuntimeEnvironment.getApplication()
        credentialManager = mockk(relaxed = true)

        authClient = GoogleAuthClient(context).apply {
            this.credentialManager = this@GoogleAuthClientTest.credentialManager
        }
    }

    @Test
    fun `signIn returns null when GetCredentialException is thrown`() = runTest {
        coEvery { credentialManager.getCredential(any<Context>(), any<GetCredentialRequest>()) } throws GetCredentialCustomException("CustomType", "Test Exception")

        val result = authClient.signIn()

        assertNull(result)
    }

    @Test
    fun `signIn returns null when generic Exception is thrown`() = runTest {
        coEvery { credentialManager.getCredential(any<Context>(), any<GetCredentialRequest>()) } throws Exception("Generic Exception")

        val result = authClient.signIn()

        assertNull(result)
    }
}
