## 2024-05-24 - Make SettingsSwitchItem entire Row toggleable
**Learning:** Using `Modifier.toggleable` on a parent `Row` with `Role.Switch` while passing `onCheckedChange = null` to the inner `Switch` component significantly improves the touch target size and semantic accessibility for screen readers in Jetpack Compose.
**Action:** Always prefer making the entire list item row interactive for switches/checkboxes rather than relying only on the small touch target of the component itself.

## 2024-05-24 - Improve Slider Accessibility with Bounding Icons
**Learning:** When placing bounding icons next to a Slider (like Volume Down/Up), screen readers will redundantly announce them as independent elements if they have `contentDescription` set.
**Action:** Set bounding icons' `contentDescription` to `null` and instead add a descriptive `contentDescription` directly to the `Slider` using `modifier.semantics { contentDescription = "..." }`. This ensures context is correctly associated with the interactive control.

## 2024-05-24 - Improve Standalone Slider Accessibility
**Learning:** Standalone sliders (like progress bars) without visible text labels or bounding icons are read only as percentages by screen readers, leaving users without context on what the slider controls.
**Action:** Always add an explicit `modifier.semantics { contentDescription = "..." }` to standalone sliders to ensure screen reader users have appropriate context.

## 2024-05-24 - Grouping Elements for Screen Readers in Compose
**Learning:** In Jetpack Compose, complex list items containing images and texts are often read as separate elements by screen readers, making navigation tedious.
**Action:** When building interactive list items or cards with multiple elements, apply `.semantics(mergeDescendants = true) { }` to the parent clickable container. Additionally, set the `contentDescription` of internal icons or images to `null` if their context is already conveyed by sibling text elements to prevent redundant announcements.
