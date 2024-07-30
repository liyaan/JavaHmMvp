package com.wefika.calendar.manager;

import com.wefika.calendar.CollapseCalendarView;
import com.wefika.calendar.annotation.NonNull;
import com.wefika.calendar.models.AbstractViewHolder;
import com.wefika.calendar.models.SizeViewHolder;
import com.wefika.calendar.models.StubViewHolder;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;

/**
 * Created by Blaz Solar on 17/04/14.
 */
public class ProgressManagerImpl extends ProgressManager {

    public ProgressManagerImpl(@NonNull CollapseCalendarView calendarView, int activeWeek, boolean fromMonth) {
        super(calendarView, activeWeek, fromMonth);

        if (!fromMonth) {
            initMonthView();
        } else {
            initWeekView();
        }

    }

    @Override
    public void finish(final boolean expanded) {
        new EventHandler(EventRunner.getMainEventRunner()).postTask(new Runnable() {
            @Override
            public void run() {
                mCalendarView.getLayoutConfig().height = ComponentContainer.LayoutConfig.MATCH_CONTENT;
                mWeeksView.getLayoutConfig().height = ComponentContainer.LayoutConfig.MATCH_CONTENT;
                if(mViews != null){
                    for (AbstractViewHolder view : mViews) {
                        view.onFinish(true);
                    }
                }

                if (!expanded) {
                    CalendarManager manager = mCalendarView.getManager();
                    if (mFromMonth) {
                        manager.toggleView();
                    } else {
                        manager.toggleToWeek(mActiveIndex);
                    }
                    mCalendarView.populateLayout();
                }
            }
        });
    }

    private void initMonthView() {

        mCalendarHolder = new SizeViewHolder(mCalendarView.getHeight(), 0);
        mCalendarHolder.setView(mCalendarView);
        mCalendarHolder.setDelay(0);
        mCalendarHolder.setDuration(1);

        mWeeksHolder = new SizeViewHolder(mWeeksView.getHeight(), 0);
        mWeeksHolder.setView(mWeeksView);
        mWeeksHolder.setDelay(0);
        mWeeksHolder.setDuration(1);

    }

    public void onPreDrawMonth(){
        mCalendarHolder.setMaxHeight(mCalendarView.getHeight());
        mWeeksHolder.setMaxHeight(mWeeksView.getHeight());

        mCalendarView.getLayoutConfig().height = mCalendarHolder.getMinHeight();
        mWeeksView.getLayoutConfig().height = mCalendarHolder.getMinHeight();

        initializeChildren();

        setInitialized(true);
    }

    private void initWeekView() {

        mCalendarHolder = new SizeViewHolder(0, mCalendarView.getHeight());
        mCalendarHolder.setView(mCalendarView);
        mCalendarHolder.setDelay(0);
        mCalendarHolder.setDuration(1);

        mWeeksHolder = new SizeViewHolder(0, mWeeksView.getHeight());
        mWeeksHolder.setView(mWeeksView);
        mWeeksHolder.setDelay(0);
        mWeeksHolder.setDuration(1);

        initializeChildren();
    }

    public void onPreDrawWeek(){
        mCalendarHolder.setMinHeight(mCalendarView.getHeight());
        mWeeksHolder.setMinHeight(mWeeksView.getHeight());

        mCalendarView.getLayoutConfig().height = mCalendarHolder.getMaxHeight();
        mWeeksView.getLayoutConfig().height = mCalendarHolder.getMaxHeight();

        setInitialized(true);
    }

    private void initializeChildren() {

        int childCount = mWeeksView.getChildCount();

        // FIXME do not assume that all views are the same height
        mViews = new AbstractViewHolder[childCount];
        for (int i = 0; i < childCount; i++) {

            Component view = mWeeksView.getComponentAt(i);

            int activeIndex = getActiveIndex();

            AbstractViewHolder holder;
            if (i == activeIndex) {
                holder = new StubViewHolder();
            } else {
                SizeViewHolder tmpHolder = new SizeViewHolder(0, view.getHeight());

                final int duration = mWeeksHolder.getMaxHeight() - view.getHeight();

                if (i < activeIndex) {
                    tmpHolder.setDelay(view.getTop() * 1.0f / duration);
                } else {
                    tmpHolder.setDelay((view.getTop() - view.getHeight()) * 1.0f / duration);
                }
                tmpHolder.setDuration(view.getHeight() * 1.0f / duration);

                holder = tmpHolder;

                view.setVisibility(Component.HIDE);
            }

            holder.setView(view);

            mViews[i] = holder;
        }

    }

}
