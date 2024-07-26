package com.example.utils.component.timePicker.dialog;


import com.example.utils.component.timePicker.widget.SingleDateAndTimeConstants;
import ohos.agp.utils.Color;



import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * Created by nor on 1/2/2017.
 */

public abstract class BaseDialog {
    public static final int DEFAULT_ITEM_COUNT_MODE_CURVED = 7;
    public static final int DEFAULT_ITEM_COUNT_MODE_NORMAL = 5;

    private boolean isDisplaying;

    protected Color backgroundColor = Color.WHITE;

    protected Color mainColor = Color.BLUE;

    protected Integer titleTextColor = null;

    protected boolean okClicked = false;
    protected boolean curved = false;
    protected boolean mustBeOnFuture = false;
    protected int minutesStep = SingleDateAndTimeConstants.STEP_MINUTES_DEFAULT;

    protected Date minDate;
    protected Date maxDate;
    protected Date defaultDate;

    protected boolean displayDays;
    protected boolean displayMinutes;
    protected boolean displayHours;
    protected boolean displayDaysOfMonth;
    protected boolean displayMonth;
    protected boolean displayYears;
    protected boolean displayMonthNumbers;

    protected Boolean isAmPm;

    protected SimpleDateFormat dayFormatter;

    protected Locale customLocale;

    public void display() {
        this.isDisplaying = true;
    }

    public void close() {
        this.isDisplaying = false;
    }

    public void dismiss() {
        this.isDisplaying = false;
    }

    public boolean isDisplaying() {
        return isDisplaying;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setMainColor(Color mainColor) {
        this.mainColor = mainColor;
    }

    public void setTitleTextColor(int titleTextColor) {
        this.titleTextColor = titleTextColor;
    }

    protected void onClose() {
        this.isDisplaying = false;
    }
}
