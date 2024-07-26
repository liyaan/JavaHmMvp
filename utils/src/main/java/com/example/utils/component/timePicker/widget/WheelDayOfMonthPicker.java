package com.example.utils.component.timePicker.widget;


import ohos.agp.components.AttrSet;
import ohos.app.Context;

import java.util.ArrayList;
import java.util.List;

public class WheelDayOfMonthPicker extends WheelPicker<String> {
    private int daysInMonth;
    private DayOfMonthSelectedListener listener;
    private FinishedLoopListener finishedLoopListener;

    public WheelDayOfMonthPicker(Context context) {
        super(context);
    }

    public WheelDayOfMonthPicker(Context context, AttrSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init() {
        // no-op here
    }

    @Override
    protected List<String> generateAdapterValues(boolean showOnlyFutureDates) {
        final List<String> dayList = new ArrayList<>();

        for (int i = 1; i <= daysInMonth; i++) {
            dayList.add(String.format("%02d", i));
        }

        return dayList;
    }


    @Override
    protected String initDefault() {
        return String.valueOf(dateHelper.getDay(dateHelper.today()));
    }

    public void setOnFinishedLoopListener(FinishedLoopListener finishedLoopListener) {
        this.finishedLoopListener = finishedLoopListener;
    }

    @Override
    protected void onFinishedLoop() {
        super.onFinishedLoop();
        if (finishedLoopListener != null) {
            finishedLoopListener.onFinishedLoop(this);
        }
    }

    public void setDayOfMonthSelectedListener(DayOfMonthSelectedListener listener) {
        this.listener = listener;
    }

    public int getDaysInMonth() {
        return daysInMonth;
    }

    public void setDaysInMonth(int daysInMonth) {
        this.daysInMonth = daysInMonth;
    }

    @Override
    protected void onItemSelected(int position, String item) {
        if (listener != null) {
            listener.onDayOfMonthSelected(this, position);
        }
    }

    public int getCurrentDay() {
        return getCurrentItemPosition();
    }

    public interface FinishedLoopListener {
        void onFinishedLoop(WheelDayOfMonthPicker picker);
    }

    public interface DayOfMonthSelectedListener {
        void onDayOfMonthSelected(WheelDayOfMonthPicker picker, int dayIndex);
    }
}