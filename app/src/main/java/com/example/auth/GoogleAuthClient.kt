package com.example.auth

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GoogleAuthClient(private val context: Context) {

    companion object {
        private const val TAG = "GoogleAuthClient"
    }

    private val credentialManager = CredentialManager.create(context)

    // In a real app, this should be the Web Client ID from Google Cloud Console.
    // Ensure you create an OAuth client ID of type "Web application" to use here.
    private val webClientId = "YOUR_WEB_CLIENT_ID_HERE"

    suspend fun signIn(): GoogleIdTokenCredential? {
        return withContext(Dispatchers.IO) {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(webClientId)
                .setAutoSelectEnabled(true)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            try {
                val result = credentialManager.getCredential(context, request)
                handleSignIn(result)
            } catch (e: GetCredentialException) {
                Log.e(TAG, "Sign-in failed", e)
                null
            } catch (e: Exception) {
                Log.e(TAG, "Unexpected sign-in error", e)
                null
            }
        }
    }

    private fun handleSignIn(result: GetCredentialResponse): GoogleIdTokenCredential? {
        val credential = result.credential
        if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            try {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                Log.d(TAG, "Signed in successfully")
                return googleIdTokenCredential
            } catch (e: Exception) {
                Log.e(TAG, "Received an invalid google id token response", e)
            }
        } else {
            Log.e(TAG, "Unexpected type of credential")
        }
        return null
    }
}
