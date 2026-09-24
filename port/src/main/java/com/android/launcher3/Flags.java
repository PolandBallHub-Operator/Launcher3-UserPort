package com.android.launcher3;

/**
 * Public-app replacement for the AOSP aconfig generated flags class.
 * Platform-only and experimental paths default to disabled. Stable user-facing behavior remains
 * in the original Launcher3 code paths that do not require privileged integration.
 */
public final class Flags {
    private Flags() { }

    public static final boolean QSB_ON_FIRST_SCREEN = true;
    public static final boolean PROMISE_APPS_IN_ALL_APPS = true;
    public static final boolean IS_STUDIO_BUILD = false;

    public static boolean allAppsBlur() { return false; }
    public static boolean enableAddAppWidgetViaConfigActivityV2() { return false; }
    public static boolean allAppsSheetForHandheld() { return true; }
    public static boolean allowPrivateProfile() { return false; }
    public static boolean enableAdditionalHomeAnimations() { return false; }
    public static boolean enableAllAppsButtonInHotseat() { return false; }
    public static boolean enableContrastTiles() { return false; }
    public static boolean enableCursorHoverStates() { return true; }
    public static boolean enableExpandingPauseWorkButton() { return false; }
    public static boolean enableFallbackOverviewInWindow() { return false; }
    public static boolean enableDesktopWindowingMode() { return false; }
    public static boolean enableDesktopWindowingWallpaperActivity() { return false; }
    public static boolean enableDismissPredictionUndo() { return false; }
    public static boolean enableFirstScreenBroadcastArchivingExtras() { return false; }
    public static boolean enableFocusOutline() { return true; }
    public static boolean enableGeneratedPreviews() { return false; }
    public static boolean enableGridOnlyOverview() { return false; }
    public static boolean enableHomeTransitionListener() { return false; }
    public static boolean enableMouseInteractionChanges() { return true; }
    public static boolean enableMovingContentIntoPrivateSpace() { return false; }
    public static boolean enableOverviewIconMenu() { return false; }
    public static boolean enableOverviewOnConnectedDisplays() { return false; }
    public static boolean enableLauncherBrMetricsFixed() { return false; }
    public static boolean enableLauncherIconShapes() { return true; }
    public static boolean enableLauncherOverviewInWindow() { return false; }
    public static boolean enableLauncherVisualRefresh() { return false; }
    public static boolean enablePrivateSpace() { return false; }
    public static boolean enableResponsiveWorkspace() { return true; }
    public static boolean enableRetrievableBubbles() { return false; }
    public static boolean enableShortcutDontSuggestApp() { return false; }
    public static boolean enableSmartspaceAsAWidget() { return false; }
    public static boolean enableSmartspaceRemovalToggle() { return false; }
    public static boolean enableStateManagerProtoLog() { return false; }
    public static boolean enableStrictMode() { return false; }
    public static boolean enableScalingRevealHomeAnimation() { return false; }
    public static boolean enableSupportForArchiving() { return false; }
    public static boolean enableTaskbarNoRecreate() { return false; }
    public static boolean enableTaskbarPinning() { return false; }
    public static boolean enableTieredWidgetsByDefaultInPicker() { return false; }
    public static boolean enableTwoPaneLauncherSettings() { return false; }
    public static boolean enableTwolineToggle() { return false; }
    public static boolean enableWorkspaceInflation() { return false; }
    public static boolean extendibleThemeManager() { return false; }
    public static boolean floatingSearchBar() { return false; }
    public static boolean forceMonochromeAppIcons() { return false; }
    public static boolean generatedPreviews() { return false; }
    public static boolean gridMigrationRefactor() { return false; }
    public static boolean letterFastScroller() { return true; }
    public static boolean msdlFeedback() { return false; }
    public static boolean multilineSearchBar() { return false; }
    public static boolean navigateToChildPreference() { return false; }
    public static boolean newCustomizationPickerUi() { return false; }
    public static boolean oneGridRotationHandling() { return false; }
    public static boolean oneGridSpecs() { return false; }
    public static boolean privateSpaceAddFloatingMaskView() { return false; }
    public static boolean privateSpaceAnimation() { return false; }
    public static boolean privateSpaceAppInstallerButton() { return false; }
    public static boolean privateSpaceRestrictAccessibilityDrag() { return false; }
    public static boolean privateSpaceRestrictItemDrag() { return false; }
    public static boolean privateSpaceSysAppsSeparation() { return false; }
    public static boolean removeAppsRefreshOnRightClick() { return false; }
    public static boolean restoreArchivedAppIconsFromDb() { return false; }
    public static boolean restoreArchivedShortcuts() { return false; }
    public static boolean useNewIconForArchivedApps() { return false; }
    public static boolean useSystemRadiusForAppWidgets() { return false; }
    public static boolean workSchedulerInWorkProfile() { return false; }
}
