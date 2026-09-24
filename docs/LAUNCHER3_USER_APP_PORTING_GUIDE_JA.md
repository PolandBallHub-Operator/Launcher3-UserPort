# Launcher3 Android 16 ユーザーアプリ移植ガイド

**対象:** AOSP `platform/packages/apps/Launcher3` の `android16-release` ブランチを、プラットフォーム署名や system_ext 配置を必要としない通常の APK として移植する手順です。本ガイドの構成は実機での起動確認済みです。

> このガイドは **Launcher3 本体を直接コンパイルする移植**を扱います。別実装のホームアプリを Launcher3 として配布する手順ではありません。

## 1. 目的と移植範囲

Android 16 の公式 Launcher3 は AOSP の Soong ビルドで定義され、SystemUI、WindowManager Shell、Aconfig 生成物、プラットフォーム専用 API に依存します。公式 `Android.bp` の通常 Launcher3 は `src`、`src_no_quickstep`、`src_build_config` を組み合わせる構成であり、`privileged` と `system_ext_specific` も指定されています。[1]

本移植では、**Launcher3 のワークスペース、アプリ一覧、フォルダー、アイコン、ウィジェット、設定、データプロバイダーを含む本体ソース**を Gradle の直接コンパイル対象とします。一方、Quickstep、recents animation、Shell 統合、タスクバー、プライベート空間、SystemUI プラグイン実行など、一般アプリの権限範囲を超える経路は除外または無効化します。

| 項目 | 本移植での扱い |
|---|---|
| Launcher3 本体 | `source/src` を直接コンパイル |
| Quickstep | 除外 |
| 非 Quickstep 派生実装 | `source/src_no_quickstep` を直接コンパイル |
| Launcher3 固有ビルド定数 | `source/src_build_config` を直接コンパイル |
| Launcher3 プラグイン API | `source/src_plugins` を直接コンパイルし、PluginCore 基底 API は no-op 互換化 |
| shared テスト由来ユーティリティ | `source/shared/src` を直接コンパイル |
| IconLoader | 同じ Android 16 ブランチの `frameworks/libs/systemui/iconloaderlib` を同梱 |
| SystemUI / Shell / MSDL | `port/src/main/java` の公開 SDK 互換クラスで置換 |
| パッケージ ID | `org.launcher3.universal` |
| Java/Kotlin パッケージ | 元の `com.android.launcher3` を保持 |
| 対象 Android | API 31（Android 12）以上 |

> Java/Kotlin 側のパッケージを保持する一方で、APK の `applicationId` を変えることにより、OEM がシステム領域へ搭載した `com.android.launcher3` と署名・パッケージ衝突しないようにします。

## 2. 配布ソースの構成

```text
launcher3-android16-user-port/
├── app/                         # Gradle application module
├── port/                        # ユーザーアプリ向け Manifest・互換 API・追加リソース
├── source/                      # 指定された AOSP Launcher3 android16-release 本体
├── dependencies/
│   └── iconloaderlib/           # 対応する AOSP IconLoader ソース
├── docs/                        # このガイドと設計・ビルド文書
├── gradle/wrapper/              # Gradle Wrapper
├── gradlew / gradlew.bat
├── build.gradle
├── settings.gradle
└── gradle.properties
```

アーカイブには Android SDK、Gradle のダウンロード済み本体、`build/`、`.gradle/`、APK、ローカル署名鍵を含めません。SDK と依存関係は各環境で取得し、署名鍵は各自で生成してください。

## 3. 前提条件

| 要件 | 推奨値 | 用途 |
|---|---:|---|
| JDK | 17 | Android Gradle Plugin と Kotlin のコンパイル |
| Android SDK Platform | API 36 | `compileSdk 36` |
| Android Build Tools | 36.0.0 | aapt2、D8、署名関連処理 |
| Gradle | 8.11.1 | 同梱 Wrapper が取得 |
| ネットワーク | 初回のみ必要 | Gradle、Maven、AndroidX、Dagger、Protobuf の取得 |

Android SDK のコマンドラインツールで必要パッケージを導入する例です。[2]

```sh
sdkmanager "platform-tools" "platforms;android-36" "build-tools;36.0.0"
```

プロジェクト直下に `local.properties` を作り、SDK の絶対パスを指定します。

```properties
sdk.dir=/absolute/path/to/Android/Sdk
```

Linux/macOS の例では、次のように環境を確認できます。

```sh
java -version
./gradlew --version
```

## 4. ソースの取得と固定

Launcher3 本体の上流は、指定されたブランチから取得します。

```sh
git clone --depth 1 --branch android16-release \
  https://android.googlesource.com/platform/packages/apps/Launcher3.git source
```

`IconLoader` は Launcher3 が AOSP 側で依存する共有ライブラリです。対応する Android 16 ブランチから `iconloaderlib` サブツリーを取得し、`dependencies/iconloaderlib` へ展開します。

```sh
mkdir -p dependencies/iconloaderlib
curl -L -o /tmp/iconloaderlib.tar.gz \
  'https://android.googlesource.com/platform/frameworks/libs/systemui/+archive/refs/heads/android16-release/iconloaderlib.tar.gz'
tar -xzf /tmp/iconloaderlib.tar.gz -C dependencies/iconloaderlib
```

上流の Gradle 移植例でも、Launcher3 のソースディレクトリを直接 `sourceSets` に接続し、IconLoader を別モジュールとして参照する構成が採られています。[3]

## 5. Gradle モジュールの要点

`app/build.gradle` の `sourceSets.main` で、Launcher3 本体を直接ソースルートとして設定します。

```groovy
sourceSets {
    main {
        manifest.srcFile '../port/AndroidManifest.xml'
        java.srcDirs = [
            '../source/src',
            '../source/src_no_quickstep',
            '../source/src_build_config',
            '../source/src_plugins',
            '../source/shared/src',
            '../dependencies/iconloaderlib/src',
            '../port/src/main/java'
        ]
        res.srcDirs = [
            '../source/res',
            '../dependencies/iconloaderlib/res',
            '../port/src/main/res'
        ]
        proto.srcDirs = ['../source/protos', '../source/protos_overrides']
    }
}
```

Android 16 Launcher3 には Java、Kotlin、Protobuf、Dagger が含まれます。そのため、アプリモジュールでは Android application、Kotlin Android、KSP、Protobuf を有効化します。Dagger は KSP **だけ**で生成してください。`annotationProcessor` と KSP を同時に使うと、同一の Dagger 生成クラスを二重生成して `FilerException` になることがあります。

AOSP の `Android.bp` と同じく、Kotlin の既定インターフェースメソッドを有効にする必要があります。これがないと `Workspace` や `Folder` が `LauncherBindableItemsContainer` の Kotlin 既定メソッドを Java 側から参照できません。

```groovy
kotlinOptions {
    jvmTarget = '17'
    freeCompilerArgs += ['-Xjvm-default=all']
}
```

## 6. Manifest とアプリ ID

`port/AndroidManifest.xml` では次を設定します。

| 設定 | 値 | 理由 |
|---|---|---|
| `applicationId` | `org.launcher3.universal` | システム Launcher3 と衝突させない |
| `namespace` | `com.android.launcher3` | 本体ソースと `R` 参照を保持 |
| Application | `com.android.launcher3.LauncherApplication` | 元の Launcher3 Application を利用 |
| Home Activity | `com.android.launcher3.Launcher` | 元の Launcher3 Activity を利用 |
| Intent filter | `MAIN` + `HOME` + `DEFAULT` + `LAUNCHER` | 一般アプリとしてホーム候補に登録 |
| `minSdk` | 31 | Android 16 Launcher3 の公開 API 依存に合わせる |

初回は通常のアプリアイコンから起動して、クラッシュしないことを確認してください。その後に端末の「既定のホームアプリ」で Launcher3 を選択します。ホームアプリ選択は端末の OEM 実装に影響されるため、初回の再現試験と既定化を分けることが安全です。

## 7. 互換レイヤーの設計

互換コードはすべて `port/` に置き、上流の大半を変更しない方針とします。上流側で必要だった変更は、`android.multiuser.Flags` と `android.appwidget.flags.Flags` のような非公開フレームワーク API を、`com.android.launcher3.Flags` の安全な no-op フラグへ差し替える最小限の import／呼出し変更に限定します。

| 上流依存 | 置換先 | 既定値・方針 |
|---|---|---|
| Aconfig 生成 `com.android.launcher3.Flags` | `port/.../com/android/launcher3/Flags.java` | 安定機能のみ有効、Shell・プライベート空間・実験機能は無効 |
| SystemUI shared flags / stats | `port/.../com/android/systemui/shared/**` | ログ enum は定数、特権連携は無効 |
| WindowManager Shell flags / bubbles | `port/.../com/android/wm/**` | 全て安全側の `false` |
| SystemUI PluginCore | `port/.../com/android/systemui/plugins/**` | API 互換の no-op。外部プラグインはロードしない |
| MSDL haptic runtime | `port/.../com/google/android/msdl/**` | no-op。プロプライエタリ runtime は同梱しない |
| AOSP animation interpolators | `port/.../com/android/app/animation/Interpolators.java` | 公開 `Interpolator` の合成で代替 |
| IconLoader 独立モジュール `R` | `port/.../com/android/launcher3/icons/R.java` | 統合 APK の `R` と framework `R` へのブリッジ |

この方針では、**一般アプリに許可されない機能を偽装して有効化しません**。実装が必要な将来機能については、公開 SDK で代替できるか、端末メーカー／AOSP のシステム統合が必要かを分けて判断してください。

## 8. ビルド手順

### 8.1 デバッグ APK

最初は debug APK を作成します。これは Android Studio を使わずに実行できます。

```sh
./gradlew --no-daemon --max-workers=1 :app:assembleDebug
```

出力先は次です。

```text
app/build/outputs/apk/debug/app-debug.apk
```

インストール例です。

```sh
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

### 8.2 リリース APK

この構成の `release` はローカル署名鍵を期待します。まず開発用の鍵を生成します。

```sh
mkdir -p release
keytool -genkeypair -v \
  -keystore release/launcher3-universal.keystore \
  -storepass changeit \
  -alias launcher3-universal \
  -keypass changeit \
  -keyalg RSA -keysize 2048 -validity 3650 \
  -dname 'CN=Launcher3 User Port, OU=Development, O=Local Build, C=JP'
```

`app/build.gradle` の `signingConfigs.release` は、この鍵のパス・別名・パスワードと一致させます。秘密鍵を Git や配布アーカイブへ含めないでください。

メモリに余裕がある環境では、次で通常の release assemble を実行できます。

```sh
./gradlew --no-daemon --max-workers=1 :app:assembleRelease
```

制約のある環境で release lint が過大にメモリを使う場合、コンパイル済み依存を使って署名済み APK を生成する次のタスクを実行します。

```sh
./gradlew --no-daemon --max-workers=1 :app:packageRelease
```

出力先は次です。

```text
app/build/outputs/apk/release/app-release.apk
```

## 9. APK の検証

Build Tools 36.0.0 の例です。

```sh
$ANDROID_SDK_ROOT/build-tools/36.0.0/apksigner verify --verbose app/build/outputs/apk/release/app-release.apk
$ANDROID_SDK_ROOT/build-tools/36.0.0/zipalign -c -P 16 -v 4 app/build/outputs/apk/release/app-release.apk
$ANDROID_SDK_ROOT/build-tools/36.0.0/aapt dump badging app/build/outputs/apk/release/app-release.apk
sha256sum app/build/outputs/apk/release/app-release.apk
```

検証時には、少なくとも次を確認します。

| 確認項目 | 期待値 |
|---|---|
| `launchable-activity` | `com.android.launcher3.Launcher` |
| APK package | `org.launcher3.universal` |
| `provides-component` | `launcher` |
| 署名 | APK Signature Scheme v2 が `true` |
| DEX | `LauncherApplication`、`Launcher`、`Workspace` を含む |

## 10. 実機検証とログ取得

インストール後は、まず通常起動し、次に既定ホームへ切り替えます。問題発生時は以下を実行し、再現直後に停止してログ全文を保存します。

```sh
adb logcat -c
adb logcat -v threadtime AndroidRuntime:E ActivityTaskManager:E ActivityManager:E Launcher3:V org.launcher3.universal:V *:S > launcher3-first-run.log
```

クラッシュ後には次の情報も採取します。

```sh
adb logcat -b crash -d -v threadtime > launcher3-crash-buffer.log
adb shell dumpsys package org.launcher3.universal > launcher3-package.txt
```

ログとともに、端末メーカー、機種、Android バージョン、既定ランチャー、画面密度、再現手順を記録してください。特に OEM が独自のホーム制限・ウィジェット・ジェスチャー処理を実装している場合、ここが次の移植修正の判断材料になります。

## 11. 上流更新時の手順

上流の Launcher3 を更新する場合は、互換レイヤーを先にコピーせず、次の順序を守ります。

1. `source/` を新しい上流リビジョンへ置き換えます。
2. `./gradlew :app:assembleDebug` を実行し、最初のコンパイルエラーを記録します。
3. 公開 Maven で解決できる依存を先に更新します。
4. SystemUI、Shell、Aconfig、非公開 framework API の各エラーを `port/` の小さな互換クラスへ隔離します。
5. 元の `source/` の修正は import 置換など最小限に保ち、パッチ一覧を残します。
6. 実機ログで起動・既定ホーム・アプリ一覧・フォルダー・ウィジェットを順に再検証します。

> Quickstep の追加は単なるコンパイル問題ではありません。recents animation、navigation mode、Shell transition、SystemUI とのプロセス横断連携を前提とするため、通常アプリ版では別フェーズの設計・実装対象として扱ってください。

## 12. ライセンスと配布上の注意

Launcher3 と IconLoader の上流ファイルに含まれる Apache License 2.0 の著作権・ライセンス表示を残してください。[1] [4] APK の署名鍵は配布物に含めず、信頼できる鍵管理手段を使ってください。アプリストアへ公開する場合は、パッケージ可視性、通話権限、通知リスナー、ウィジェット、既定ホームアプリに関する各ストアおよび Android のポリシーを別途確認してください。

## References

[1]: https://android.googlesource.com/platform/packages/apps/Launcher3/+/refs/heads/android16-release/Android.bp "AOSP Launcher3 Android 16 Android.bp"
[2]: https://developer.android.com/tools/sdkmanager "Android SDK Manager command-line tool"
[3]: https://github.com/Goooler/Launcher3/blob/trunk/build.gradle "Launcher3 Gradle source-set migration example"
[4]: https://android.googlesource.com/platform/frameworks/libs/systemui/+/refs/heads/android16-release/iconloaderlib/ "AOSP IconLoader Android 16 source"
