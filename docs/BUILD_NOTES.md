# Launcher3 Android 16 User-App Port

## Source basis

This APK is built from the requested AOSP Launcher3 source snapshot.

| Item | Value |
|---|---|
| Upstream repository | https://android.googlesource.com/platform/packages/apps/Launcher3/ |
| Requested branch | `android16-release` |
| Branch head inspected | `2f5b9b869d86620fdb899bc2df53d523dc9dc6ea` |
| Directly compiled Launcher3 roots | `source/src`, `source/src_no_quickstep`, `source/src_build_config`, `source/src_plugins`, `source/shared/src` |
| Supporting AOSP source | Android 16 `frameworks/libs/systemui/iconloaderlib` |
| Original license | Apache License 2.0; original notices are retained under `source/` and `dependencies/` |

The release DEX contains the original Launcher3 application classes `com.android.launcher3.LauncherApplication`, `com.android.launcher3.Launcher`, and `com.android.launcher3.Workspace`. The manifest launches `com.android.launcher3.Launcher` and declares the standard `HOME` intent filter.

## Public user-app port scope

The upstream Android 16 source is designed for Soong/AOSP and assumes platform signing, SystemUI shared libraries, Aconfig-generated flags, privileged permissions, and internal framework APIs. This port compiles the Launcher3 source itself through Gradle and replaces only those unavailable dependencies that would prevent a normally installed APK from loading.

| Area | User-app port behavior |
|---|---|
| Launcher workspace, all-apps, folders, icons, widgets, settings, provider | Original Launcher3 source is compiled directly. |
| Quickstep, recents animation, system gesture / taskbar integration | Not included; the port builds the upstream `src_no_quickstep` variant. |
| SystemUI plugin discovery | Source-compatible no-op interface; no privileged SystemUI plugin loading. |
| Aconfig and Shell feature gates | User-app compatibility values. Platform-only, private-space, taskbar, bubbles, and experimental paths are disabled. |
| MSDL haptic runtime | Source-compatible no-op implementation; proprietary MSDL runtime is not bundled. |
| App identity | `org.launcher3.universal`, so it does not collide with a system `com.android.launcher3` package. |
| SDK range | `minSdk 31` (Android 12), `targetSdk 35`. |

## Reproducible build

A full `assembleRelease` invokes release lint and can exceed the sandbox memory budget. The signed package task is the reproducible output step after compilation:

```sh
./toolchain/gradle-8.11.1/bin/gradle --no-daemon --max-workers=1 :app:packageRelease
```

The generated release APK is signed by the local development key at `release/launcher3-universal.keystore`. It is intended solely for direct installation and diagnostic testing. Do not publish this development-signed artifact to an app store.
