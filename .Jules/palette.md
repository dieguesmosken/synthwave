## 2024-05-24 - Make SettingsSwitchItem entire Row toggleable
**Learning:** Using `Modifier.toggleable` on a parent `Row` with `Role.Switch` while passing `onCheckedChange = null` to the inner `Switch` component significantly improves the touch target size and semantic accessibility for screen readers in Jetpack Compose.
**Action:** Always prefer making the entire list item row interactive for switches/checkboxes rather than relying only on the small touch target of the component itself.

## 2024-05-24 - Improve Slider Accessibility with Bounding Icons
**Learning:** When placing bounding icons next to a Slider (like Volume Down/Up), screen readers will redundantly announce them as independent elements if they have `contentDescription` set.
**Action:** Set bounding icons' `contentDescription` to `null` and instead add a descriptive `contentDescription` directly to the `Slider` using `modifier.semantics { contentDescription = "..." }`. This ensures context is correctly associated with the interactive control.
