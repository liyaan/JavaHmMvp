package com.example.utils.net;

import io.reactivex.rxjava3.core.Scheduler;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;

public final class HarmonySchedulers {

    private static final Scheduler MAIN_THREAD =
            RxHarmonyPlugins.initMainThreadScheduler(() -> MainHolder.DEFAULT);

    private HarmonySchedulers() {
        throw new AssertionError("No instances.");
    }

    public static Scheduler mainThread() {
        return RxHarmonyPlugins.onMainThreadScheduler(MAIN_THREAD);
    }

    public static Scheduler from(EventRunner looper) {
        return from(looper, true);
    }

    public static Scheduler from(EventRunner looper, boolean async) {
        if (looper == null) throw new NullPointerException("looper == null");

        return new HandlerScheduler(new EventHandler(looper), async);
    }

    private static final class MainHolder {
        static final Scheduler DEFAULT
                = new HandlerScheduler(new EventHandler(EventRunner.current()), true);
    }
}
