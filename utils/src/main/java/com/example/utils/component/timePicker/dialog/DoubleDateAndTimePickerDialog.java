package com.example.utils.component.timePicker.dialog;

import com.example.utils.ResourceTable;
import com.example.utils.component.timePicker.DateHelper;
import com.example.utils.component.timePicker.SingleDateAndTimePickerContainer;
import com.example.utils.component.timePicker.Utils;
import com.example.utils.component.timePicker.widget.DateWithLabel;
import com.example.utils.component.timePicker.widget.SingleDateAndTimeConstants;
import ohos.agp.animation.Animator;
import ohos.agp.animation.AnimatorValue;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.Text;
import ohos.agp.utils.Color;
import ohos.agp.utils.LayoutAlignment;
import ohos.app.Context;


import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public class DoubleDateAndTimePickerDialog extends BaseDialog {

    private Listener listener;
    private BottomSheetHelper bottomSheetHelper;
    private Button buttonTab0;
    private Button buttonTab1;
    private SingleDateAndTimePickerContainer pickerTab0;
    private SingleDateAndTimePickerContainer pickerTab1;
    private final DateHelper dateHelper = new DateHelper();
    private Component tab0;
    private Component tab1;
    private String tab0Text, tab1Text, title;
    private Integer titleTextSize;
    private Integer bottomSheetHeight;
    private String todayText;
    private String buttonOkText;
    private Date tab0Date;
    private Date tab1Date;
    private boolean secondDateAfterFirst;
    private boolean tab0Days, tab0Hours, tab0Minutes;
    private boolean tab1Days, tab1Hours, tab1Minutes;
    private AnimatorValue animatorValue;
    private Date mMinDate;

    private DoubleDateAndTimePickerDialog(Context context) {
        this(context, false);
    }

    private DisplayListener displayListener;

    private DoubleDateAndTimePickerDialog(Context context, boolean bottomSheet) {
        final int layout = bottomSheet ? ResourceTable.Layout_bottom_sheet_double_picker_bottom_sheet :
            ResourceTable.Layout_bottom_sheet_double_picker;
        this.bottomSheetHelper = new BottomSheetHelper(context, layout);
        bottomSheetHelper.setDialogWidthSize(ComponentContainer.LayoutConfig.MATCH_CONTENT);
        bottomSheetHelper.setLayoutAlignment(LayoutAlignment.CENTER);
        this.bottomSheetHelper.setListener(new BottomSheetHelper.Listener() {
            @Override
            public void onOpen() {

            }

            @Override
            public void onLoaded(Component view) {
                if (displayListener != null) {
                    displayListener.onDisplayed();
                }
                init(view);
            }

            @Override
            public void onClose() {
                if (displayListener != null) {
                    displayListener.onClosed();
                }
                DoubleDateAndTimePickerDialog.this.onClose();
            }
        });
    }

    private void init(Component view) {
        buttonTab0 = (Button) view.findComponentById(ResourceTable.Id_buttonTab0);
        buttonTab1 = (Button) view.findComponentById(ResourceTable.Id_buttonTab1);
        pickerTab0 = (SingleDateAndTimePickerContainer) view.findComponentById(ResourceTable.Id_picker_tab_3);
        pickerTab1 = (SingleDateAndTimePickerContainer) view.findComponentById(ResourceTable.Id_picker_tab_4);
        tab0 = view.findComponentById(ResourceTable.Id_tab0);
        tab1 = view.findComponentById(ResourceTable.Id_tab1);


        if (pickerTab0 != null) {
            if (bottomSheetHeight != null) {
                ComponentContainer.LayoutConfig params = pickerTab0.getLayoutConfig();
                params.height = bottomSheetHeight;
                pickerTab0.setLayoutConfig(params);
            }
            pickerTab0.setTodayText(new DateWithLabel(todayText, new Date()));
        }

        if (pickerTab1 != null) {
            if (bottomSheetHeight != null) {
                ComponentContainer.LayoutConfig params = pickerTab1.getLayoutConfig();
                params.height = bottomSheetHeight;
                pickerTab1.setLayoutConfig(params);
            }
            pickerTab1.setTodayText(new DateWithLabel(todayText, new Date()));
        }

        final Component titleLayout = view.findComponentById(ResourceTable.Id_sheetTitleLayout);
        final Text titleTextView = (Text) view.findComponentById(ResourceTable.Id_sheetTitle);
        if (title != null) {
            if (titleTextView != null) {
                titleTextView.setText(title);
                if (titleTextColor != null) {
                    titleTextView.setTextColor(new Color(titleTextColor));
                }
                if (titleTextSize != null) {
                    titleTextView.setTextSize(titleTextSize);
                }
            }
            if (mainColor != null && titleLayout != null) {
                titleLayout.setBackground(Utils.getShapeElementByColor(mainColor.getValue()));
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

        tab1.setBindStateChangedListener(new Component.BindStateChangedListener() {
            @Override
            public void onComponentBoundToWindow(Component component) {
                tab1.setTranslationX(tab1.getWidth());
            }

            @Override
            public void onComponentUnboundFromWindow(Component component) {

            }
        });


        buttonTab0.setSelected(true);
        buttonTab0.setTextColor(Color.WHITE);

        if (tab0Text != null) {
            buttonTab0.setText(tab0Text);
        }
        buttonTab0.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                displayTab0();
            }
        });

        if (tab1Text != null) {
            buttonTab1.setText(tab1Text);
        }
        buttonTab1.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                displayTab1();
            }
        });

        final Text buttonOk = (Text) view.findComponentById(ResourceTable.Id_buttonOk);
        if (buttonOk != null) {
            if (buttonOkText != null) {
                buttonOk.setText(buttonOkText);
            }

            if (mainColor != null) {
                buttonOk.setTextColor(mainColor);
            }

            if (titleTextSize != null) {
                buttonOk.setTextSize(titleTextSize);
            }
        }

        buttonOk.setClickedListener(new Component.ClickedListener() {

            @Override
            public void onClick(Component component) {
                if (isTab0Visible()) {
                    displayTab1();
                } else {
                    okClicked = true;
                    close();
                }
            }
        });

        if (curved) {
            pickerTab0.setCurved(true);
            pickerTab1.setCurved(true);
            pickerTab0.setVisibleItemCount(DEFAULT_ITEM_COUNT_MODE_CURVED);
            pickerTab1.setVisibleItemCount(DEFAULT_ITEM_COUNT_MODE_CURVED);
        } else {
            pickerTab0.setCurved(false);
            pickerTab1.setCurved(false);
            pickerTab0.setVisibleItemCount(DEFAULT_ITEM_COUNT_MODE_NORMAL);
            pickerTab1.setVisibleItemCount(DEFAULT_ITEM_COUNT_MODE_NORMAL);
        }
        pickerTab0.setDisplayDays(tab0Days);
        pickerTab0.setDisplayHours(tab0Hours);
        pickerTab0.setDisplayMinutes(tab0Minutes);
        pickerTab1.setDisplayDays(tab1Days);
        pickerTab1.setDisplayHours(tab1Hours);
        pickerTab1.setDisplayMinutes(tab1Minutes);

        pickerTab0.setMustBeOnFuture(mustBeOnFuture);
        pickerTab1.setMustBeOnFuture(mustBeOnFuture);

        pickerTab0.setStepSizeMinutes(minutesStep);
        pickerTab1.setStepSizeMinutes(minutesStep);

        if (mainColor != null) {
            pickerTab0.setSelectedTextColor(mainColor.getValue());
            pickerTab1.setSelectedTextColor(mainColor.getValue());
        }

//        if (minDate != null) {
//            pickerTab0.setMinDate(minDate);
//            pickerTab1.setMinDate(minDate);
//        }
//
//        if (maxDate != null) {
//            pickerTab0.setMaxDate(maxDate);
//            pickerTab1.setMaxDate(maxDate);
//        }
//
        if (defaultDate != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(defaultDate);
            pickerTab0.selectDate(calendar);
            pickerTab1.selectDate(calendar);
        }
        if (tab0Date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(tab0Date);
            pickerTab0.selectDate(calendar);
        }
        if (tab1Date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(tab1Date);
            pickerTab1.selectDate(calendar);
        }

        if (dayFormatter != null) {
            pickerTab0.setDayFormatter(dayFormatter);
            pickerTab1.setDayFormatter(dayFormatter);
        }

        if (customLocale != null) {
            pickerTab0.setCustomLocale(customLocale);
            pickerTab1.setCustomLocale(customLocale);
        }

        if (secondDateAfterFirst) {
            pickerTab0.addOnDateChangedListener(new SingleDateAndTimePickerContainer.OnDateChangedListener() {
                @Override
                public void onDateChanged(String displayed, Date date) {
                    mMinDate = date;
                    if (!isTab0Visible()) {
                        Date minData = pickerTab1.getMinDate();
                        if (mMinDate != null && minData != null) {
                            if (!mMinDate.toString().equals(minData.toString())) {
                                pickerTab1.setMinDate(mMinDate);
                                pickerTab1.checkPickersMinMax();
                            }
                        }
                    }
                }
            });
        }
        animatorValue = new AnimatorValue();
        animatorValue.setDuration(200L);
        animatorValue.setCurveType(Animator.CurveType.SMOOTH_STEP);
    }


    private void setDisplayListener(DisplayListener displayListener) {
        this.displayListener = displayListener;
    }


    public DoubleDateAndTimePickerDialog setTab0Text(String tab0Text) {
        this.tab0Text = tab0Text;
        return this;
    }

    public DoubleDateAndTimePickerDialog setTab1Text(String tab1Text) {
        this.tab1Text = tab1Text;
        return this;
    }

    public DoubleDateAndTimePickerDialog setButtonOkText(String buttonOkText) {
        this.buttonOkText = buttonOkText;
        return this;
    }

    public DoubleDateAndTimePickerDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public DoubleDateAndTimePickerDialog setTitleTextSize(Integer titleTextSize) {
        this.titleTextSize = titleTextSize;
        return this;
    }

    public DoubleDateAndTimePickerDialog setBottomSheetHeight(Integer bottomSheetHeight) {
        this.bottomSheetHeight = bottomSheetHeight;
        return this;
    }

    public DoubleDateAndTimePickerDialog setTodayText(String todayText) {
        this.todayText = todayText;
        return this;
    }

    public DoubleDateAndTimePickerDialog setListener(Listener listener) {
        this.listener = listener;
        return this;
    }

    public DoubleDateAndTimePickerDialog setCurved(boolean curved) {
        this.curved = curved;
        return this;
    }

    public DoubleDateAndTimePickerDialog setMinutesStep(int minutesStep) {
        this.minutesStep = minutesStep;
        return this;
    }

    public DoubleDateAndTimePickerDialog setMustBeOnFuture(boolean mustBeOnFuture) {
        this.mustBeOnFuture = mustBeOnFuture;
        return this;
    }

    public DoubleDateAndTimePickerDialog setMinDateRange(Date minDate) {
        if(minDate != null){
            this.minDate = (Date) minDate.clone();
        }else {
            this.minDate = null;
        }
        return this;
    }

    public DoubleDateAndTimePickerDialog setMaxDateRange(Date maxDate) {
        if(maxDate != null){
            this.maxDate = (Date) maxDate.clone();
        }else {
            this.maxDate = null;
        }
        return this;
    }

    public DoubleDateAndTimePickerDialog setDefaultDate(Date defaultDate) {
        if(defaultDate != null){
            this.defaultDate = (Date) defaultDate.clone();
        }else {
            this.defaultDate = null;
        }
        return this;
    }

    public DoubleDateAndTimePickerDialog setDayFormatter(SimpleDateFormat dayFormatter) {
        this.dayFormatter = dayFormatter;
        return this;
    }

    public DoubleDateAndTimePickerDialog setCustomLocale(Locale locale) {
        this.customLocale = locale;
        return this;
    }

    public DoubleDateAndTimePickerDialog setTab0Date(Date tab0Date) {
        if(tab0Date != null){
            this.tab0Date = (Date) tab0Date.clone();
        }else {
            this.tab0Date = null;
        }
        return this;
    }

    public DoubleDateAndTimePickerDialog setTab1Date(Date tab1Date) {
        if(tab1Date != null){
            this.tab1Date = (Date) tab1Date.clone();
        }else {
            this.tab1Date = null;
        }
        return this;
    }

    public DoubleDateAndTimePickerDialog setSecondDateAfterFirst(boolean secondDateAfterFirst) {
        this.secondDateAfterFirst = secondDateAfterFirst;
        return this;
    }

    public DoubleDateAndTimePickerDialog setTab0DisplayDays(boolean tab0Days) {
        this.tab0Days = tab0Days;
        return this;
    }

    public DoubleDateAndTimePickerDialog setTab0DisplayHours(boolean tab0Hours) {
        this.tab0Hours = tab0Hours;
        return this;
    }

    public DoubleDateAndTimePickerDialog setTab0DisplayMinutes(boolean tab0Minutes) {
        this.tab0Minutes = tab0Minutes;
        return this;
    }

    public DoubleDateAndTimePickerDialog setTab1DisplayDays(boolean tab1Days) {
        this.tab1Days = tab1Days;
        return this;
    }

    public DoubleDateAndTimePickerDialog setTab1DisplayHours(boolean tab1Hours) {
        this.tab1Hours = tab1Hours;
        return this;
    }

    public DoubleDateAndTimePickerDialog setTab1DisplayMinutes(boolean tab1Minutes) {
        this.tab1Minutes = tab1Minutes;
        return this;
    }

    public DoubleDateAndTimePickerDialog setFocusable(boolean focusable) {
        bottomSheetHelper.setFocusable(focusable);
        return this;
    }

    private DoubleDateAndTimePickerDialog setTimeZone(TimeZone timeZone) {
        dateHelper.setTimeZone(timeZone);
//        pickerTab0.setTimeZone(timeZone);
//        pickerTab1.setTimeZone(timeZone);
        return this;
    }

    @Override
    public void display() {
        super.display();
        this.bottomSheetHelper.display();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        bottomSheetHelper.dismiss();
    }

    @Override
    public void close() {
        super.close();
        bottomSheetHelper.hide();
    }

    protected void onClose() {
        super.onClose();
        if (listener != null && okClicked) {
            listener.onDateSelected(Arrays.asList(pickerTab0.getDate(), pickerTab1.getDate()));
        }
    }

    private void displayTab0() {
        if (!isTab0Visible()) {
            if (animatorValue.isRunning()) {
                return;
            }
            buttonTab0.setSelected(true);
            buttonTab0.setTextColor(Color.WHITE);
            buttonTab1.setSelected(false);
            buttonTab1.setTextColor(new Color(Color.getIntColor("#BDBDBD")));

            tab0.setTranslationX(-tab1.getWidth());
            //停止tab1滚动
            animatorValue.setValueUpdateListener(new AnimatorValue.ValueUpdateListener() {
                @Override
                public void onUpdate(AnimatorValue animatorValue, float v) {

                    tab0.setTranslationX(-tab0.getWidth() + tab0.getWidth() * v);
                    tab1.setTranslationX(tab0.getWidth() * v);
                }
            });
            animatorValue.start();

        }
    }

    private void displayTab1() {
        if (isTab0Visible()) {
            if (animatorValue.isRunning()) {
                return;
            }
            buttonTab0.setSelected(false);
            buttonTab0.setTextColor(new Color(Color.getIntColor("#BDBDBD")));
            buttonTab1.setSelected(true);
            buttonTab1.setTextColor(Color.WHITE);

            //停止tab0滚动
            tab1.setTranslationX(tab1.getWidth());

            animatorValue.setValueUpdateListener(new AnimatorValue.ValueUpdateListener() {
                @Override
                public void onUpdate(AnimatorValue animatorValue, float v) {
                    tab0.setTranslationX(-tab0.getWidth() * v);
                    tab1.setTranslationX(tab0.getWidth() + (-tab0.getWidth()) * v);

                }
            });
            animatorValue.setStateChangedListener(new Animator.StateChangedListener() {
                @Override
                public void onStart(Animator animator) {

                }

                @Override
                public void onStop(Animator animator) {

                }

                @Override
                public void onCancel(Animator animator) {

                }

                @Override
                public void onEnd(Animator animator) {
                    Date minData = pickerTab1.getMinDate();
                    if (mMinDate != null && minData != null) {
                        if (!mMinDate.toString().equals(minData.toString())) {
                            pickerTab1.setMinDate(mMinDate);
                            pickerTab1.checkPickersMinMax();
                        }
                    }

                }

                @Override
                public void onPause(Animator animator) {

                }

                @Override
                public void onResume(Animator animator) {


                }
            });

            animatorValue.start();

        }
    }

    private boolean isTab0Visible() {
        return tab0.getTranslationX() == 0;
    }

    public interface Listener {
        void onDateSelected(List<Date> dates);
    }

    public interface DisplayListener {
        void onDisplayed();

        void onClosed();
    }

    public static class Builder {

        private final Context context;

        private Listener listener;
        private boolean bottomSheet;
        private DoubleDateAndTimePickerDialog dialog;


        private String tab0Text;

        private String tab1Text;

        private String title;

        private Integer titleTextSize;

        private Integer bottomSheetHeight;

        private String buttonOkText;

        private String todayText;

        private boolean curved;
        private boolean secondDateAfterFirst;
        private boolean mustBeOnFuture;
        private int minutesStep = SingleDateAndTimeConstants.STEP_MINUTES_DEFAULT;

        private SimpleDateFormat dayFormatter;

        private Locale customLocale;


        private Integer backgroundColor = null;


        private Integer mainColor = null;


        private Integer titleTextColor = null;

        private DisplayListener displayListener;

        private Date minDate;

        private Date maxDate;

        private Date defaultDate;

        private Date tab0Date;

        private Date tab1Date;

        private boolean tab0Days = true;
        private boolean tab0Hours = true;
        private boolean tab0Minutes = true;
        private boolean tab1Days = true;
        private boolean tab1Hours = true;
        private boolean tab1Minutes = true;
        private boolean focusable = false;
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

        public Builder displayListener(DisplayListener displayListener) {
            this.displayListener = displayListener;
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

        public Builder dayFormatter(SimpleDateFormat dayFormatter) {
            this.dayFormatter = dayFormatter;
            return this;
        }

        public Builder customLocale(Locale locale) {
            this.customLocale = locale;
            return this;
        }

        public Builder minutesStep(int minutesStep) {
            this.minutesStep = minutesStep;
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

        public Builder defaultDate(Date defaultDate) {
            if(defaultDate != null){
                this.defaultDate = (Date) defaultDate.clone();
            }else {
                this.defaultDate = null;
            }
            return this;
        }

        public Builder tab0Date(Date tab0Date) {
            if(tab0Date != null){
                this.tab0Date = (Date) tab0Date.clone();
            }else {
                this.tab0Date = null;
            }
            return this;
        }

        public Builder tab1Date(Date tab1Date) {
            if(tab1Date != null){
                this.tab1Date = (Date) tab1Date.clone();
            }else {
                this.tab1Date = null;
            }
            return this;
        }

        public Builder listener(
            Listener listener) {
            this.listener = listener;
            return this;
        }

        public Builder tab1Text(String tab1Text) {
            this.tab1Text = tab1Text;
            return this;
        }

        public Builder tab0Text(String tab0Text) {
            this.tab0Text = tab0Text;
            return this;
        }

        public Builder buttonOkText(String buttonOkText) {
            this.buttonOkText = buttonOkText;
            return this;
        }

        public Builder secondDateAfterFirst(boolean secondDateAfterFirst) {
            this.secondDateAfterFirst = secondDateAfterFirst;
            return this;
        }

        public Builder setTab0DisplayDays(boolean tab0Days) {
            this.tab0Days = tab0Days;
            return this;
        }

        public Builder setTab0DisplayHours(boolean tab0Hours) {
            this.tab0Hours = tab0Hours;
            return this;
        }

        public Builder setTab0DisplayMinutes(boolean tab0Minutes) {
            this.tab0Minutes = tab0Minutes;
            return this;
        }

        public Builder setTab1DisplayDays(boolean tab1Days) {
            this.tab1Days = tab1Days;
            return this;
        }

        public Builder setTab1DisplayHours(boolean tab1Hours) {
            this.tab1Hours = tab1Hours;
            return this;
        }

        public Builder setTab1DisplayMinutes(boolean tab1Minutes) {
            this.tab1Minutes = tab1Minutes;
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

        public DoubleDateAndTimePickerDialog build() {
            final DoubleDateAndTimePickerDialog dialog = new DoubleDateAndTimePickerDialog(context, bottomSheet)
                .setTitle(title)
                .setTitleTextSize(titleTextSize)
                .setBottomSheetHeight(bottomSheetHeight)
                .setTodayText(todayText)
                .setListener(listener)
                .setCurved(curved)
                .setButtonOkText(buttonOkText)
                .setTab0Text(tab0Text)
                .setTab1Text(tab1Text)
                .setMinutesStep(minutesStep)
                .setMaxDateRange(maxDate)
                .setMinDateRange(minDate)
                .setDefaultDate(defaultDate)
                .setTab0DisplayDays(tab0Days)
                .setTab0DisplayHours(tab0Hours)
                .setTab0DisplayMinutes(tab0Minutes)
                .setTab1DisplayDays(tab1Days)
                .setTab1DisplayHours(tab1Hours)
                .setTab1DisplayMinutes(tab1Minutes)
                .setTab0Date(tab0Date)
                .setTab1Date(tab1Date)
                .setDayFormatter(dayFormatter)
                .setCustomLocale(customLocale)
                .setMustBeOnFuture(mustBeOnFuture)
                .setSecondDateAfterFirst(secondDateAfterFirst)
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

        public boolean isDisplaying() {
            if (dialog != null) {
                return dialog.isDisplaying();
            }
            return false;
        }

        public void dismiss() {
            if (dialog != null) {
                dialog.dismiss();
            }
        }
    }
}
