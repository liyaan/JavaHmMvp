package com.example.utils.net;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.InnerEvent;

import java.util.concurrent.TimeUnit;

public class HandlerScheduler extends Scheduler {
    private final EventHandler handler;
    private final boolean async;

    HandlerScheduler(EventHandler handler, boolean async) {
        this.handler = handler;
        this.async = async;
    }

    @Override
    public @NonNull Disposable scheduleDirect(@NonNull Runnable run, long delay, @NonNull TimeUnit unit) {
        if (run == null) throw new NullPointerException("run == null");
        if (unit == null) throw new NullPointerException("unit == null");

        run = RxJavaPlugins.onSchedule(run);
        ScheduledRunnable scheduled = new ScheduledRunnable(handler, run);
        InnerEvent innerEvent = InnerEvent.get(scheduled);
        handler.sendEvent(innerEvent, unit.toMillis(delay));

        return scheduled;
    }

    @Override
    public @NonNull Worker createWorker() {
        return new HandlerWorker(handler, async);
    }

    private static final class HandlerWorker extends Worker {
        private final EventHandler handler;
        private final boolean async;

        private volatile boolean disposed;

        HandlerWorker(EventHandler handler, boolean async) {
            this.handler = handler;
            this.async = async;
        }

        @Override
        public Disposable schedule(Runnable run, long delay, TimeUnit unit) {
            if (run == null) throw new NullPointerException("run == null");
            if (unit == null) throw new NullPointerException("unit == null");

            if (disposed) {
                return Disposable.disposed();
            }

            run = RxJavaPlugins.onSchedule(run);

            ScheduledRunnable scheduled = new ScheduledRunnable(handler, run);

            InnerEvent innerEvent = InnerEvent.get(scheduled);
            innerEvent.object = this;

            handler.sendEvent(innerEvent, unit.toMillis(delay));

            // Re-check disposed state for removing in case we were racing a call to dispose().
            if (disposed) {
                handler.removeTask(scheduled);
                return Disposable.disposed();
            }

            return scheduled;
        }

        @Override
        public void dispose() {
            disposed = true;
            handler.removeAllEvent();
        }

        @Override
        public boolean isDisposed() {
            return disposed;
        }
    }

    private static final class ScheduledRunnable implements Runnable, Disposable {
        private final EventHandler handler;
        private final Runnable delegate;

        private volatile boolean disposed; // Tracked solely for isDisposed().

        ScheduledRunnable(EventHandler handler, Runnable delegate) {
            this.handler = handler;
            this.delegate = delegate;
        }

        @Override
        public void run() {
            try {
                delegate.run();
            } catch (Throwable t) {
                RxJavaPlugins.onError(t);
            }
        }

        @Override
        public void dispose() {
            handler.removeTask(this);
            disposed = true;
        }

        @Override
        public boolean isDisposed() {
            return disposed;
        }
    }
}
