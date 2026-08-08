## 2024-05-24 - Make SettingsSwitchItem entire Row toggleable
**Learning:** Using `Modifier.toggleable` on a parent `Row` with `Role.Switch` while passing `onCheckedChange = null` to the inner `Switch` component significantly improves the touch target size and semantic accessibility for screen readers in Jetpack Compose.
**Action:** Always prefer making the entire list item row interactive for switches/checkboxes rather than relying only on the small touch target of the component itself.

## 2024-06-25 - Improve Form Usability with KeyboardOptions
**Learning:** By default, Jetpack Compose `OutlinedTextField` and `TextField` components do not provide context-aware virtual keyboards. This forces the user to manually switch keyboard modes (e.g., to find the '@' symbol for an email) and makes form submission clunky (e.g., using an 'Enter' key instead of a 'Next' or 'Done' action).
**Action:** Always provide `KeyboardOptions` to text fields. Specifically, set `keyboardType = KeyboardType.Email` for email fields, `keyboardType = KeyboardType.Password` for passwords, and use `imeAction = ImeAction.Next` or `ImeAction.Done` to allow users to smoothly navigate through forms via the keyboard.
