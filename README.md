------------------------------
## Launcher3 Android 16 User-App Port

* Please note: This app is the standard Android launcher created by Google and the AOSP project. It is not my own original work.

This porting project allows you to directly compile the specified AOSP Launcher3 android16-release branch as a standard, non-system user APK.
## Quick Start Build

   1. Set up JDK 17 and Android SDK Platform 36 / Build Tools 36.0.0.
   2. Create a local.properties file in the project root directory and include your SDK path:
   
   sdk.dir=/absolute/path/to/Android/Sdk
   
   3. Build the debug APK:
   
   ./gradlew --no-daemon --max-workers=1 :app:assembleDebug
   
   
The output will be generated at app/build/outputs/apk/debug/app-debug.apk.
For detailed porting strategies, SDK setup, release signing, verification, and on-device logging, please refer to docs/LAUNCHER3_USER_APP_PORTING_GUIDE_JA.md.

This distribution does not include the SDK, Gradle cache, build artifacts, APKs, or local signing keys. The Gradle Wrapper and Maven dependencies will be fetched over the network during the initial build.

------------------------------
