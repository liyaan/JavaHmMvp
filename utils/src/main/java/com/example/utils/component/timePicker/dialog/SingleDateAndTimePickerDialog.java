package com.example.utils.component.timePicker.dialog;


import com.example.utils.ResourceTable;
import com.example.utils.component.timePicker.DateHelper;
import com.example.utils.component.timePicker.SingleDateAndTimePickerContainer;
import com.example.utils.component.timePicker.Utils;
import com.example.utils.component.timePicker.widget.DateWithLabel;
import com.example.utils.component.timePicker.widget.SingleDateAndTimeConstants;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.Text;
import ohos.agp.utils.Color;
import ohos.app.Context;



import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class SingleDateAndTimePickerDialog extends BaseDialog {
    private final DateHelper dateHelper = new DateHelper();
    private Listener listener;
    private BottomSheetHelper bottomSheetHelper;
    private SingleDateAndTimePickerContainer picker;

    private String title;
    private Integer titleTextSize;
    private Integer bottomSheetHeight;
    private String todayText;
    private DisplayListener displayListener;

    private SingleDateAndTimePickerDialog(Context context) {
        this(context, false);
    }

    private SingleDateAndTimePickerDialog(Context context, boolean bottomSheet) {
        final int layout = bottomSheet ? ResourceTable.Layout_bottom_sheet_picker_bottom_sheet :
            ResourceTable.Layout_bottom_sheet_picker;
        this.bottomSheetHelper = new BottomSheetHelper(context, layout);

        this.bottomSheetHelper.setListener(new BottomSheetHelper.Listener() {
            @Override
            public void onOpen() {
            }


            @Override
            public void onLoaded(Component view) {
                init(view);
                if (displayListener != null) {
                    displayListener.onDisplayed(picker);
                }
            }

            @Override
            public void onClose() {
                SingleDateAndTimePickerDialog.this.onClose();

                if (displayListener != null) {
                    displayListener.onClosed(picker);
                }
            }
        });
    }


    private void init(Component view) {
        picker = (SingleDateAndTimePickerContainer) view.findComponentById(ResourceTable.Id_picker);
        if (picker != null) {
            picker.setDateHelper(dateHelper);
            if (bottomSheetHeight != null) {
                ComponentContainer.LayoutConfig params = picker.getLayoutConfig();
                params.height = bottomSheetHeight;
                picker.setLayoutConfig(params);
            }
        }

        final Text buttonOk = (Text) view.findComponentById(ResourceTable.Id_buttonOk);
        if (buttonOk != null) {
            buttonOk.setClickedListener(new Component.ClickedListener() {
                @Override
                public void onClick(Component component) {
                    okClicked = true;
                    close();
                }
            });

            if (mainColor != null) {
                buttonOk.setTextColor(mainColor);
            }

            if (titleTextSize != null) {
                buttonOk.setTextSize(titleTextSize);
            }
        }

        final Component sheetContentLayout = view.findComponentById(ResourceTable.Id_sheetContentLayout);
        if (sheetContentLayout != null) {
            sheetContentLayout.setClickedListener(new Component.ClickedListener() {
                @Override
                public void onClick(Component component) {

                }
            });

            if (backgroundColor != null) {
                sheetContentLayout.setBackground(Utils.getShapeElementByColor(backgroundColor.getValue()));
            }
        }

        final Text titleTextView = (Text) view.findComponentById(ResourceTable.Id_sheetTitle);
        if (titleTextView != null) {
            titleTextView.setText(title);

            if (titleTextColor != null) {
                titleTextView.setTextColor(new Color(titleTextColor));
            }

            if (titleTextSize != null) {
                titleTextView.setTextSize(titleTextSize);
            }
        }

        picker.setTodayText(new DateWithLabel(todayText, new Date()));

        final Component pickerTitleHeader = view.findComponentById(ResourceTable.Id_mDirectionalLayout);
        if (mainColor != null && pickerTitleHeader != null) {
            pickerTitleHeader.setBackground(Utils.getShapeElementByColor(mainColor.getValue()));
        }

        if (curved) {
            picker.setCurved(true);
            picker.setVisibleItemCount(7);
        } else {
            picker.setCurved(false);
            picker.setVisibleItemCount(5);
        }
        picker.setMustBeOnFuture(mustBeOnFuture);

        picker.setStepSizeMinutes(minutesStep);

        if (dayFormatter != null) {
            picker.setDayFormatter(dayFormatter);
        }

        if (customLocale != null) {
            picker.setCustomLocale(customLocale);
        }

        if (mainColor != null) {
            picker.setSelectedTextColor(mainColor.getValue());
        }

        // displayYears used in setMinDate / setMaxDate
        picker.setDisplayYears(displayYears);

        if (minDate != null) {
            picker.setMinDate(minDate);
        }

        if (maxDate != null) {
            picker.setMaxDate(maxDate);
        }

        if (defaultDate != null) {
            picker.setDefaultDate(defaultDate);
        }

        if (isAmPm != null) {
            picker.setIsAmPm(isAmPm);
        }

        picker.setDisplayDays(displayDays);
        picker.setDisplayMonths(displayMonth);
        picker.setDisplayDaysOfMonth(displayDaysOfMonth);
        picker.setDisplayMinutes(displayMinutes);
        picker.setDisplayHours(displayHours);
    }

    public SingleDateAndTimePickerDialog setListener(Listener listener) {
        this.listener = listener;
        return this;
    }

    public SingleDateAndTimePickerDialog setCurved(boolean curved) {
        this.curved = curved;
        return this;
    }

    public SingleDateAndTimePickerDialog setMinutesStep(int minutesStep) {
        this.minutesStep = minutesStep;
        return this;
    }

    private void setDisplayListener(DisplayListener displayListener) {
        this.displayListener = displayListener;
    }

    public SingleDateAndTimePickerDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public SingleDateAndTimePickerDialog setTitleTextSize(Integer titleTextSize) {
        this.titleTextSize = titleTextSize;
        return this;
    }

    public SingleDateAndTimePickerDialog setBottomSheetHeight(Integer bottomSheetHeight) {
        this.bottomSheetHeight = bottomSheetHeight;
        return this;
    }

    public SingleDateAndTimePickerDialog setTodayText(String todayText) {
        this.todayText = todayText;
        return this;
    }

    public SingleDateAndTimePickerDialog setMustBeOnFuture(boolean mustBeOnFuture) {
        this.mustBeOnFuture = mustBeOnFuture;
        return this;
    }

    public SingleDateAndTimePickerDialog setMinDateRange(Date minDate) {
        if(minDate != null){
            this.minDate = (Date) minDate.clone();
        }else {
            this.minDate = null;
        }
        return this;
    }

    public SingleDateAndTimePickerDialog setMaxDateRange(Date maxDate) {
        if(maxDate != null){
            this.maxDate = (Date) maxDate.clone();
        }else {
            this.maxDate = null;
        }
        return this;
    }

    public SingleDateAndTimePickerDialog setDefaultDate(Date defaultDate) {
        if(defaultDate != null){
            this.defaultDate = (Date) defaultDate.clone();
        }else {
            this.defaultDate = null;
        }
        return this;
    }

    public SingleDateAndTimePickerDialog setDisplayDays(boolean displayDays) {
        this.displayDays = displayDays;
        return this;
    }

    public SingleDateAndTimePickerDialog setDisplayMinutes(boolean displayMinutes) {
        this.displayMinutes = displayMinutes;
        return this;
    }

    public SingleDateAndTimePickerDialog setDisplayMonthNumbers(boolean displayMonthNumbers) {
        this.displayMonthNumbers = displayMonthNumbers;
        return this;
    }

    public SingleDateAndTimePickerDialog setDisplayHours(boolean displayHours) {
        this.displayHours = displayHours;
        return this;
    }

    public SingleDateAndTimePickerDialog setDisplayDaysOfMonth(boolean displayDaysOfMonth) {
        this.displayDaysOfMonth = displayDaysOfMonth;
        return this;
    }


    private SingleDateAndTimePickerDialog setDisplayMonth(boolean displayMonth) {
        this.displayMonth = displayMonth;
        return this;
    }

    private SingleDateAndTimePickerDialog setDisplayYears(boolean displayYears) {
        this.displayYears = displayYears;
        return this;
    }

    public SingleDateAndTimePickerDialog setDayFormatter(SimpleDateFormat dayFormatter) {
        this.dayFormatter = dayFormatter;
        return this;
    }

    public SingleDateAndTimePickerDialog setCustomLocale(Locale locale) {
        this.customLocale = locale;
        return this;
    }

    public SingleDateAndTimePickerDialog setIsAmPm(boolean isAmPm) {
        this.isAmPm = Boolean.valueOf(isAmPm);
        return this;
    }

    public SingleDateAndTimePickerDialog setFocusable(boolean focusable) {
        bottomSheetHelper.setFocusable(focusable);
        return this;
    }

    private SingleDateAndTimePickerDialog setTimeZone(TimeZone timeZone) {
        dateHelper.setTimeZone(timeZone);
        return this;
    }

    @Override
    public void display() {
        super.display();
        bottomSheetHelper.display();
    }

    @Override
    public void close() {
        super.close();
        bottomSheetHelper.hide();

        if (listener != null && okClicked) {
            listener.onDateSelected(picker.getDate());
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        bottomSheetHelper.dismiss();
    }

    public interface Listener {
        void onDateSelected(Date date);
    }

    public interface DisplayListener {
        void onDisplayed(SingleDateAndTimePickerContainer picker);

        void onClosed(SingleDateAndTimePickerContainer picker);
    }

    public static class Builder {
        private final Context context;
        private SingleDateAndTimePickerDialog dialog;


        private Listener listener;

        private DisplayListener displayListener;


        private String title;


        private Integer titleTextSize;


        private Integer bottomSheetHeight;


        private String todayText;

        private boolean bottomSheet;

        private boolean curved;
        private boolean mustBeOnFuture;
        private int minutesStep = SingleDateAndTimeConstants.STEP_MINUTES_DEFAULT;

        private boolean displayDays = true;
        private boolean displayMinutes = true;
        private boolean displayHours = true;
        private boolean displayMonth = false;
        private boolean displayDaysOfMonth = false;
        private boolean displayYears = false;
        private boolean displayMonthNumbers = false;
        private boolean focusable = false;


        private Boolean isAmPm;


        private Integer backgroundColor = null;


        private Integer mainColor = null;


        private Integer titleTextColor = null;


        private Date minDate;

        private Date maxDate;

        private Date defaultDate;


        private SimpleDateFormat dayFormatter;


        private Locale customLocale;
        private TimeZone timeZone;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder titleTextSize(Integer titleTextSize) {
            this.titleTextSize = titleTextSize;
            return this;
        }

        public Builder bottomSheetHeight(Integer bottomSheetHeight) {
            this.bottomSheetHeight = bottomSheetHeight;
            return this;
        }

        public Builder todayText(String todayText) {
            this.todayText = todayText;
            return this;
        }

        public Builder bottomSheet() {
            this.bottomSheet = true;
            return this;
        }

        public Builder curved() {
            this.curved = true;
            return this;
        }

        public Builder mustBeOnFuture() {
            this.mustBeOnFuture = true;
            return this;
        }

        public Builder minutesStep(int minutesStep) {
            this.minutesStep = minutesStep;
            return this;
        }

        public Builder displayDays(boolean displayDays) {
            this.displayDays = displayDays;
            return this;
        }

        public Builder displayAmPm(boolean isAmPm) {
            this.isAmPm = isAmPm;
            return this;
        }

        public Builder displayMinutes(boolean displayMinutes) {
            this.displayMinutes = displayMinutes;
            return this;
        }

        public Builder displayHours(boolean displayHours) {
            this.displayHours = displayHours;
            return this;
        }

        public Builder displayDaysOfMonth(boolean displayDaysOfMonth) {
            this.displayDaysOfMonth = displayDaysOfMonth;
            return this;
        }

        public Builder displayMonth(boolean displayMonth) {
            this.displayMonth = displayMonth;
            return this;
        }

        public Builder displayYears(boolean displayYears) {
            this.displayYears = displayYears;
            return this;
        }

        public Builder listener(Listener listener) {
            this.listener = listener;
            return this;
        }

        public Builder displayListener(DisplayListener displayListener) {
            this.displayListener = displayListener;
            return this;
        }

        public Builder titleTextColor(int titleTextColor) {
            this.titleTextColor = titleTextColor;
            return this;
        }

        public Builder backgroundColor(int backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public Builder mainColor(int mainColor) {
            this.mainColor = mainColor;
            return this;
        }

        public Builder minDateRange(Date minDate) {
            if(minDate != null){
                this.minDate = (Date) minDate.clone();
            }else {
                this.minDate = null;
            }
            return this;
        }

        public Builder maxDateRange(Date maxDate) {
            if(maxDate != null){
                this.maxDate = (Date) maxDate.clone();
            }else {
                this.maxDate = null;
            }
            return this;
        }

        public Builder displayMonthNumbers(boolean displayMonthNumbers) {
            this.displayMonthNumbers = displayMonthNumbers;
            return this;
        }

        public Builder defaultDate(Date defaultDate) {
            if(defaultDate != null){
                this.defaultDate = (Date) defaultDate.clone();
            }else {
                this.defaultDate = null;
            }
            return this;
        }

        public Builder setDayFormatter(SimpleDateFormat dayFormatter) {
            this.dayFormatter = dayFormatter;
            return this;
        }

        public Builder customLocale(Locale locale) {
            this.customLocale = locale;
            return this;
        }

        public Builder setTimeZone(TimeZone timeZone) {
            this.timeZone = timeZone;
            return this;
        }

        public Builder focusable() {
            this.focusable = true;
            return this;
        }

        public SingleDateAndTimePickerDialog build() {
            final SingleDateAndTimePickerDialog dialog = new SingleDateAndTimePickerDialog(context, bottomSheet)
                .setTitle(title)
                .setTitleTextSize(titleTextSize)
                .setBottomSheetHeight(bottomSheetHeight)
                .setTodayText(todayText)
                .setListener(listener)
                .setCurved(curved)
                .setMinutesStep(minutesStep)
                .setMaxDateRange(maxDate)
                .setMinDateRange(minDate)
                .setDefaultDate(defaultDate)
                .setDisplayHours(displayHours)
                .setDisplayMonth(displayMonth)
                .setDisplayYears(displayYears)
                .setDisplayDaysOfMonth(displayDaysOfMonth)
                .setDisplayMinutes(displayMinutes)
                .setDisplayMonthNumbers(displayMonthNumbers)
                .setDisplayDays(displayDays)
                .setDayFormatter(dayFormatter)
                .setCustomLocale(customLocale)
                .setMustBeOnFuture(mustBeOnFuture)
                .setTimeZone(timeZone)
                .setFocusable(focusable);

            if (mainColor != null) {
                dialog.setMainColor(new Color(mainColor));
            }

            if (backgroundColor != null) {
                dialog.setBackgroundColor(new Color(backgroundColor));
            }

            if (titleTextColor != null) {
                dialog.setTitleTextColor(titleTextColor);
            }

            if (displayListener != null) {
                dialog.setDisplayListener(displayListener);
            }

            if (isAmPm != null) {
                dialog.setIsAmPm(isAmPm);
            }

            return dialog;
        }

        public void display() {
            dialog = build();
            dialog.display();
        }

        public void close() {
            if (dialog != null) {
                dialog.close();
            }
        }


        public void dismiss() {
            if (dialog != null) {
                dialog.dismiss();
            }
        }

        public boolean isDisplaying() {
            if (dialog != null) {
                return dialog.isDisplaying();
            }
            return false;
        }

    }

}
