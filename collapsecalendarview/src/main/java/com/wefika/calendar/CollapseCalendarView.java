package com.wefika.calendar;

import com.liyaan.collapsecalendarview.ResourceTable;
import com.wefika.calendar.annotation.NonNull;
import com.wefika.calendar.annotation.Nullable;
import com.wefika.calendar.manager.*;
import com.wefika.calendar.widget.DayView;
import com.wefika.calendar.widget.WeekView;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.*;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.render.Canvas;
import ohos.agp.utils.Color;
import ohos.app.Context;
import ohos.multimodalinput.event.TouchEvent;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by Blaz Solar on 28/02/14.
 */
public class CollapseCalendarView extends DirectionalLayout implements Component.ClickedListener, Component.DrawTask,
        Component.TouchEventListener, Component.BindStateChangedListener {

    private static final String TAG = "CalendarView";

    @Nullable
    private CalendarManager mManager;

    @NonNull
    private Text mTitleView;
    @NonNull
    private Image mPrev;
    @NonNull
    private Image mNext;
    @NonNull
    private DirectionalLayout mWeeksView;

    @NonNull
    private final LayoutScatter mInflater;
    @NonNull
    private final RecycleBin mRecycleBin = new RecycleBin();

    @Nullable
    private OnDateSelect mListener;

    @NonNull
    private Text mSelectionText;
    @NonNull
    private DirectionalLayout mHeader;

    @NonNull
    private ResizeManager mResizeManager;

    private boolean initialized;

    private Component component;

    public CollapseCalendarView(Context context) {
        this(context, null);
    }

    public CollapseCalendarView(Context context, AttrSet attrs) {
        this(context, attrs, null);
    }

    public CollapseCalendarView(Context context, AttrSet attrs, String defStyle) {
        super(context, attrs, defStyle);

        mInflater = LayoutScatter.getInstance(context);

        mResizeManager = new ResizeManager(this);

        component = mInflater.parse(ResourceTable.Layout_calendar_layout, this, false);
        addComponent(component);
        onFinishInflate();

        addDrawTask(this);
        setClickedListener(this);
        setTouchEventListener(this);
        setBindStateChangedListener(this);
    }

    public void init(@NonNull CalendarManager manager) {
        if (manager != null) {

            mManager = manager;

            populateLayout();

            if (mListener != null) {
                mListener.onDateSelected(mManager.getSelectedDay());
            }

        }
    }

    @Nullable
    public CalendarManager getManager() {
        return mManager;
    }


    @Override
    public void onClick(Component v) {
        System.out.println(TAG + ":On click");
        if (mManager != null) {
            int id = v.getId();
            if (id == ResourceTable.Id_prev) {
                if (mManager.prev()) {
                    populateLayout();
                }
            } else if (id == ResourceTable.Id_next) {
                System.out.println(TAG + ":next");
                if (mManager.next()) {
                    System.out.println(TAG + ":populate");
                    populateLayout();
                }
            }

        }
    }

    private Component mComponent;
    private Canvas mCanvas;

    @Override
    public void onDraw(Component component, Canvas canvas) {
        mComponent = component;
        mCanvas = canvas;
        mResizeManager.onDraw();

    }

    @Override
    public void invalidate() {
        super.invalidate();
        onDraw(mComponent, mCanvas);
    }

    @Nullable
    public CalendarManager.State getState() {
        if (mManager != null) {
            return mManager.getState();
        } else {
            return null;
        }
    }

    public void setListener(@Nullable OnDateSelect listener) {
        mListener = listener;
    }

    /**
     * @deprecated This will be removed
     */
    public void setTitle(@Nullable String text) {
        if (StringUtils.isEmpty(text)) {
            mHeader.setVisibility(Component.VISIBLE);
            mSelectionText.setVisibility(Component.HIDE);
        } else {
            mHeader.setVisibility(Component.HIDE);
            mSelectionText.setVisibility(Component.VISIBLE);
            mSelectionText.setText(text);
        }
    }

    @Override
    public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
        return mResizeManager.onTouchEvent(touchEvent);
    }

    private void onFinishInflate() {
        ShapeElement mWeeksViewBG = new ShapeElement();
        mWeeksViewBG.setStroke(2, RgbColor.fromArgbInt(Color.getIntColor("#00aeef")));

        mTitleView = (Text) component.findComponentById(ResourceTable.Id_title);
        mPrev = (Image) component.findComponentById(ResourceTable.Id_prev);
        mNext = (Image) component.findComponentById(ResourceTable.Id_next);
        mWeeksView = (DirectionalLayout) component.findComponentById(ResourceTable.Id_weeks);
        mWeeksView.setBackground(mWeeksViewBG);

        mHeader = (DirectionalLayout) component.findComponentById(ResourceTable.Id_header);
        mSelectionText = (Text) component.findComponentById(ResourceTable.Id_selection_title);

        mPrev.setClickedListener(this);
        mNext.setClickedListener(this);

        populateLayout();
    }

    private void populateDays() {

        if (!initialized) {
            CalendarManager manager = getManager();

            if (manager != null) {
                Formatter formatter = manager.getFormatter();

                DirectionalLayout layout = (DirectionalLayout) component.findComponentById(ResourceTable.Id_days);

                LocalDate date = LocalDate.now().withDayOfWeek(DateTimeConstants.MONDAY);
                for (int i = 0; i < 7; i++) {
                    Text textView = (Text) layout.getComponentAt(i);
                    textView.setText(formatter.getDayName(date));
                    date = date.plusDays(1);
                }

                initialized = true;
            }
        }

    }

    public void populateLayout() {

        if (mManager != null) {

            populateDays();
            if (mManager.hasPrev()) {
                mPrev.setPixelMap(ResourceTable.Media_ic_arrow_left_normal);
            } else {
                mPrev.setPixelMap(ResourceTable.Media_ic_arrow_left_disabled);
            }

            if (mManager.hasNext()) {
                mNext.setPixelMap(ResourceTable.Media_ic_arrow_right_normal);
            } else {
                mNext.setPixelMap(ResourceTable.Media_ic_arrow_right_disabled);
            }

            mTitleView.setText(mManager.getHeaderText());

            if (mManager.getState() == CalendarManager.State.MONTH) {
                populateMonthLayout((Month) mManager.getUnits());
            } else {
                populateWeekLayout((Week) mManager.getUnits());
            }
        }

    }

    private void populateMonthLayout(Month month) {

        List<Week> weeks = month.getWeeks();
        int cnt = weeks.size();
        for (int i = 0; i < cnt; i++) {
            WeekView weekView = getWeekView(i);
            populateWeekLayout(weeks.get(i), weekView);
        }

        int childCnt = mWeeksView.getChildCount();
        if (cnt < childCnt) {
            for (int i = cnt; i < childCnt; i++) {
                cacheView(i);
            }
        }

    }

    private void populateWeekLayout(Week week) {
        WeekView weekView = getWeekView(0);
        populateWeekLayout(week, weekView);

        int cnt = mWeeksView.getChildCount();
        if (cnt > 1) {
            for (int i = cnt - 1; i > 0; i--) {
                cacheView(i);
            }
        }
    }

    private void populateWeekLayout(@NonNull Week week, @NonNull WeekView weekView) {
        List<Day> days = week.getDays();
        ShapeElement shapeElement = new ShapeElement();
        shapeElement.setCornerRadius(100.0f);
        shapeElement.setRgbColor(RgbColor.fromArgbInt(Color.getIntColor("#00aeef")));

        for (int i = 0; i < 7; i++) {
            final Day day = days.get(i);
            DayView dayView = (DayView) weekView.getComponentAt(i);
            dayView.setText(day.getText());
            dayView.setSelected(day.isSelected());
            dayView.setCurrent(day.isCurrent());

            boolean enables = day.isEnabled();
            dayView.setEnabled(enables);

            if (enables) {
                if (day.isSelected()) {
                    dayView.setTextColor(Color.WHITE);
                    dayView.setBackground(shapeElement);
                } else {
                    dayView.setTextColor(Color.BLACK);
                    dayView.setBackground(null);
                }
                dayView.setClickedListener(new ClickedListener() {
                    @Override
                    public void onClick(Component v) {
                        LocalDate date = day.getDate();
                        if (mManager.selectDay(date)) {
                            populateLayout();
                            if (mListener != null) {
                                mListener.onDateSelected(date);
                            }
                        }
                    }
                });
            } else {
                dayView.setClickedListener(null);
                dayView.setTextColor(Color.GRAY);
                dayView.setBackground(null);
            }
        }

    }

    @NonNull
    public DirectionalLayout getWeeksView() {
        return mWeeksView;
    }

    @NonNull
    private WeekView getWeekView(int index) {
        int cnt = mWeeksView.getChildCount();
        if (cnt < index + 1) {
            for (int i = cnt; i < index + 1; i++) {
                Component view = getView();
                mWeeksView.addComponent(view);
            }
        }

        return (WeekView) mWeeksView.getComponentAt(index);
    }

    private Component getView() {
        Component view = mRecycleBin.recycleView();
        if (view == null) {
            view = mInflater.parse(ResourceTable.Layout_week_layout, this, false);
        } else {
            view.setVisibility(Component.VISIBLE);
        }
        return view;
    }

    private void cacheView(int index) {
        Component view = mWeeksView.getComponentAt(index);
        if (view != null) {
            mWeeksView.removeComponentAt(index);
            mRecycleBin.addView(view);
        }
    }

    public LocalDate getSelectedDate() {
        return mManager.getSelectedDay();
    }

    @Override
    public void onComponentBoundToWindow(Component component) {

    }

    @Override
    public void onComponentUnboundFromWindow(Component component) {
        mResizeManager.recycle();
    }


    private class RecycleBin {

        private final Queue<Component> mViews = new LinkedList<>();

        @Nullable
        public Component recycleView() {
            return mViews.poll();
        }

        public void addView(@NonNull Component view) {
            mViews.add(view);
        }

    }

    public interface OnDateSelect {
        public void onDateSelected(LocalDate date);
    }


}
