package com.android.systemui.shared;

/** Public-app compatibility replacement for SystemUI shared generated flags. */
public final class Flags {
    private Flags() { }
    public static boolean enableLauncherIconShapes() { return true; }
    public static boolean extendibleThemeManager() { return false; }
    public static boolean newCustomizationPickerUi() { return false; }
    public static boolean oneGridSpecs() { return false; }
}
