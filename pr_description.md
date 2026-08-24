🎯 **What:**
Implemented missing unit tests for `GoogleAuthClient`. The `GoogleAuthClient` heavily relied on the Android `CredentialManager` and `Context`, which were previously untested. By mocking these classes, we can thoroughly test the sign-in logic.

📊 **Coverage:**
The newly added tests comprehensively cover:
- The happy path: successfully generating and returning a `GoogleIdTokenCredential`.
- Handling of `GetCredentialException` scenarios where it safely returns `null`.
- Handling of unexpected RuntimeExceptions (returning `null`).
- Edge cases such as unexpected credential types.
- Scenarios where token creation parsing (`GoogleIdTokenCredential.createFrom`) fails.

✨ **Result:**
Test coverage for Google authentication logic is established. Future refactoring of the authentication process can be performed with higher confidence because the tests explicitly verify error scenarios, credential validation, and exceptions.
