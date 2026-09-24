# Launcher3 Android 16 User-App Port
- このアプリは、Google様およびAOSPプロジェクトによりつくられたAndroidの標準ランチャーであり、私の自作ではありません。ご注意ください。
指定された AOSP Launcher3 `android16-release` を、通常のユーザー APK として直接コンパイルする移植プロジェクトです。

## 最短ビルド

1. JDK 17 と Android SDK Platform 36 / Build Tools 36.0.0 を準備します。
2. プロジェクト直下に SDK パスを含む `local.properties` を作ります。

   ```properties
   sdk.dir=/absolute/path/to/Android/Sdk
   ```

3. デバッグ APK をビルドします。

   ```sh
   ./gradlew --no-daemon --max-workers=1 :app:assembleDebug
   ```

出力は `app/build/outputs/apk/debug/app-debug.apk` です。

詳細な移植方針、SDK 準備、リリース署名、検証、実機ログ取得は [docs/LAUNCHER3_USER_APP_PORTING_GUIDE_JA.md](docs/LAUNCHER3_USER_APP_PORTING_GUIDE_JA.md) を参照してください。

> この配布物には SDK、Gradle キャッシュ、ビルド生成物、APK、ローカル署名鍵を含めません。初回ビルド時には Gradle Wrapper と Maven 依存関係をネットワーク経由で取得します。
