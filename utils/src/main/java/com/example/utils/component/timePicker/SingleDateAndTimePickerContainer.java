package com.example.utils.component.timePicker;

import com.example.utils.ResourceTable;
import com.example.utils.component.timePicker.widget.*;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.AttrHelper;
import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.Color;
import ohos.app.Context;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.global.i18n.text.DateFormatUtil;
import ohos.global.icu.text.DateFormat;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static com.example.utils.component.timePicker.widget.SingleDateAndTimeConstants.DAYS_PADDING;


public class SingleDateAndTimePickerContainer extends DirectionalLayout {
    public static final boolean IS_CYCLIC_DEFAULT = true;
    public static final boolean IS_CURVED_DEFAULT = false;
    public static final boolean MUST_BE_ON_FUTURE_DEFAULT = false;
    private static final int VISIBLE_ITEM_COUNT_DEFAULT = 7;
    private static final int PM_HOUR_ADDITION = 12;
    public static final int ALIGN_CENTER = 0;
    private DateHelper dateHelper = new DateHelper();
    private final EventHandler handler = new EventHandler(EventRunner.getMainEventRunner());
    private static final CharSequence FORMAT_24_HOUR = "EEE d MMM H:mm";
    private static final CharSequence FORMAT_12_HOUR = "EEE d MMM h:mm a";
    private final WheelYearPicker yearsPicker;
    private final WheelMonthPicker monthPicker;
    private final WheelDayOfMonthPicker daysOfMonthPicker;
    private final WheelDayPicker daysPicker;
    private final WheelMinutePicker minutesPicker;
    private final WheelHourPicker hoursPicker;
    private final WheelAmPmPicker amPmPicker;
    private List<WheelPicker> pickers = new ArrayList<>();
    private List<OnDateChangedListener> listeners = new ArrayList<>();
    private Component dtSelector;
    private boolean mustBeOnFuture;
    private Date minDate;
    private Date maxDate;
    private Date defaultDate;
    private boolean displayYears = false;
    private boolean displayMonth = false;
    private boolean displayDaysOfMonth = false;
    private boolean displayDays = true;
    private boolean displayMinutes = true;
    private boolean displayHours = true;

    private boolean isAmPm;
    private Calendar mSettingCalendar;

    public SingleDateAndTimePickerContainer(Context context, AttrSet attrs) {
        this(context, attrs, null);
    }

    public SingleDateAndTimePickerContainer(Context context, AttrSet attrs, String styleName) {
        super(context, attrs, styleName);
        defaultDate = new Date();
        isAmPm = !(DateFormatUtil.is24HourClock(context));
        LayoutScatter.getInstance(context).parse(ResourceTable.Layout_single_day_and_time_picker, this, true);

        yearsPicker = (WheelYearPicker) findComponentById(ResourceTable.Id_yearPicker);
        monthPicker = (WheelMonthPicker) findComponentById(ResourceTable.Id_monthPicker);
        daysOfMonthPicker = (WheelDayOfMonthPicker) findComponentById(ResourceTable.Id_daysOfMonthPicker);
        daysPicker = (WheelDayPicker) findComponentById(ResourceTable.Id_daysPicker);
        minutesPicker = (WheelMinutePicker) findComponentById(ResourceTable.Id_minutesPicker);
        hoursPicker = (WheelHourPicker) findComponentById(ResourceTable.Id_hoursPicker);
        amPmPicker = (WheelAmPmPicker) findComponentById(ResourceTable.Id_amPmPicker);
        dtSelector = findComponentById(ResourceTable.Id_dtSelector);

        pickers.addAll(Arrays.asList(
            daysPicker,
            minutesPicker,
            hoursPicker,
            amPmPicker,
            daysOfMonthPicker,
            monthPicker,
            yearsPicker
        ));
        for (WheelPicker wheelPicker : pickers) {
            wheelPicker.setDateHelper(dateHelper);
        }
        init(context, attrs);
    }

    public void setDateHelper(DateHelper dateHelper) {
        this.dateHelper = dateHelper;
    }

    public void stopPickersScroll() {
        for (WheelPicker wheelPicker : pickers) {
            wheelPicker.stopScroll();
        }
    }

    public void setTimeZone(TimeZone timeZone) {
        dateHelper.setTimeZone(timeZone);
    }


    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        for (WheelPicker picker : pickers) {
            picker.setEnabled(enabled);
        }
    }

    public void setDisplayYears(boolean displayYears) {
        this.displayYears = displayYears;
        yearsPicker.setVisibility(displayYears ? VISIBLE : HIDE);
    }

    public void setDisplayMonths(boolean displayMonths) {
        this.displayMonth = displayMonths;
        monthPicker.setVisibility(displayMonths ? VISIBLE : HIDE);
        checkSettings();
    }

    public void setDisplayDaysOfMonth(boolean displayDaysOfMonth) {
        this.displayDaysOfMonth = displayDaysOfMonth;
        daysOfMonthPicker.setVisibility(displayDaysOfMonth ? VISIBLE : HIDE);

        if (displayDaysOfMonth) {
            updateDaysOfMonth();
        }
        checkSettings();
    }

    public void setDisplayDays(boolean displayDays) {
        this.displayDays = displayDays;
        daysPicker.setVisibility(displayDays ? VISIBLE : HIDE);
        checkSettings();
    }

    public void setDisplayMinutes(boolean displayMinutes) {
        this.displayMinutes = displayMinutes;
        minutesPicker.setVisibility(displayMinutes ? VISIBLE : HIDE);
    }

    public void setDisplayHours(boolean displayHours) {
        this.displayHours = displayHours;
        hoursPicker.setVisibility(displayHours ? VISIBLE : HIDE);

        setIsAmPm(this.isAmPm);
        hoursPicker.setIsAmPm(isAmPm);
    }

    public void setDisplayMonthNumbers(boolean displayMonthNumbers) {
        this.monthPicker.setDisplayMonthNumbers(displayMonthNumbers);
        this.monthPicker.updateAdapter();
    }

    public void setMonthFormat(String monthFormat) {

        this.monthPicker.setMonthFormat(monthFormat);
        this.monthPicker.updateAdapter();
    }

    public void setTodayText(DateWithLabel todayText) {
        if (todayText != null && todayText.mLabel != null && !todayText.mLabel.isEmpty()) {
            daysPicker.setTodayText(todayText);
        }
    }

    public void setItemSpacing(int size) {
        for (WheelPicker picker : pickers) {
            picker.setItemSpace(size);
        }
    }

    public void setCurvedMaxAngle(int angle) {
        for (WheelPicker picker : pickers) {
            picker.setCurvedMaxAngle(angle);
        }
    }

    public void setCurved(boolean curved) {
        for (WheelPicker picker : pickers) {
            picker.setCurved(curved);
        }
    }

    public void setCyclic(boolean cyclic) {
        for (WheelPicker picker : pickers) {
            picker.setCyclic(cyclic);
        }
    }

    public void setTextSize(int textSize) {
        for (WheelPicker picker : pickers) {
            picker.setItemTextSize(textSize);
        }
    }

    public void setSelectedTextColor(int selectedTextColor) {
        for (WheelPicker picker : pickers) {
            picker.setSelectedItemTextColor(selectedTextColor);
        }
    }

    public void setTextColor(int textColor) {
        for (WheelPicker picker : pickers) {
            picker.setItemTextColor(textColor);
        }
    }

    public void setTextAlign(int align) {
        for (WheelPicker picker : pickers) {
            picker.setItemAlign(align);
        }
    }

    public void setSelectorColor(int selectorColor) {
        ShapeElement shapeElement = new ShapeElement();
        shapeElement.setRgbColor(RgbColor.fromArgbInt(selectorColor));
        dtSelector.setBackground(shapeElement);
    }

    public void setSelectorHeight(int selectorHeight) {
        final ComponentContainer.LayoutConfig dtSelectorLayoutParams = dtSelector.getLayoutConfig();
        dtSelectorLayoutParams.height = selectorHeight;
        dtSelector.setLayoutConfig(dtSelectorLayoutParams);
    }

    public void setVisibleItemCount(int visibleItemCount) {
        for (WheelPicker picker : pickers) {
            picker.setVisibleItemCount(visibleItemCount);
        }
    }

    public void setIsAmPm(boolean isAmPm) {
        this.isAmPm = isAmPm;

        amPmPicker.setVisibility((isAmPm && displayHours) ? VISIBLE : HIDE);
        hoursPicker.setIsAmPm(isAmPm);
    }

    public boolean isAmPm() {
        return isAmPm;
    }

    public void setDayFormatter(SimpleDateFormat simpleDateFormat) {
        if (simpleDateFormat != null) {
            this.daysPicker.setDayFormatter(simpleDateFormat);
        }
    }

    public Date getMinDate() {
        return (Date) minDate.clone();
    }

    public void setMinDate(Date minDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(dateHelper.getTimeZone());
        calendar.setTime(minDate);
        this.minDate = calendar.getTime();
        setMinYear();
    }

    public Date getMaxDate() {
        return (Date) maxDate.clone();
    }

    public void setMaxDate(Date maxDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(dateHelper.getTimeZone());
        calendar.setTime(maxDate);
        this.maxDate = calendar.getTime();
        setMinYear();
    }

    public void setCustomLocale(Locale locale) {
        for (WheelPicker p : pickers) {
            p.setCustomLocale(locale);
            p.updateAdapter();
        }
    }

    private void checkMinMaxDate(final WheelPicker picker) {
        checkBeforeMinDate(picker);
        checkAfterMaxDate(picker);
    }

    private void checkBeforeMinDate(final WheelPicker picker) {

        handler.postTask(new Runnable() {
            @Override
            public void run() {
                if (minDate != null && isBeforeMinDate(getDate())) {
                    for (WheelPicker p : pickers) {
                        int destPos = p.findIndexOfDate(minDate);
                        p.scrollTo(destPos);
                    }
                }
            }
        });

    }

    private void checkAfterMaxDate(final WheelPicker picker) {
        new EventHandler(EventRunner.getMainEventRunner()).postTask(new Runnable() {
            @Override
            public void run() {
                if (maxDate != null && isAfterMaxDate(getDate())) {
                    for (WheelPicker p : pickers) {
                        p.scrollTo(p.findIndexOfDate(maxDate));
                    }
                }
            }
        });
    }

    private boolean isBeforeMinDate(Date date) {
        boolean isBefore = dateHelper.getCalendarOfDate(date).before(dateHelper.getCalendarOfDate(minDate));
        return isBefore;
    }

    private boolean isAfterMaxDate(Date date) {
        boolean isAfter = dateHelper.getCalendarOfDate(date).after(dateHelper.getCalendarOfDate(maxDate));
        return isAfter;
    }

    public void addOnDateChangedListener(OnDateChangedListener listener) {
        this.listeners.add(listener);
    }

    public void removeOnDateChangedListener(OnDateChangedListener listener) {
        this.listeners.remove(listener);
    }

    public void checkPickersMinMax() {
        for (WheelPicker picker : pickers) {
            checkMinMaxDate(picker);
        }
    }

    public Date getDate() {
        int hour = hoursPicker.getCurrentHour();
        if (isAmPm && amPmPicker.isPm()) {
            hour += PM_HOUR_ADDITION;
        }
        final int minute = minutesPicker.getCurrentMinute();

        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(dateHelper.getTimeZone());
        if (displayDays) {
            final Date dayDate = daysPicker.getCurrentDate();
            calendar.setTime(dayDate);
        } else {
            if (displayMonth) {
                calendar.set(Calendar.MONTH, monthPicker.getCurrentMonth());
            }

            if (displayYears) {
                calendar.set(Calendar.YEAR, yearsPicker.getCurrentYear());
            }

            if (displayDaysOfMonth) {
                int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                if (daysOfMonthPicker.getCurrentDay() >= daysInMonth) {
                    calendar.set(Calendar.DAY_OF_MONTH, daysInMonth);
                } else {
                    calendar.set(Calendar.DAY_OF_MONTH, daysOfMonthPicker.getCurrentDay() + 1);
                }
            }
        }
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date date = calendar.getTime();
        return date;

    }

    public void setStepSizeMinutes(int minutesStep) {
        minutesPicker.setStepSizeMinutes(minutesStep);
    }

    public void setStepSizeHours(int hoursStep) {
        hoursPicker.setStepSizeHours(hoursStep);
    }

    public void setDefaultDate(Date date) {
        if (mSettingCalendar != null) {
            return;
        }
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeZone(dateHelper.getTimeZone());
            calendar.setTime(date);
            this.defaultDate = calendar.getTime();

            updateDaysOfMonth(calendar);

            for (WheelPicker picker : pickers) {
                picker.setDefaultDate(defaultDate);
            }
        }
    }

    public void selectDate(Calendar calendar) {
        mSettingCalendar = calendar;
        if (calendar == null) {
            return;
        }

        final Date date = calendar.getTime();
        for (WheelPicker picker : pickers) {
            picker.selectDate(date);
        }

        if (displayDaysOfMonth) {
            updateDaysOfMonth();
        }
    }

    private void updateListener() {
        final Date date = getDate();
        //TODO
//        final CharSequence format = isAmPm ? FORMAT_12_HOUR : FORMAT_24_HOUR;
        final String displayed = DateFormat.getInstance().format(date);

        for (OnDateChangedListener listener : listeners) {
            listener.onDateChanged(displayed, date);
        }
    }

    private void updateDaysOfMonth() {
        final Date date = getDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(dateHelper.getTimeZone());
        calendar.setTime(date);
        updateDaysOfMonth(calendar);
    }

    private void updateDaysOfMonth(Calendar calendar) {
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        daysOfMonthPicker.setDaysInMonth(daysInMonth);
        daysOfMonthPicker.updateAdapter();
    }

    public void setMustBeOnFuture(boolean mustBeOnFuture) {
        this.mustBeOnFuture = mustBeOnFuture;
        daysPicker.setShowOnlyFutureDate(mustBeOnFuture);
        if (mustBeOnFuture) {
            Calendar now = Calendar.getInstance();
            now.setTimeZone(dateHelper.getTimeZone());
            minDate = now.getTime(); //minDate is Today
        }
    }

    public boolean mustBeOnFuture() {
        return mustBeOnFuture;
    }

    private void setMinYear() {
        if (displayYears && this.minDate != null && this.maxDate != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeZone(dateHelper.getTimeZone());
            calendar.setTime(this.minDate);
            yearsPicker.setMinYear(calendar.get(Calendar.YEAR));
            calendar.setTime(this.maxDate);
            yearsPicker.setMaxYear(calendar.get(Calendar.YEAR));
        }
    }

    private void checkSettings() {
        if (displayDays && (displayDaysOfMonth || displayMonth)) {
            throw new IllegalArgumentException("You can either display days with months or days and months separately");
        }
    }

    private void init(Context context, AttrSet attrs) {
        setTodayText(new DateWithLabel(AttrUtil.getStringValue(attrs, "picker_todayText", ""), new Date()));
        setTextColor(AttrUtil.getColorIntValue(attrs, "picker_textColor", context.getColor(ResourceTable.Color_picker_default_selector_color)));
        setSelectedTextColor(AttrUtil.getColorIntValue(attrs, "picker_selectedTextColor", Color.getIntColor("#f4ea2a")));
        setSelectorColor(AttrUtil.getColorIntValue(attrs, "picker_selectorColor", context.getColor(ResourceTable.Color_picker_default_selector_color)));
        setItemSpacing(AttrUtil.getDimension(attrs, "picker_itemSpacing", AttrHelper.vp2px(8, getContext())));
        setCurvedMaxAngle(AttrUtil.getIntegerValue(attrs, "picker_curvedMaxAngle", WheelPicker.MAX_ANGLE));
        setSelectorHeight(AttrUtil.getDimension(attrs, "picker_selectorHeight", AttrHelper.vp2px(30, getContext())));
        setTextSize(AttrUtil.getDimension(attrs, "picker_textSize", 35));
        setCurved(AttrUtil.getBooleanValue(attrs, "picker_curved", IS_CURVED_DEFAULT));
        setCurved(AttrUtil.getBooleanValue(attrs, "picker_cyclic", IS_CYCLIC_DEFAULT));
        setCurved(AttrUtil.getBooleanValue(attrs, "picker_mustBeOnFuture", MUST_BE_ON_FUTURE_DEFAULT));
        setVisibleItemCount(AttrUtil.getIntegerValue(attrs, "picker_visibleItemCount", VISIBLE_ITEM_COUNT_DEFAULT));
        setCurvedMaxAngle(AttrUtil.getIntegerValue(attrs, "picker_stepSizeMinutes", 1));
        setCurvedMaxAngle(AttrUtil.getIntegerValue(attrs, "picker_stepSizeHours", 1));
        daysPicker.setDayCount(AttrUtil.getIntegerValue(attrs, "picker_dayCount", DAYS_PADDING));
        setDisplayDays(AttrUtil.getBooleanValue(attrs, "picker_displayDays", displayDays));
        setDisplayMinutes(AttrUtil.getBooleanValue(attrs, "picker_displayMinutes", displayMinutes));
        setDisplayHours(AttrUtil.getBooleanValue(attrs, "picker_displayHours", displayHours));
        setDisplayMonths(AttrUtil.getBooleanValue(attrs, "picker_displayMonth", displayMonth));
        setDisplayYears(AttrUtil.getBooleanValue(attrs, "picker_displayYears", displayYears));
        setDisplayDaysOfMonth(AttrUtil.getBooleanValue(attrs, "picker_displayDaysOfMonth", displayDaysOfMonth));
        setDisplayMonthNumbers(AttrUtil.getBooleanValue(attrs, "picker_displayMonthNumbers", monthPicker.displayMonthNumbers()));
        String monthFormat = AttrUtil.getStringValue(attrs, "picker_monthFormat", "");
        setMonthFormat(Utils.isEmptyStr(monthFormat) ? WheelMonthPicker.MONTH_FORMAT : monthFormat);
        setTextAlign(AttrUtil.getIntegerValue(attrs, "picker_textAlign", ALIGN_CENTER));
        setBindStateChangedListener(new BindStateChangedListener() {
            @Override
            public void onComponentBoundToWindow(Component component) {
                yearsPicker.setOnYearSelectedListener(new WheelYearPicker.OnYearSelectedListener() {
                    @Override
                    public void onYearSelected(WheelYearPicker picker, int position, int year) {
                        updateListener();
                        checkMinMaxDate(picker);

                        if (displayDaysOfMonth) {
                            updateDaysOfMonth();
                        }
                    }
                });

                monthPicker.setOnMonthSelectedListener(new WheelMonthPicker.MonthSelectedListener() {
                    @Override
                    public void onMonthSelected(WheelMonthPicker picker, int monthIndex, String monthName) {
                        updateListener();
                        checkMinMaxDate(picker);

                        if (displayDaysOfMonth) {
                            updateDaysOfMonth();
                        }
                    }
                });

                daysOfMonthPicker.setDayOfMonthSelectedListener(new WheelDayOfMonthPicker.DayOfMonthSelectedListener() {
                    @Override
                    public void onDayOfMonthSelected(WheelDayOfMonthPicker picker, int dayIndex) {
                        updateListener();
                        checkMinMaxDate(picker);
                    }
                });

                daysOfMonthPicker.setOnFinishedLoopListener(new WheelDayOfMonthPicker.FinishedLoopListener() {
                    @Override
                    public void onFinishedLoop(WheelDayOfMonthPicker picker) {
                        if (displayMonth) {
                            monthPicker.scrollTo(monthPicker.getCurrentItemPosition() + 1);
                            updateDaysOfMonth();
                        }
                    }
                });

                daysPicker.setOnDaySelectedListener(new WheelDayPicker.OnDaySelectedListener() {
                    @Override
                    public void onDaySelected(WheelDayPicker picker, int position, String name, Date date) {
                        updateListener();
                        checkMinMaxDate(picker);
                    }
                });

                minutesPicker.setOnMinuteChangedListener(new WheelMinutePicker.OnMinuteChangedListener() {
                    @Override
                    public void onMinuteChanged(WheelMinutePicker picker, int minutes) {
                        updateListener();
                        checkMinMaxDate(picker);
                    }
                })
                    .setOnFinishedLoopListener(new WheelMinutePicker.OnFinishedLoopListener() {
                        @Override
                        public void onFinishedLoop(WheelMinutePicker picker) {
                            hoursPicker.scrollTo(hoursPicker.getCurrentItemPosition() + 1);
                        }
                    });

                hoursPicker.setOnFinishedLoopListener(new WheelHourPicker.FinishedLoopListener() {
                    @Override
                    public void onFinishedLoop(WheelHourPicker picker) {
                        daysPicker.scrollTo(daysPicker.getCurrentItemPosition() + 1);
                    }
                })
                    .setHourChangedListener(new WheelHourPicker.OnHourChangedListener() {
                        @Override
                        public void onHourChanged(WheelHourPicker picker, int hour) {
                            updateListener();
                            checkMinMaxDate(picker);
                        }
                    });

                amPmPicker.setAmPmListener(new WheelAmPmPicker.AmPmListener() {
                    @Override
                    public void onAmPmChanged(WheelAmPmPicker picker, boolean isAm) {
                        updateListener();
                        checkMinMaxDate(picker);
                    }
                });

                setDefaultDate(defaultDate); //update displayed date
            }

            @Override
            public void onComponentUnboundFromWindow(Component component) {

            }
        });
        checkSettings();
        setMinYear();

        if (displayDaysOfMonth) {
            Calendar now = Calendar.getInstance();
            now.setTimeZone(dateHelper.getTimeZone());
            updateDaysOfMonth(now);
        }
        daysPicker.updateAdapter(); // For MustBeFuture and dayCount


    }

    public interface OnDateChangedListener {
        void onDateChanged(String displayed, Date date);
    }
}
