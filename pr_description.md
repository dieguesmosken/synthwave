## 🧪 Testing Improvement: UserRepository

### 🎯 What
The `UserRepository` logic was lacking unit tests, primarily because the instantiation of the `MongoClient` was hardcoded inside the class, making it difficult to inject mocks for testing purposes.

### 📊 Coverage
The following changes and scenarios have been tested:
- Extracted the `MongoClient` instance to be injected via the constructor with a default singleton instance utilizing `by lazy` initialized via a companion object.
- Replaced `println`/`e.printStackTrace` statements with standard `android.util.Log` calls.
- Added `io.mockk:mockk` to handle mocking.
- **Happy Path:** Added `saveUser_success_insertsIntoCollection` to verify `insertOne` runs correctly.
- **Error Condition:** Added `saveUser_error_handlesException` to verify that an exception occurring inside `insertOne` is properly logged without crashing the app.
- **getUser Test:** Added `getUser_returnsNull` (which currently returns null per the placeholder implementation).

### ✨ Result
The application now supports dependency injection for `UserRepository`, ensuring decoupled interactions with the MongoDB driver. The coverage is now robust against the primary database interaction behaviors, making `UserRepository` reliable and preventing regressions on the data layer functionality.
