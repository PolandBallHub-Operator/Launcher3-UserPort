package com.android.wm.shell.shared.bubbles;

/** Public-app compatibility replacement; arbitrary bubble creation requires Shell integration. */
public final class BubbleAnythingFlagHelper {
    private BubbleAnythingFlagHelper() { }
    public static boolean enableCreateAnyBubble() { return false; }
}
