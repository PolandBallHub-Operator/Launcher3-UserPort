# Open Source Licenses change set

"If you fork Launcher3 and want to use a different license notation, you can change it here."

## Placement

| File | Placement in repository | Purpose |
|---|---|---|
| `source/res/xml/launcher_preferences.xml` | `source/res/xml/` | Adds the preference row to Home settings. |
| `source/res/values/strings.xml` | `source/res/values/` | Adds the preference title and summary. |
| `source/res/raw/open_source_license.txt` | `source/res/raw/` | Bundled license text displayed in the viewer. |
| `source/src/com/android/launcher3/settings/SettingsActivity.java` | `source/src/com/android/launcher3/settings/` | Handles the preference click and launches the viewer. |
| `port/src/main/java/com/android/launcher3/settings/OpenSourceLicenseActivity.java` | `port/src/main/java/com/android/launcher3/settings/` | Scrollable in-app license viewer. |
| `port/AndroidManifest.xml` | `port/` | Registers the viewer activity as an internal activity. |

