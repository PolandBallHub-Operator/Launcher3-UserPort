package com.google.android.msdl.domain;

import android.os.Vibrator;
import com.google.android.msdl.data.model.MSDLToken;
import com.google.android.msdl.logging.MSDLEvent;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * Compatibility no-op for the proprietary MSDL runtime. Launcher3 remains source-compatible;
 * vibration feedback is deliberately omitted in the initial user-app port.
 */
public final class MSDLPlayer {
    public static final Companion Companion = new Companion();

    public static final class Companion {
        public MSDLPlayer createPlayer(Vibrator vibrator, Executor executor, Object callback) {
            return new MSDLPlayer();
        }
    }

    public void playToken(MSDLToken token, InteractionProperties properties) { }
    public List<MSDLEvent> getHistory() { return Collections.emptyList(); }
    @Override public String toString() { return "MSDLPlayer(disabled)"; }
}
