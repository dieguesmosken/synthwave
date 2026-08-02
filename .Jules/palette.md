## 2024-05-24 - Make SettingsSwitchItem entire Row toggleable
**Learning:** Using `Modifier.toggleable` on a parent `Row` with `Role.Switch` while passing `onCheckedChange = null` to the inner `Switch` component significantly improves the touch target size and semantic accessibility for screen readers in Jetpack Compose.
**Action:** Always prefer making the entire list item row interactive for switches/checkboxes rather than relying only on the small touch target of the component itself.
