package com.android.window.flags;

/** Public-app compatibility replacement for framework aconfig generated flags. */
public final class Flags {
    private Flags() { }
    public static boolean enableDesktopWindowingMode() { return false; }
    public static boolean enableDesktopWindowingWallpaperActivity() { return false; }
    public static boolean enableTaskbarConnectedDisplays() { return false; }
    public static boolean predictiveBackThreeButtonNav() { return false; }
}
