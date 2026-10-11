# Panda Macro — Android starter

Dark Panda Touch-inspired Android/Kotlin starter with profile persistence, an action editor (tap/wait/swipe), repeat timing controls, AccessibilityService gesture-dispatch scaffold, and an overlay-permission flow.

## Build
1. Open this folder in Android Studio.
2. Install Android SDK 35 if prompted; use JDK 17 for Gradle.
3. Sync Gradle, then Build → Build APK(s).

## Use
- Add/select a profile and add TAP, WAIT, or SWIPE actions.
- Save profiles; they are stored as JSON in app-private storage.
- Enable **Panda Macro Service** in Android Settings → Accessibility to authorize gesture dispatch.
- Grant overlay permission if you want to add the floating panel implementation next.

## Scope
This is a starter, not a copy of Panda Touch Pro. Cross-app automation requires explicit Android Accessibility consent. Some apps, secure screens, and games block synthetic gestures. The included service can dispatch configured gestures, but the UI does not yet contain a full recorder that captures arbitrary touches in other apps, nor a draggable persistent overlay; those are next implementation steps.
