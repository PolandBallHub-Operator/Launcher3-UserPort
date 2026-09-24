package com.android.launcher3.icons;

/** Bridges IconLoader's original library namespace to the merged Launcher3 APK resources. */
public final class R {
    private R() { }

    public static final class attr {
        public static final int disabledIconAlpha = com.android.launcher3.R.attr.disabledIconAlpha;
        public static final int loadingIconColor = com.android.launcher3.R.attr.loadingIconColor;
        public static final int state_hovered = android.R.attr.state_hovered;
        public static final int state_pressed = android.R.attr.state_pressed;
        private attr() { }
    }

    public static final class bool {
        public static final int enable_forced_themed_icon = com.android.launcher3.R.bool.enable_forced_themed_icon;
        private bool() { }
    }

    public static final class color {
        public static final int badge_tint_clone = com.android.launcher3.R.color.badge_tint_clone;
        public static final int badge_tint_instant = com.android.launcher3.R.color.badge_tint_instant;
        public static final int badge_tint_private = com.android.launcher3.R.color.badge_tint_private;
        public static final int badge_tint_work = com.android.launcher3.R.color.badge_tint_work;
        public static final int themed_badge_icon_background_color = com.android.launcher3.R.color.themed_badge_icon_background_color;
        public static final int themed_badge_icon_color = com.android.launcher3.R.color.themed_badge_icon_color;
        public static final int themed_icon_background_color = com.android.launcher3.R.color.themed_icon_background_color;
        public static final int themed_icon_color = com.android.launcher3.R.color.themed_icon_color;
        private color() { }
    }

    public static final class dimen {
        public static final int default_icon_bitmap_size = com.android.launcher3.R.dimen.default_icon_bitmap_size;
        private dimen() { }
    }

    public static final class drawable {
        public static final int ic_clone_app_badge = com.android.launcher3.R.drawable.ic_clone_app_badge;
        public static final int ic_instant_app_badge = com.android.launcher3.R.drawable.ic_instant_app_badge;
        public static final int ic_private_profile_app_badge = com.android.launcher3.R.drawable.ic_private_profile_app_badge;
        public static final int ic_work_app_badge = com.android.launcher3.R.drawable.ic_work_app_badge;
        public static final int sym_def_app_icon = android.R.drawable.sym_def_app_icon;
        private drawable() { }
    }

    public static final class string {
        public static final int cache_db_name = com.android.launcher3.R.string.cache_db_name;
        public static final int calendar_component_name = com.android.launcher3.R.string.calendar_component_name;
        public static final int clock_component_name = com.android.launcher3.R.string.clock_component_name;
        private string() { }
    }
}
