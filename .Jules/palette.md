## 2024-05-24 - Make SettingsSwitchItem entire Row toggleable
**Learning:** Using `Modifier.toggleable` on a parent `Row` with `Role.Switch` while passing `onCheckedChange = null` to the inner `Switch` component significantly improves the touch target size and semantic accessibility for screen readers in Jetpack Compose.
**Action:** Always prefer making the entire list item row interactive for switches/checkboxes rather than relying only on the small touch target of the component itself.

## 2024-05-24 - Improve Slider Accessibility with Bounding Icons
**Learning:** When placing bounding icons next to a Slider (like Volume Down/Up), screen readers will redundantly announce them as independent elements if they have `contentDescription` set.
**Action:** Set bounding icons' `contentDescription` to `null` and instead add a descriptive `contentDescription` directly to the `Slider` using `modifier.semantics { contentDescription = "..." }`. This ensures context is correctly associated with the interactive control.

## 2024-05-24 - Improve Standalone Slider Accessibility
**Learning:** Standalone sliders (like progress bars) without visible text labels or bounding icons are read only as percentages by screen readers, leaving users without context on what the slider controls.
**Action:** Always add an explicit `modifier.semantics { contentDescription = "..." }` to standalone sliders to ensure screen reader users have appropriate context.

## 2025-02-12 - Merge Descendants for Clickable List Items
**Learning:** In Jetpack Compose, when list items or cards have multiple text or icon elements inside a clickable container, screen readers may announce each child sequentially or awkwardly. Using `Modifier.semantics(mergeDescendants = true)` groups them into a single focusable node, reducing redundant and tedious announcements.
**Action:** Always use `Modifier.semantics(mergeDescendants = true)` on parent clickable containers (like list items or cards) to group child elements for screen readers.

## 2025-02-13 - Improve Keyboard Usability with KeyboardOptions and singleLine
**Learning:** In Jetpack Compose, missing `singleLine = true` and `KeyboardOptions` (like `KeyboardType.Email` or `ImeAction.Next`) on text fields causes the enter key to insert new lines awkwardly instead of moving focus, and prevents the system from displaying optimized keyboards (like showing the `@` key for emails).
**Action:** Always provide explicit `KeyboardOptions` and `singleLine = true` (unless explicitly needing a multi-line input) on forms to significantly improve input speed and usability.

## 2025-02-13 - Enhance Search Field UX
**Learning:** Hardcoded, empty `onValueChange` callbacks in Jetpack Compose `OutlinedTextField` make the input uneditable and unresponsive. Furthermore, without `singleLine = true` and `KeyboardOptions(imeAction = ImeAction.Search)`, the enter key defaults to an unexpected newline, and the keyboard lacks the "Search" action button.
**Action:** Always bind text fields to a state holder (e.g., `mutableStateOf`). Add a conditional trailing 'clear' icon button for better usability, and explicitly configure `singleLine` and `KeyboardOptions` to optimize the keyboard layout for the context (e.g., Search).

## 2025-02-13 - Dismiss Keyboard on Search Action
**Learning:** In Jetpack Compose, even if an `ImeAction.Search` is specified in `KeyboardOptions`, the software keyboard does not automatically dismiss when the user triggers the search action. This can obstruct the UI and lead to a poor user experience.
**Action:** Always provide explicit `KeyboardActions(onSearch = { focusManager.clearFocus() })` using `LocalFocusManager.current` alongside `ImeAction.Search` to ensure the software keyboard dismisses correctly upon triggering a search.
