package com.wefika.calendar.manager;

import com.wefika.calendar.CollapseCalendarView;
import com.wefika.calendar.annotation.NonNull;
import com.wefika.calendar.models.AbstractViewHolder;
import com.wefika.calendar.models.SizeViewHolder;
import ohos.agp.components.DirectionalLayout;

/**
 * Created by Blaz Solar on 17/04/14.
 */
public abstract class ProgressManager {

    private static final String TAG = "ProgressManager";

    @NonNull
    protected CollapseCalendarView mCalendarView;

    protected DirectionalLayout mWeeksView;
    protected AbstractViewHolder[] mViews;

    protected SizeViewHolder mCalendarHolder;
    protected SizeViewHolder mWeeksHolder;

    final int mActiveIndex;

    private boolean mInitialized = false;

    final boolean mFromMonth;

    protected ProgressManager(@NonNull CollapseCalendarView calendarView, int activeWeek, boolean fromMonth) {
        mCalendarView = calendarView;
        mWeeksView = calendarView.getWeeksView();
        mActiveIndex = activeWeek;
        mFromMonth = fromMonth;
    }

    public void applyDelta(float delta) {
        int distance = getDeltaInBounds(delta);
        float progress = getProgress(distance);
        apply(progress);
    }

    public void apply(float progress) {
        mCalendarHolder.animate(progress);
        mWeeksHolder.animate(progress);

        // animate views if necessary
        if (mViews != null) {
            for (AbstractViewHolder view : mViews) {
                view.animate(progress);
            }
        }

        // request layout
        mCalendarView.postLayout();
        mCalendarView.invalidate();
    }

    public boolean isInitialized() {
        return mInitialized;
    }

    void setInitialized(boolean initialized) {
        mInitialized = initialized;
    }

    public int getCurrentHeight() {
        return mCalendarView.getLayoutConfig().height - mCalendarHolder.getMinHeight();
    }

    public int getStartSize() {
        return 0;
    }

    public int getEndSize() {
        return mCalendarHolder.getHeight();
    }

    public abstract void finish(boolean expanded);

    public float getProgress(int distance) {
        return Math.max(0, Math.min(distance * 1f / mCalendarHolder.getHeight(), 1));
    }

    protected int getActiveIndex() {
        return mActiveIndex;
    }

    private int getDeltaInBounds(float delta) {
        if (mFromMonth) {
            return (int) Math.max(-mCalendarHolder.getHeight(), Math.min(0, delta)) + mCalendarHolder.getHeight();
        } else {
            return (int) Math.max(0, Math.min(mCalendarHolder.getHeight(), delta));
        }

    }

}
