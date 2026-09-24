package com.android.app.animation;

import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.Interpolator;

/** Public SDK replacement for the AOSP shared animation interpolator collection. */
public final class Interpolators {
    private Interpolators() { }
    public static final Interpolator LINEAR = new LinearInterpolator();
    public static final Interpolator INSTANT = input -> 1f;
    public static final Interpolator FINAL_FRAME = input -> 1f;
    public static final Interpolator ACCELERATE = new AccelerateInterpolator(1f);
    public static final Interpolator ACCELERATE_2 = new AccelerateInterpolator(2f);
    public static final Interpolator ACCELERATE_DECELERATE = new AccelerateDecelerateInterpolator();
    public static final Interpolator DECELERATE = new DecelerateInterpolator(1f);
    public static final Interpolator DECELERATE_1_5 = new DecelerateInterpolator(1.5f);
    public static final Interpolator DECELERATE_1_7 = new DecelerateInterpolator(1.7f);
    public static final Interpolator DECELERATE_2 = new DecelerateInterpolator(2f);
    public static final Interpolator DECELERATE_3 = new DecelerateInterpolator(3f);
    public static final Interpolator DECELERATE_QUINT = new DecelerateInterpolator(5f);
    public static final Interpolator DECELERATED_EASE = new DecelerateInterpolator(1.5f);
    public static final Interpolator EMPHASIZED = new DecelerateInterpolator(1.5f);
    public static final Interpolator EMPHASIZED_ACCELERATE = new AccelerateInterpolator(1.5f);
    public static final Interpolator EMPHASIZED_DECELERATE = new DecelerateInterpolator(1.5f);
    public static final Interpolator FAST_OUT_SLOW_IN = new AccelerateDecelerateInterpolator();
    public static final Interpolator SCROLL = new DecelerateInterpolator(1.2f);
    public static final Interpolator SCROLL_CUBIC = new DecelerateInterpolator(2f);
    public static final Interpolator STANDARD = new AccelerateDecelerateInterpolator();
    public static final Interpolator ZOOM_OUT = new DecelerateInterpolator(1.2f);

    public static Interpolator clampToProgress(
            final Interpolator interpolator, final float lowerBound, final float upperBound) {
        return input -> {
            if (input <= lowerBound) return 0f;
            if (input >= upperBound) return 1f;
            return interpolator.getInterpolation((input - lowerBound) / (upperBound - lowerBound));
        };
    }

    public static float clampToProgress(float progress, float lowerBound, float upperBound) {
        if (progress <= lowerBound) return 0f;
        if (progress >= upperBound) return 1f;
        return (progress - lowerBound) / (upperBound - lowerBound);
    }

    public static Interpolator mapToProgress(
            final Interpolator interpolator, final float start, final float end) {
        return input -> start + (end - start) * interpolator.getInterpolation(input);
    }

    public static Interpolator reverse(final Interpolator interpolator) {
        return input -> 1f - interpolator.getInterpolation(1f - input);
    }

    public static Interpolator scrollInterpolatorForVelocity(float velocity) {
        return velocity == 0f ? LINEAR : new DecelerateInterpolator(1.2f);
    }
}
