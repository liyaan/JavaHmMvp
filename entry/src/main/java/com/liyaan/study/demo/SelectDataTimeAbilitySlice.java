package com.liyaan.study.demo;


import com.example.utils.component.timePicker.SingleDateAndTimePickerContainer;
import com.example.utils.component.timePicker.dialog.DoubleDateAndTimePickerDialog;
import com.example.utils.component.timePicker.dialog.SingleDateAndTimePickerDialog;
import com.liyaan.study.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.Text;
import ohos.agp.window.service.WindowManager;
import ohos.bundle.AbilityInfo;




import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class SelectDataTimeAbilitySlice extends AbilitySlice implements Component.ClickedListener {
    private SingleDateAndTimePickerDialog.Builder singleBuilder;
    private DirectionalLayout mSingleTextLayout;
    private DirectionalLayout mDoubleTextLayout;
    private DirectionalLayout msingleTimeLayout;
    private DirectionalLayout mSingleDateLayout;
    private DirectionalLayout mSingleDateLocaleLayout;
    SimpleDateFormat simpleDateFormat;
    SimpleDateFormat simpleDateOnlyFormat;
    SimpleDateFormat simpleTimeFormat;
    SimpleDateFormat simpleDateLocaleFormat;
    private Text mSingleText;
    private Text mDoubleText;
    DoubleDateAndTimePickerDialog.Builder doubleBuilder;
    private Text mSingleTimeText;
    private Text mSingleDateText;
    private Text mSingleDateLocaleText;
    private long mEndClickTime;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);

        super.setUIContent(ResourceTable.Layout_single_date_picker_activity_main_double_picker);
        try {
            int color = getResourceManager().getElement(ResourceTable.Color_picker_background_dark).getColor();
            WindowManager.getInstance().getTopWindow().get().setStatusBarColor(color);
        } catch (Exception e) {
            e.printStackTrace();
        }
        initData();
        initView();
    }


    private void initData() {
        this.simpleDateFormat = new SimpleDateFormat("EEE d MMM HH:mm", Locale.getDefault());
        this.simpleDateOnlyFormat = new SimpleDateFormat("EEE d MMM", Locale.getDefault());
        this.simpleTimeFormat = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
        this.simpleDateLocaleFormat = new SimpleDateFormat("EEE d MMM", Locale.GERMAN);
    }

    private void initView() {
        mSingleTextLayout = (DirectionalLayout) findComponentById(ResourceTable.Id_singleLayout);
        mDoubleTextLayout = (DirectionalLayout) findComponentById(ResourceTable.Id_doubleLayout);
        msingleTimeLayout = (DirectionalLayout) findComponentById(ResourceTable.Id_singleTimeLayout);
        mSingleDateLayout = (DirectionalLayout) findComponentById(ResourceTable.Id_singleDateLayout);
        mSingleDateLocaleLayout = (DirectionalLayout) findComponentById(ResourceTable.Id_singleDateLocaleLayout);

        mSingleText = (Text) findComponentById(ResourceTable.Id_singleText);
        mDoubleText = (Text) findComponentById(ResourceTable.Id_doubleText);
        mSingleTimeText = (Text) findComponentById(ResourceTable.Id_singleTimeText);
        mSingleDateText = (Text) findComponentById(ResourceTable.Id_singleDateText);
        mSingleDateLocaleText = (Text) findComponentById(ResourceTable.Id_singleDateLocaleText);

        mSingleTextLayout.setClickedListener(this);
        mDoubleTextLayout.setClickedListener(this);
        msingleTimeLayout.setClickedListener(this);
        mSingleDateLayout.setClickedListener(this);
        mSingleDateLocaleLayout.setClickedListener(this);
    }

    @Override
    protected void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    protected void onBackground() {
        super.onBackground();
        if (singleBuilder != null) {
            singleBuilder.close();
        }
        if (doubleBuilder != null) {
            doubleBuilder.close();
        }
    }

    @Override
    protected void onOrientationChanged(AbilityInfo.DisplayOrientation displayOrientation) {
        super.onOrientationChanged(displayOrientation);

    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    @Override
    public void onClick(Component component) {
        long CurClickTime = System.currentTimeMillis();

        if (Math.abs(CurClickTime - mEndClickTime) < 200) {
            return;
        }

        switch (component.getId()) {

            case ResourceTable.Id_singleLayout:

                showSigleTextDia();

                break;
            case ResourceTable.Id_doubleLayout:
                showDoubleDia();
                break;
            case ResourceTable.Id_singleTimeLayout:
                simpleTimeClicked();
                break;
            case ResourceTable.Id_singleDateLayout:
                showSimpleDateDia();
                break;
            case ResourceTable.Id_singleDateLocaleLayout:
                singleDateLocaleClicked();

                break;
        }
        mEndClickTime = System.currentTimeMillis();
    }

    public void singleDateLocaleClicked() {

        singleBuilder = new SingleDateAndTimePickerDialog.Builder(this)
                .customLocale(Locale.GERMAN)
                .bottomSheet()
                .curved()
                .displayHours(false)
                .displayMinutes(false)
                .displayDays(true)

                .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {
                    @Override
                    public void onDisplayed(SingleDateAndTimePickerContainer picker) {
//                        Log.d(TAG, "Dialog displayed");
                        if (doubleBuilder != null && doubleBuilder.isDisplaying()) {
                            doubleBuilder.close();
                        }
                    }

                    @Override
                    public void onClosed(SingleDateAndTimePickerContainer picker) {
//                        Log.d(TAG, "Dialog closed");
                    }
                })

                .title("")
                .listener(new SingleDateAndTimePickerDialog.Listener() {
                    @Override
                    public void onDateSelected(Date date) {
                        mSingleDateLocaleText.setText(simpleDateLocaleFormat.format(date));
                    }
                });
        singleBuilder.display();
    }

    public void simpleTimeClicked() {

        Calendar calendar = Calendar.getInstance();
        final Date defaultDate = calendar.getTime();

        singleBuilder = new SingleDateAndTimePickerDialog.Builder(this)
                .setTimeZone(TimeZone.getDefault())
                .bottomSheet()
                .curved()

                .defaultDate(defaultDate)

                //.titleTextColor(Color.GREEN)
                //.backgroundColor(Color.BLACK)
                //.mainColor(Color.GREEN)

                .displayMinutes(true)
                .displayHours(true)
                .displayDays(false)
                //.displayMonth(true)
                //.displayYears(true)

                .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {
                    @Override
                    public void onDisplayed(SingleDateAndTimePickerContainer picker) {
                        if (doubleBuilder != null && doubleBuilder.isDisplaying()) {
                            doubleBuilder.close();
                        }
                    }

                    @Override
                    public void onClosed(SingleDateAndTimePickerContainer picker) {
                    }
                })

                .title("Simple Time")
                .listener(new SingleDateAndTimePickerDialog.Listener() {
                    @Override
                    public void onDateSelected(Date date) {
                        mSingleTimeText.setText(simpleTimeFormat.format(date));
                    }
                });

        singleBuilder.display();
    }

    public void showSimpleDateDia() {

        singleBuilder = new SingleDateAndTimePickerDialog.Builder(this)
                .setTimeZone(TimeZone.getDefault())
                .bottomSheet()
                .curved()

                //.titleTextColor(Color.GREEN)
                //.backgroundColor(Color.BLACK)
                //.mainColor(Color.GREEN)

                .displayHours(false)
                .displayMinutes(false)
                .displayDays(true)

                .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {
                    @Override
                    public void onDisplayed(SingleDateAndTimePickerContainer picker) {
//                        Log.d(TAG, "Dialog displayed");
                        if (doubleBuilder != null && doubleBuilder.isDisplaying()) {
                            doubleBuilder.close();
                        }
                    }

                    @Override
                    public void onClosed(SingleDateAndTimePickerContainer picker) {
//                        Log.d(TAG, "Dialog closed");
                    }
                })

                .title("")
                .listener(new SingleDateAndTimePickerDialog.Listener() {
                    @Override
                    public void onDateSelected(Date date) {
                        mSingleDateText.setText(simpleDateOnlyFormat.format(date));
                    }
                });

        singleBuilder.display();
    }

    public void showDoubleDia() {
        if (singleBuilder != null && singleBuilder.isDisplaying()) {
            singleBuilder.close();
        }
        final Date now = new Date();
        final Calendar calendarMin = Calendar.getInstance();
        final Calendar calendarMax = Calendar.getInstance();

        calendarMin.setTime(now); // Set min now
        calendarMax.setTime(new Date(now.getTime() + TimeUnit.DAYS.toMillis(150))); // Set max now + 150 days

        final Date minDate = calendarMin.getTime();
        final Date maxDate = calendarMax.getTime();

        doubleBuilder = new DoubleDateAndTimePickerDialog.Builder(this)
                .setTimeZone(TimeZone.getDefault())
                //.bottomSheet()
                //.curved()

//                .backgroundColor(Color.BLACK)
//                .mainColor(Color.GREEN)
                .minutesStep(15)
                .mustBeOnFuture()

                .minDateRange(minDate)
                .maxDateRange(maxDate)
//                .bottomSheet()
                .secondDateAfterFirst(true)
                //.defaultDate(now)
                .tab0Date(now)
                .tab1Date(new Date(now.getTime() + TimeUnit.HOURS.toMillis(1)))
                .title("Double")
                .tab0Text("DEPART")
                .tab1Text("RETURN")
                .displayListener(new DoubleDateAndTimePickerDialog.DisplayListener() {
                    @Override
                    public void onDisplayed() {
                        if (singleBuilder != null && singleBuilder.isDisplaying()) {
                            singleBuilder.close();
                        }
                    }

                    @Override
                    public void onClosed() {

                    }
                })
                .listener(new DoubleDateAndTimePickerDialog.Listener() {
                    @Override
                    public void onDateSelected(List<Date> dates) {
                        final StringBuilder stringBuilder = new StringBuilder();
                        for (Date date : dates) {
                            stringBuilder.append(simpleDateFormat.format(date)).append("\n");
                        }
                        mDoubleText.setText(stringBuilder.toString());
                    }
                });
        doubleBuilder.display();
    }

    private void showSigleTextDia() {

        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        calendar.set(Calendar.DAY_OF_MONTH, day); // 4. Feb. 2018
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        final Date defaultDate = calendar.getTime();

        singleBuilder = new SingleDateAndTimePickerDialog.Builder(this)
                .setTimeZone(TimeZone.getDefault())
                .bottomSheet()
                .curved()

                //.backgroundColor(Color.BLACK)
                //.mainColor(Color.GREEN)

                .displayHours(false)
                .displayMinutes(false)
                .displayDays(false)
                .displayMonth(true)
                .displayDaysOfMonth(true)
                .displayYears(true)
                .defaultDate(defaultDate)
                .displayMonthNumbers(true)

                //.mustBeOnFuture()

                //.minutesStep(15)
                //.mustBeOnFuture()
                //.defaultDate(defaultDate)
                // .minDateRange(minDate)
                // .maxDateRange(maxDate)

                .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {
                    @Override
                    public void onDisplayed(SingleDateAndTimePickerContainer picker) {
                        if (doubleBuilder != null && doubleBuilder.isDisplaying()) {
                            doubleBuilder.close();
                        }
                    }

                    @Override
                    public void onClosed(SingleDateAndTimePickerContainer picker) {
                    }
                })

                .title("Simple")
                .listener(new SingleDateAndTimePickerDialog.Listener() {
                    @Override
                    public void onDateSelected(Date date) {
                        mSingleText.setText(simpleDateFormat.format(date));
                    }
                });

        singleBuilder.display();
    }
}
