# Launcher3 本体のユーザーアプリ移植監査

## 固定した一次ソース

指定された公式ブランチ `android16-release` は、2025-04-12 のコミット `2f5b9b869d86620fdb899bc2df53d523dc9dc6ea` を指している。取得した `source/` は、このブランチのアーカイブから展開した本体ソースである。

## ビルド方式と阻害要因

現在の公式ソースは `Android.bp` により AOSP/Soong ビルドを定義している。通常版 `Launcher3` は、`src/`、`src_no_quickstep/`、`src_build_config/` を対象とするが、`Launcher3ResLib` を通じて AndroidX だけでなく、SystemUI 共有ライブラリ、WindowManager-Shell、AOSP flags、Dagger、プロト生成物に依存する。さらに公式定義では `privileged: true`、`system_ext_specific: true` を指定しており、同じ APK を通常アプリとして再配布することはできない。

一方、公式リポジトリには過去の Gradle 定義があり、`src` と `src_plugins` をアプリのソースセットに直接指定し、Quickstep を含まない `withoutQuickstep` フレーバーを定義していた。この構成は、現在のソースでも本体クラスを直接 Gradle でコンパイルするための出発点になる。外部の現行ミラーも同じ `src` 直接コンパイル方式を維持しているが、`IconLoader`、`SharedLibWrapper`、SystemUI 共有 JAR／ダミーモジュールを別途必要とする。

## 移植戦略

初期の直接ビルド対象は、Quickstep と platform-only recents を外した **Launcher3 without Quickstep** とする。`source/src`、`source/src_no_quickstep`、`source/src_build_config`、`source/src_plugins` を Gradle の同一アプリモジュールへ直接接続する。AOSP 内部の依存は、公開 Maven 依存に変換できるものを置換し、置換不能な SystemUI / hidden API / privileged API は、機能境界に沿った互換クラスまたは no-op 実装に限定して差し替える。

この方針では、起動・ワークスペース・アプリ一覧・フォルダー・ウィジェットホストなどの Launcher3 本体を実際にコンパイル対象へ含める。プラットフォーム専用の Quickstep、ジェスチャーナビゲーション統合、recents animation、SystemUI 側のログ・共有トランジションは初期ユーザーアプリ版から除外する。

## 参照

- 公式 Android 16 source: https://android.googlesource.com/platform/packages/apps/Launcher3/+/refs/heads/android16-release
- 過去の公式 Gradle 定義: https://android.googlesource.com/platform/packages/apps/Launcher3/+/01efb3d4c3/build.gradle
- 現行ミラーの Gradle 定義: https://github.com/Goooler/Launcher3/blob/trunk/build.gradle
