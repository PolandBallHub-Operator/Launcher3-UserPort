package com.google.android.msdl.data.model;

/** Minimal token model used when the proprietary MSDL feedback runtime is unavailable. */
public final class MSDLToken {
    public static final MSDLToken DRAG_INDICATOR_DISCRETE = new MSDLToken("DRAG_INDICATOR_DISCRETE");
    public static final MSDLToken SWIPE_THRESHOLD_INDICATOR = new MSDLToken("SWIPE_THRESHOLD_INDICATOR");
    public static final MSDLToken TAP_HIGH_EMPHASIS = new MSDLToken("TAP_HIGH_EMPHASIS");

    private final String name;
    private MSDLToken(String name) { this.name = name; }
    @Override public String toString() { return name; }
}
