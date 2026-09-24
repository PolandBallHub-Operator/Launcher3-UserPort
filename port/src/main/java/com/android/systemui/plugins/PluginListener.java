package com.android.systemui.plugins;

import android.content.Context;

/** Source-compatible listener contract; plugin discovery is intentionally disabled in user mode. */
public interface PluginListener<T extends Plugin> {
    void onPluginConnected(T plugin, Context context);
    void onPluginDisconnected(T plugin);
}
