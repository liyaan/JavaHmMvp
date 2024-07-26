package com.example.utils.component.timePicker.widget;


import com.example.utils.component.timePicker.Utils;
import ohos.agp.components.AttrSet;
import ohos.app.Context;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class WheelMonthPicker extends WheelPicker<String> {

    private int lastScrollPosition;

    private MonthSelectedListener listener;

    private boolean displayMonthNumbers = false;

    public static final String MONTH_FORMAT = "MMMM";

    private String monthFormat;

    public WheelMonthPicker(Context context) {
        super(context);
    }

    public WheelMonthPicker(Context context, AttrSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init() {
    }

    @Override
    protected List<String> generateAdapterValues(boolean showOnlyFutureDates) {
        final List<String> monthList = new ArrayList<>();
        SimpleDateFormat month_date = new SimpleDateFormat(getMonthFormat(), getCurrentLocale());
        Calendar cal = Calendar.getInstance(getCurrentLocale());
        cal.setTimeZone(dateHelper.getTimeZone());
        cal.set(Calendar.DAY_OF_MONTH, 1);
        for (int i = 0; i < 12; i++) {
            cal.set(Calendar.MONTH, i);
            if (displayMonthNumbers) {
                monthList.add(String.format("%02d", i + 1));
            } else {
                monthList.add(month_date.format(cal.getTime()));
            }
        }
        return monthList;
    }


    @Override
    protected String initDefault() {
        return String.valueOf(dateHelper.getMonth(dateHelper.today()));
    }

    public void setOnMonthSelectedListener(MonthSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onItemSelected(int position, String item) {
        if (listener != null) {
            listener.onMonthSelected(this, position, item);
        }
    }

    @Override
    protected void onItemCurrentScroll(int position, String item) {
        if (lastScrollPosition != position) {
            onItemSelected(position, item);
            lastScrollPosition = position;
        }
    }

    public boolean displayMonthNumbers() {
        return displayMonthNumbers;
    }

    public void setDisplayMonthNumbers(boolean displayMonthNumbers) {
        this.displayMonthNumbers = displayMonthNumbers;
    }

    public int getCurrentMonth() {
        return getCurrentItemPosition();
    }

    public interface MonthSelectedListener {
        void onMonthSelected(WheelMonthPicker picker, int monthIndex, String monthName);
    }

    public void setMonthFormat(String format) {
        this.monthFormat = format;
    }

    public String getMonthFormat() {
        if (Utils.isEmptyStr(this.monthFormat)) {
            return MONTH_FORMAT;
        } else {
            return this.monthFormat;
        }
    }
}