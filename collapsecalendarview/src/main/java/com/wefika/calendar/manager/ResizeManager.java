package com.wefika.calendar.manager;

import com.wefika.calendar.CollapseCalendarView;
import com.wefika.calendar.annotation.NonNull;
import com.wefika.calendar.annotation.Nullable;
import com.wefika.calendar.detector.VelocityTracker;
import com.wefika.calendar.detector.ViewConfiguration;
import ohos.agp.components.ScrollHelper;
import ohos.agp.components.VelocityDetector;
import ohos.multimodalinput.event.TouchEvent;

/**
 * Created by Blaz Solar on 17/04/14.
 */
public class ResizeManager {

    private static final String TAG = "ResizeManager";

    /**
     * View to resize
     */
    @NonNull
    private CollapseCalendarView mCalendarView;

    /**
     * Distance in px until drag has started
     */
    private final int mTouchSlop;

    private final int mMinFlingVelocity;

    private final int mMaxFlingVelocity;

    /**
     * Y position on
     */
    private float mDownY;

    /**
     * Y position when resizing started
     */
    private float mDragStartY;

    /**
     * If calendar is currently resizing.
     */
    private State mState = State.IDLE;

    private VelocityTracker mVelocityTracker;
    private final ScrollHelper mScroller;

    @Nullable
    private ProgressManagerImpl mProgressManager;

    private boolean isMonth;

    private String monthOrWeek = null;  // 1-month week-month null-初始化

    public ResizeManager(@NonNull CollapseCalendarView calendarView) {

        mCalendarView = calendarView;

        mScroller = new ScrollHelper();

        ViewConfiguration viewConfig = ViewConfiguration.get(mCalendarView.getContext());
        mTouchSlop = viewConfig.getScaledTouchSlop();
        mMinFlingVelocity = viewConfig.getScaledMinimumFlingVelocity();
        mMaxFlingVelocity = viewConfig.getScaledMaximumFlingVelocity();
    }

    public boolean onTouchEvent(@NonNull TouchEvent event) {
        final int action = event.getAction();

        if (action == TouchEvent.PRIMARY_POINT_DOWN) {
            onDownEvent(event);
        }

        if (action == TouchEvent.POINT_MOVE) {
            mVelocityTracker.addMovement(event);
        }
        if (mState == State.DRAGGING) {
            switch (action) {
                case TouchEvent.POINT_MOVE:
                    int deltaY = calculateDistanceForDrag(event);
                    mProgressManager.applyDelta(deltaY);
                    break;
                case TouchEvent.CANCEL:
                case TouchEvent.PRIMARY_POINT_UP:
                    finishMotionEvent();
                    break;
            }

        } else if (action == TouchEvent.POINT_MOVE) {
            checkForResizing(event);
        }

        return true;
    }

    /**
     * Triggered
     *
     * @param event Down event
     */
    private boolean onDownEvent(@NonNull TouchEvent event) {
        if (event.getAction() != TouchEvent.PRIMARY_POINT_DOWN) {
            throw new IllegalStateException("Has to be down event!");
        }

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        } else {
            mVelocityTracker.clear();
        }

        mDownY = event.getPointerPosition(0).getY();
        if (!mScroller.isFinished()) {
            if (mScroller.getFlingDistanceY(mScroller.getFlingVelocityY()) == 0) {
                mDragStartY = mDownY + mScroller.getScrollDistanceY() - mScroller.getCurrValue(ScrollHelper.AXIS_Y);
            } else {
                mDragStartY = mDownY - mScroller.getCurrValue(ScrollHelper.AXIS_Y);
            }
            mState = State.DRAGGING;
            return true;
        } else {
            return false;
        }

    }

    public void recycle() {
        if (mVelocityTracker != null) {
            mVelocityTracker.clear();
            mVelocityTracker = null;
        }
    }

    public boolean checkForResizing(TouchEvent ev) { // FIXME this method should only return true / false. Make another method for starting animation
        if (mState == State.DRAGGING) {
            return true;
        }

        final int yDIff = calculateDistance(ev);

        CalendarManager manager = mCalendarView.getManager();
        CalendarManager.State state = manager.getState();

        if (Math.abs(yDIff) > mTouchSlop) { // FIXME this should happen only if dragging int right direction
            mState = State.DRAGGING;
            mDragStartY = ev.getPointerPosition(0).getY();

            if (mProgressManager == null) {

                int weekOfMonth = manager.getWeekOfMonth();
                if (state == CalendarManager.State.WEEK) { // always animate in month view
                    manager.toggleView();
                    mCalendarView.populateLayout();
                }
                isMonth = state == CalendarManager.State.MONTH;
                mProgressManager = new ProgressManagerImpl(mCalendarView, weekOfMonth, isMonth);
            }

            return true;
        }

        return false;
    }

    private void finishMotionEvent() {
        if (mProgressManager != null && mProgressManager.isInitialized()) {
            startScolling();
        }
    }

    private void startScolling() {
        mVelocityTracker.calculateCurrentVelocity(1000, mMaxFlingVelocity);
        int velocity = (int) mVelocityTracker.getYVelocity();

        int progress = mProgressManager.getCurrentHeight();
        int end;
        if (Math.abs(velocity) > mMinFlingVelocity) {

            if (velocity > 0) {
                end = mProgressManager.getEndSize() - progress;
            } else {
                end = -progress;
            }

        } else {

            int endSize = mProgressManager.getEndSize();
            if (endSize / 2 <= progress) {
                end = endSize - progress;
            } else {
                end = -progress;
            }

        }

        mScroller.startScroll(0, progress, 0, end);
        mCalendarView.invalidate();

        mState = State.SETTLING;


    }

    private int calculateDistance(TouchEvent event) {
        return (int) (event.getPointerPosition(0).getY() - mDownY);
    }

    private int calculateDistanceForDrag(TouchEvent event) {
        return (int) (event.getPointerPosition(0).getY() - mDragStartY);
    }

    public void onDraw() {
        if (mProgressManager != null) {
            if (isMonth) {
                mProgressManager.onPreDrawMonth();
            } else {
                mProgressManager.onPreDrawWeek();
            }
        }
        System.out.println("Scroller isFinished["+mScroller.isFinished()+"]");
        if (!mScroller.isFinished()) {
            mScroller.updateScroll();
            int y = mScroller.getCurrValue(ScrollHelper.AXIS_Y);
            float position = y * 1f / mProgressManager.getEndSize();
            mProgressManager.apply(position);
        } else if (mState == State.SETTLING) {
            mState = State.IDLE;
            float position = mScroller.getCurrValue(ScrollHelper.AXIS_Y) * 1f / mProgressManager.getEndSize();
            mProgressManager.finish(position > 0);
            mProgressManager = null;
        }
    }

    private enum State {
        IDLE,
        DRAGGING,
        SETTLING
    }
}
