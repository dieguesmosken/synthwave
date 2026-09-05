1. Modify `app/src/main/java/com/example/ui/screens/SearchScreen.kt` to include `LocalFocusManager` and `KeyboardActions`.
2. Retrieve `LocalFocusManager.current` and apply `KeyboardActions(onSearch = { focusManager.clearFocus() })` to the search input.
3. Add an entry to `.Jules/palette.md` about ensuring keyboard dismissal in Jetpack Compose search inputs.
4. Run pre commit steps (formatting, linting, tests).
5. Submit the changes with a PR description matching Palette's format.
