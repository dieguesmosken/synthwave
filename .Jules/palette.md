## 2024-03-24 - Screen Reader Redundancy in Compose Custom Buttons
**Learning:** In Jetpack Compose, setting a `contentDescription` on an `Icon` inside a `clickable` `Column` that also contains a `Text` element causes the screen reader to read the information redundantly, and without a button role, the user might not know it's a clickable action.
**Action:** Always assign `role = Role.Button` to the clickable modifier of a custom action component. Additionally, set `contentDescription = null` on the inner `Icon` when a sibling `Text` element already provides the identical context.
