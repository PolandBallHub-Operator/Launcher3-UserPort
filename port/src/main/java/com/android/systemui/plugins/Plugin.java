package com.android.systemui.plugins;

/**
 * Minimal public-app compatibility contract for the SystemUI PluginCore interface.
 * Launcher3's user-app port keeps the interface for source compatibility but does not load
 * privileged SystemUI plugins.
 */
public interface Plugin {
    default void onCreate() { }
    default void onDestroy() { }
    default int getVersion() { return 1; }
}
