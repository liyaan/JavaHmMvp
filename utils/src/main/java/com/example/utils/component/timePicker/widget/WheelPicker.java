package com.example.utils.component.timePicker.widget;

import com.example.utils.ResourceTable;
import com.example.utils.component.timePicker.*;
import ohos.agp.animation.Animator;
import ohos.agp.animation.AnimatorValue;
import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.components.ScrollHelper;
import ohos.agp.components.VelocityDetector;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.render.ThreeDimView;
import ohos.agp.utils.Color;
import ohos.agp.utils.Matrix;
import ohos.agp.utils.Rect;
import ohos.agp.utils.RectFloat;
import ohos.agp.utils.TextAlignment;
import ohos.app.Context;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.multimodalinput.event.TouchEvent;


import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public abstract class WheelPicker<V> extends Component implements Component.TouchEventListener {
    private static HiLogLabel TAG = new HiLogLabel(HiLog.LOG_APP, 0x000110, "WheelPicker----");
    protected V defaultValue;
    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_DRAGGING = 1;
    public static final int SCROLL_STATE_SCROLLING = 2;
    public static final int MAX_ANGLE = 90;
    protected final static String FORMAT = "%1$02d"; // two digits
    protected final static String DEFAULT_PICKER_TEXT_COLOR = "#646464"; // two digits
    public static final int ALIGN_CENTER = 0;
    public static final int ALIGN_LEFT = 1;
    public static final int ALIGN_RIGHT = 2;
    private final Rect rectDrawn = new Rect();
    private final Rect rectIndicatorHead = new Rect();
    private final Rect rectIndicatorFoot = new Rect();
    private final Rect rectCurrentItem = new Rect();
    private final RectFloat rectFloatCurrentItem = new RectFloat();
    private final Matrix matrixRotate = new Matrix();
    private final ThreeDimView threeDimView = new ThreeDimView();

    protected DateHelper dateHelper = new DateHelper(); // Overwritten from Single..Picker
    private String maxWidthText;
    protected int lastScrollPosition;
    protected Listener<WheelPicker, V> listener;
    private int mVisibleItemCount, mDrawnItemCount;
    private int mHalfDrawnItemCount;
    private int mTextMaxWidth, mTextMaxHeight;
    private int mItemTextColor;
    private int mSelectedItemTextColor;
    private int mItemTextSize;
    private int mIndicatorSize;
    private int mIndicatorColor;
    private int mCurtainColor;
    private int mItemSpace;
    private int mMaxAngle = MAX_ANGLE;
    private int mItemAlign;
    private int mItemHeight, mHalfItemHeight;
    private int mHalfWheelHeight;
    private int selectedItemPosition;
    private int currentItemPosition;
    private int minFlingY, maxFlingY;
    private int minimumVelocity = 50, maximumVelocity = 8000;
    private int wheelCenterX, wheelCenterY;
    private int drawnCenterX, drawnCenterY;
    private int scrollOffsetY;
    private int textMaxWidthPosition;
    private int lastPointY;
    private int downPointY;
    private int touchSlop = 8;
    private Paint paint;
    private boolean hasSameWidth;
    private boolean hasIndicator;
    private boolean hasCurtain;
    private boolean hasAtmospheric;
    private boolean isCyclic;
    private boolean isCurved;
    private ScrollHelper scroller;
    private boolean showOnlyFutureDate;
    protected Adapter<V> adapter = new Adapter<>();
    private boolean isClick;
    private boolean isForceFinishScroll;
    public VelocityDetector tracker;
    private List<WheelPicker> pickers = new ArrayList<>();
    private OnItemSelectedListener onItemSelectedListener;
    private final EventHandler handler = new EventHandler(EventRunner.getMainEventRunner());
    private boolean mOnlayoutAlready = false;
    private Locale customLocale;
    private Date mSettingData;
    private AnimatorValue animator;
    private int radius;
    private OnWheelChangeListener onWheelChangeListener;
    private String mItemTextColorStr;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (null == adapter) {
                return;
            }
            final int itemCount = adapter.getItemCount();
            if (itemCount == 0) {
                return;
            }
            if (scroller.isFinished() && !isForceFinishScroll) {
                if (mItemHeight == 0) {
                    return;
                }
                scrollOffsetY = scroller.getCurrValue(AXIS_Y);
                int position = calculatePosition(itemCount);
                position = position < 0 ? position + itemCount : position;
                setCurrentItemPosition(position);
                onItemSelected();
                if (null != onWheelChangeListener) {
                    onWheelChangeListener.onWheelSelected(position);
                    onWheelChangeListener.onWheelScrollStateChanged(SCROLL_STATE_IDLE);
                }
            }
            if (!scroller.isFinished()) {
                scroller.updateScroll();
                if (null != onWheelChangeListener) {
                    onWheelChangeListener.onWheelScrollStateChanged(SCROLL_STATE_SCROLLING);
                }
                scrollOffsetY = scroller.getCurrValue(AXIS_Y);
                HiLog.info(TAG, "updateScroll == scrollOffsetY " + scrollOffsetY);
                int position = calculatePosition(itemCount);
                if (onItemSelectedListener != null) {
                    onItemSelectedListener.onCurrentItemOfScroll(WheelPicker.this, position);
                }
                onItemCurrentScroll(position, adapter.getItem(position));
                handler.postTask(this, 16, EventHandler.Priority.HIGH);
                invalidate();

            }
        }
    };


    public WheelPicker(Context context) {
        super(context);
    }

    public WheelPicker(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
    }

    public WheelPicker(Context context, AttrSet attrSet, int resId) {
        super(context, attrSet, resId);

    }

    public WheelPicker(Context context, AttrSet attrSet) {
        super(context, attrSet);
        this.mContext = context;
        mItemTextSize = AttrUtil.getDimension(attrSet, "wheel_item_text_size", 35);
        mVisibleItemCount = AttrUtil.getIntegerValue(attrSet, "wheel_visible_item_count", 7);
        selectedItemPosition = AttrUtil.getIntegerValue(attrSet, "wheel_selected_item_position", 0);
        hasSameWidth = AttrUtil.getBooleanValue(attrSet, "wheel_same_width", false);
        textMaxWidthPosition = AttrUtil.getIntegerValue(attrSet, "wheel_maximum_width_text_position", -1);
        maxWidthText = AttrUtil.getStringValue(attrSet, "wheel_maximum_width_text", "");
        mSelectedItemTextColor = AttrUtil.getColorIntValue(attrSet, "wheel_selected_item_text_color", -1);
        mItemTextColorStr = AttrUtil.getStringValue(attrSet, "wheel_item_text_color", DEFAULT_PICKER_TEXT_COLOR);
        mItemTextColor = Color.getIntColor(mItemTextColorStr);
        mItemSpace = AttrUtil.getDimension(attrSet, "wheel_item_space", 46);
        isCyclic = AttrUtil.getBooleanValue(attrSet, "wheel_cyclic", true);
        hasIndicator = AttrUtil.getBooleanValue(attrSet, "wheel_indicator", false);
        mIndicatorColor = AttrUtil.getColorIntValue(attrSet, "wheel_indicator_color", 0xFFEE3333);
        mIndicatorSize = AttrUtil.getDimension(attrSet, "wheel_indicator_size", 6);
        hasCurtain = AttrUtil.getBooleanValue(attrSet, "wheel_curtain", false);
        mCurtainColor = AttrUtil.getColorIntValue(attrSet, "wheel_curtain_color", 0x88FFFFFF);
        hasAtmospheric = AttrUtil.getBooleanValue(attrSet, "wheel_atmospheric", false);
        isCurved = AttrUtil.getBooleanValue(attrSet, "wheel_curved", true);
        mItemAlign = AttrUtil.getIntegerValue(attrSet, "wheel_item_align", ALIGN_CENTER);

        updateVisibleItemCount();
        paint = new Paint();
        paint.setTextSize(35);

        scroller = new ScrollHelper();
        init();

        defaultValue = initDefault();

        adapter.setData(generateAdapterValues(showOnlyFutureDate));
        setCurrentItemPosition(adapter.getItemPosition(defaultValue));
        selectedItemPosition = currentItemPosition;

        setBindStateChangedListener(new BindStateChangedListener() {
            @Override
            public void onComponentBoundToWindow(Component component) {

                if (mSettingData == null) {
                    setAdapter(adapter);
                    setDefault(defaultValue);
                }
            }

            @Override
            public void onComponentUnboundFromWindow(Component component) {

            }
        });

        setEstimateSizeListener(new EstimateSizeListener() {
            @Override
            public boolean onEstimateSize(int widthMeasureSpec, int heightMeasureSpec) {

                onMeasure(widthMeasureSpec, heightMeasureSpec);

                return false;
            }
        });
        addDrawTask(mDrawTask);
        setTouchEventListener(this::onTouchEvent);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        final int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        final int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        final int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        // Correct sizes of original content
        int resultWidth = mTextMaxWidth;
        int resultHeight = mTextMaxHeight * mVisibleItemCount + mItemSpace * (mVisibleItemCount - 1);

        // Correct view sizes again if curved is enable
        if (isCurved) {
            // The text is written on the circle circumference from -mMaxAngle to mMaxAngle.
            // 2 * sinDegree(mMaxAngle): Height of drawn circle
            // Math.PI: Circumference of half unit circle, `mMaxAngle / 90f`: The ratio of half-circle we draw on
            resultHeight = (int) (2 * sinDegree(mMaxAngle) / (Math.PI * mMaxAngle / 90f) * resultHeight);
        }

        // Consideration padding influence the view sizes
        resultWidth += getPaddingLeft() + getPaddingRight();
        resultHeight += getPaddingTop() + getPaddingBottom();

        // Consideration sizes of parent can influence the view sizes
        resultWidth = measureSize(modeWidth, sizeWidth, resultWidth);
        resultHeight = measureSize(modeHeight, sizeHeight, resultHeight);
        setEstimatedSize(resultWidth, resultHeight);
    }

    protected int calculatePosition(int itemCount) {
        float num = (float) -scrollOffsetY / mItemHeight;
        DecimalFormat df = new DecimalFormat("0.0");
        String result = df.format(num);
        int wholeValue = (int) Math.rint(Double.parseDouble(result));
        return (wholeValue + selectedItemPosition) % itemCount;
    }

    protected void setCurrentItemPosition(int position) {
        this.currentItemPosition = position;
    }

    protected void onItemCurrentScroll(int position, V item) {
        if (lastScrollPosition != position) {
            if (listener != null) {
                listener.onCurrentScrolled(this, position, item);
                if (lastScrollPosition == adapter.getItemCount() - 1 && position == 0) {
                    onFinishedLoop();
                }
            }
            lastScrollPosition = position;
        }
    }

    protected void onFinishedLoop() {

    }

    public void setDateHelper(DateHelper dateHelper) {
        this.dateHelper = dateHelper;
    }

    public DateHelper getDateHelper() {
        return dateHelper;
    }

    private final void onItemSelected() {
        int position = currentItemPosition;
        final V item = this.adapter.getItem(position);
        if (null != onItemSelectedListener) {
            onItemSelectedListener.onItemSelected(this, item, position);
        }
        onItemSelected(position, item);
    }

    protected void onItemSelected(int position, V item) {
        if (listener != null) {
            listener.onSelected(this, position, item);
        }
    }

    public void setCyclic(boolean isCyclic) {
        this.isCyclic = isCyclic;
        computeFlingLimitY();
        invalidate();
    }

    public void setItemSpace(int space) {
        mItemSpace = space;
        postLayout();
        invalidate();
    }

    public void stopScroll() {
        if (!scroller.isFinished()) {
            scroller.abortAnimation();
            isForceFinishScroll = true;
        }
    }

    public String getLocalizedString(int stringRes) {
        return LocaleHelper.getString(getContext(), getCurrentLocale(), stringRes);
    }

    public void setVisibleItemCount(int count) {
        mVisibleItemCount = count;
        updateVisibleItemCount();
        postLayout();
    }

    private void updateVisibleItemCount() {
        if (mVisibleItemCount < 2) {
            throw new ArithmeticException("Wheel's visible item count can not be less than 2!");
        }

        if (mVisibleItemCount % 2 == 0) mVisibleItemCount += 1;
        mDrawnItemCount = mVisibleItemCount + 2;
        mHalfDrawnItemCount = mDrawnItemCount / 2;
    }


    public void scrollTo(final int itemPosition) {
        if (itemPosition != currentItemPosition) {
            final int differencesLines = currentItemPosition - itemPosition;
            if (animator == null) {
                animator = new AnimatorValue();
                animator.setDuration(300);
                animator.setCurveType(Animator.CurveType.SMOOTH_STEP);
            }
            animator.setValueUpdateListener(new AnimatorValue.ValueUpdateListener() {
                @Override
                public void onUpdate(AnimatorValue animatorValue, float v) {

                    scrollOffsetY = scrollOffsetY + (int) v * (differencesLines * mItemHeight);
                    invalidate();
                }
            });

            animator.setStateChangedListener(new Animator.StateChangedListener() {
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
                    setCurrentItemPosition(itemPosition);
                    onItemSelected();
                }

                @Override
                public void onPause(Animator animator) {

                }

                @Override
                public void onResume(Animator animator) {

                }
            });
            animator.start();

        }
    }

    public interface OnItemSelectedListener {
        void onItemSelected(WheelPicker picker, Object data, int position);

        void onCurrentItemOfScroll(WheelPicker picker, int position);
    }

    public void setTextAlign(int align) {
        for (WheelPicker picker : pickers) {
            picker.setItemAlign(align);
        }
    }

    protected void onSizeChanged() {
        // Set content region

        if (mOnlayoutAlready) {
            return;
        }
        rectDrawn.set(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(),
            getHeight() - getPaddingBottom());

        // Get the center coordinates of content region

        wheelCenterX = rectDrawn.getCenterX();
        wheelCenterY = rectDrawn.getCenterY();

        // Correct item drawn center
        computeDrawnCenter();

        mHalfWheelHeight = rectDrawn.getHeight() / 2;

        mItemHeight = rectDrawn.getHeight() / mVisibleItemCount;
        mHalfItemHeight = mItemHeight / 2;
        // Initialize fling max Y-coordinates
        computeFlingLimitY();

        // Correct region of indicator
        computeIndicatorRect();

        // Correct region of current select item
        computeCurrentItemRect();
        mOnlayoutAlready = true;

    }

    private void computeIndicatorRect() {
        if (!hasIndicator) return;
        int halfIndicatorSize = mIndicatorSize / 2;
        int indicatorHeadCenterY = wheelCenterY + mHalfItemHeight;
        int indicatorFootCenterY = wheelCenterY - mHalfItemHeight;
        rectIndicatorHead.set(rectDrawn.left, indicatorHeadCenterY - halfIndicatorSize, rectDrawn.right,
            indicatorHeadCenterY + halfIndicatorSize);
        rectIndicatorFoot.set(rectDrawn.left, indicatorFootCenterY - halfIndicatorSize, rectDrawn.right,
            indicatorFootCenterY + halfIndicatorSize);
    }


    public void setDefaultDate(Date date) {
        if (adapter != null && adapter.getItemCount() > 0) {
            final int indexOfDate = findIndexOfDate(date);
            if (indexOfDate >= 0) {
                this.defaultValue = adapter.getData().get(indexOfDate);
                setSelectedItemPosition(indexOfDate);
            }
        }
    }

    public void selectDate(Date date) {
        if(date != null){
            mSettingData = (Date) date.clone();
        }else {
            mSettingData = null;
        }
        setSelectedItemPosition(findIndexOfDate(date));
    }

    public void setShowOnlyFutureDate(boolean showOnlyFutureDate) {
        this.showOnlyFutureDate = showOnlyFutureDate;
    }

    public void setCustomLocale(Locale customLocale) {
        this.customLocale = customLocale;

    }

    public Locale getCurrentLocale() {
        if (customLocale != null) {
            return customLocale;
        }
        return mContext.getResourceManager().getConfiguration().getLocaleProfile().getLocales()[0];
    }

    public int getCurrentItemPosition() {
        return currentItemPosition;
    }

    public void setItemAlign(int align) {
        mItemAlign = align;
        updateItemTextAlign();
        computeDrawnCenter();
        invalidate();
    }

    public void updateAdapter() {
        adapter.setData(generateAdapterValues(showOnlyFutureDate));
        notifyDatasetChanged();
    }

    private void computeDrawnCenter() {
        switch (mItemAlign) {
            case ALIGN_LEFT:
                drawnCenterX = rectDrawn.left;
                break;
            case ALIGN_RIGHT:
                drawnCenterX = rectDrawn.right;
                break;
            default:
                drawnCenterX = wheelCenterX;
                break;
        }
        drawnCenterY = (int) (wheelCenterY - ((paint.ascent() + paint.descent()) / 2));
    }


    @Override
    public LayoutRefreshedListener getLayoutRefreshedListener() {
        return super.getLayoutRefreshedListener();
    }

    private DrawTask mDrawTask = new DrawTask() {
        @Override
        public void onDraw(Component component, Canvas canvas) {
            onSizeChanged();
            drawWheelContent(canvas);
        }
    };


    private void drawWheelContent(Canvas canvas) {
        if (null != onWheelChangeListener) onWheelChangeListener.onWheelScrolled(scrollOffsetY);
        if (mItemHeight - mHalfDrawnItemCount <= 0) {
            return;
        }
        int drawnDataStartPos = -scrollOffsetY / mItemHeight - mHalfDrawnItemCount;

        for (int drawnDataPos = drawnDataStartPos + selectedItemPosition,
             drawnOffsetPos = -mHalfDrawnItemCount;
             drawnDataPos < drawnDataStartPos + selectedItemPosition + mDrawnItemCount;
             drawnDataPos++, drawnOffsetPos++) {
            String data = "";
            if (isCyclic) {
                final int itemCount = adapter.getItemCount();
                int actualPos = drawnDataPos % itemCount;
                actualPos = actualPos < 0 ? (actualPos + itemCount) : actualPos;
                data = adapter.getItemText(actualPos);
            } else {
                if (isPosInRang(drawnDataPos)) {
                    data = adapter.getItemText(drawnDataPos);
                }

            }

            paint.setColor(new Color(mItemTextColor));
            paint.setStyle(Paint.Style.FILL_STYLE);
            int mDrawnItemCenterY = drawnCenterY + (drawnOffsetPos * mItemHeight) + scrollOffsetY % mItemHeight;
            float distanceToCenter = 0;

            if (isCurved) {
                // Correct ratio of item's drawn center to wheel center
                float ratio = (drawnCenterY - Math.abs(drawnCenterY - mDrawnItemCenterY) -
                    rectDrawn.top) * 1.0F / (drawnCenterY - rectDrawn.top);

                // Correct unit
                int unit = 0;
                if (mDrawnItemCenterY > drawnCenterY) {
                    unit = 1;
                } else if (mDrawnItemCenterY < drawnCenterY) unit = -1;

                float degree = clamp((-(1 - ratio) * mMaxAngle * unit), -mMaxAngle, mMaxAngle);
                distanceToCenter = computeYCoordinateAtAngle(degree);

            }
            float itemHeightOffset = (scrollOffsetY % mItemHeight);
            if (hasAtmospheric) {
                int alpha = (int) ((drawnCenterY - Math.abs(drawnCenterY - mDrawnItemCenterY)) * 1.0F / drawnCenterY * 255);
                alpha = alpha < 0 ? 0 : alpha;
                float alphaF = new BigDecimal((float) alpha / 255).setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
                paint.setAlpha(alphaF);
            }
//            // Correct item's drawn centerY base on curved state
            float drawnCenterYTemp = isCurved ? drawnCenterY - distanceToCenter : mDrawnItemCenterY;
            int halfCircumference = (mItemHeight * (mVisibleItemCount - 1));
            //整个圆的周长除以PI得到直径，这个直径用作控件的总高度
            //求出半径
            radius = (int) (halfCircumference / Math.PI);
            double radian = ((mItemHeight * mVisibleItemCount - itemHeightOffset)) / radius;
            // Judges need to draw different color for current item or not
            if (mSelectedItemTextColor != -1) {
                canvas.save();
                if (isCurved) canvas.concat(matrixRotate);

                rectFloatCurrentItem.bottom = rectCurrentItem.bottom;
                rectFloatCurrentItem.top = rectCurrentItem.top;
                rectFloatCurrentItem.right = rectCurrentItem.right;
                rectFloatCurrentItem.left = rectCurrentItem.left;
                // 裁剪画布 DIFFERENCE 不同部分 除了rectFloatCurrentItem矩形之外，其他的都是未选中颜色
                canvas.clipRect(rectFloatCurrentItem, Canvas.ClipOp.DIFFERENCE);
//                canvas.scale(1.0F, (float) Math.sin(radian) * 0.8f);
                canvas.drawText(paint, data, drawnCenterX, drawnCenterYTemp);
                canvas.restore();
                paint.setColor(new Color(mSelectedItemTextColor));
                canvas.save();

                if (isCurved) canvas.concat(matrixRotate);
                //INTERSECT 相交部分 只取rectFloatCurrentItem矩形，设置为选中色
                canvas.clipRect(rectFloatCurrentItem, Canvas.ClipOp.INTERSECT);
//                canvas.scale(1.0F, (float) Math.sin(radian) * 0.8f);
                canvas.drawText(paint, data, drawnCenterX, drawnCenterYTemp);
                canvas.restore();
            } else {
                canvas.save();
                canvas.clipRect(new RectFloat(rectDrawn));
                if (isCurved) canvas.concat(matrixRotate);
                canvas.drawText(paint, data, drawnCenterX, drawnCenterYTemp);
                canvas.restore();
            }
        }
        // Need to draw curtain or not
        if (hasCurtain) {
            paint.setColor(new Color(mCurtainColor));
            paint.setStyle(Paint.Style.FILL_STYLE);
            canvas.drawRect(rectCurrentItem, paint);
        }
//         Need to draw indicator or not
        if (hasIndicator) {
            paint.setColor(new Color(mIndicatorColor));
            paint.setStyle(Paint.Style.FILL_STYLE);
            canvas.drawRect(rectIndicatorHead, paint);
            canvas.drawRect(rectIndicatorFoot, paint);
        }

    }

    private float clamp(float value, float min, float max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

    public void setItemTextColor(int color) {
        if (mItemTextColorStr.equals(DEFAULT_PICKER_TEXT_COLOR)) {
            mItemTextColor = color;
            invalidate();
        }
    }

    public void setCurved(boolean isCurved) {
        this.isCurved = isCurved;
        postLayout();
        invalidate();
    }

    public void setSelectedItemTextColor(int color) {
        mSelectedItemTextColor = color;
        computeCurrentItemRect();
        invalidate();
    }

    public void setCurvedMaxAngle(int maxAngle) {
        this.mMaxAngle = maxAngle;
        postLayout();
        invalidate();
    }

    public void setItemTextSize(int size) {
        if (mItemTextSize != size) {
            mItemTextSize = size;
            paint.setTextSize(mItemTextSize);
            computeTextSize();
            postLayout();
            invalidate();
        }
    }

    private void computeCurrentItemRect() {
        rectCurrentItem.set(rectDrawn.left, wheelCenterY - mHalfItemHeight, rectDrawn.right, wheelCenterY + mHalfItemHeight);
    }

    private float computeYCoordinateAtAngle(float degree) {
        // Compute y-coordinate for item at degree. mMaxAngle is at mHalfWheelHeight
        return sinDegree(degree) / sinDegree(mMaxAngle) * mHalfWheelHeight;
    }

    @Override
    public boolean onTouchEvent(Component component, TouchEvent event) {
        if (isEnabled()) {
            switch (event.getAction()) {
                case TouchEvent.PRIMARY_POINT_DOWN:
                    if (null == tracker) {
                        tracker = VelocityDetector.obtainInstance();
                    } else {
                        tracker.clear();
                    }
                    tracker.addEvent(event);
                    if (!scroller.isFinished()) {
                        scroller.abortAnimation();
                        isForceFinishScroll = true;
                    }
                    downPointY = lastPointY = (int) event.getPointerScreenPosition(0).getY();

                    break;
                case TouchEvent.POINT_MOVE:
                    if (Math.abs(downPointY - event.getPointerScreenPosition(0).getY()) < touchSlop
                        && computeDistanceToEndPoint(scroller.getCurrValue(AXIS_Y) % mItemHeight) > 0) {
                        isClick = true;
                        break;
                    }
                    isClick = false;
                    tracker.addEvent(event);
                    if (null != onWheelChangeListener) {
                        onWheelChangeListener.onWheelScrollStateChanged(SCROLL_STATE_DRAGGING);
                    }

                    // Scroll WheelPicker's content
                    int move = (int) (event.getPointerScreenPosition(0).getY() - lastPointY);
                    if (Math.abs(move) < 1) break;
                    scrollOffsetY += move;
                    lastPointY = (int) event.getPointerScreenPosition(0).getY();
                    invalidate();


                    break;
                case TouchEvent.PRIMARY_POINT_UP:
                    if (isClick) break;
                    int velocity = 0;
                    if (null != tracker) {
                        tracker.addEvent(event);
                        tracker.calculateCurrentVelocity(600, maximumVelocity, maximumVelocity);
                        velocity = (int) tracker.getVerticalVelocity();
                    }
                    // Judges the WheelPicker is scroll or fling base on current velocity
                    isForceFinishScroll = false;

                    if (Math.abs(velocity) > minimumVelocity) {
//                        scroller.doFling(0, scrollOffsetY, 0, velocity, 0, 0, minFlingY, maxFlingY);
                        double finalY = getSplineFlingDistance(velocity, 0, scrollOffsetY, minFlingY, maxFlingY);
                        int dxY = (int) (finalY - scrollOffsetY);
                        scroller.startScroll(0, scrollOffsetY, 0, dxY + computeDistanceToEndPoint((int) (finalY % mItemHeight)));
                    } else {
                        scroller.startScroll(0, scrollOffsetY, 0, computeDistanceToEndPoint(scrollOffsetY % mItemHeight));

                    }
                    // Correct coordinates
                    if (!isCyclic) {
                        if (scroller.getFlingDistanceY(0) > maxFlingY) {
//                            scroller.setFinalY(maxFlingY);
                        } else if (scroller.getFlingDistanceY(0) < minFlingY) {
//                            scroller.setFinalY(minFlingY);
                        }
                    }
                    handler.postSyncTask(runnable);
                    if (null != tracker) {
                        tracker.clear();
                        tracker = null;
                    }
                    break;
                case TouchEvent.CANCEL:
                    if (null != tracker) {
                        tracker.clear();
                        tracker = null;
                    }
                    break;
            }
        }
        return true;
    }

    private static final float INFLEXION = 0.35f; // Tension lines cross at (INFLEXION, 1)
    private static final float SCROLL_FRICTION = 0.015f;
    private float mFlingFriction = SCROLL_FRICTION;
    private static final float mPpi = 3 * 160.0f;
    private static float DECELERATION_RATE = (float) (Math.log(0.78) / Math.log(0.9));

    private double getSplineFlingDistance(float velocityY, float velocityX, int startY, int mMinY, int mMaxY) {
        final double l = getSplineDeceleration(velocityY);
        float velocity = (float) Math.hypot(velocityX, velocityY);
        float coeffY = velocity == 0 ? 1.0f : velocityY / velocity;
        final double decelMinusOne = DECELERATION_RATE - 1.0;
        double totalDistance = mFlingFriction * computeDeceleration(0.84f) * Math.exp(DECELERATION_RATE / decelMinusOne * l);
        int mFinalY = startY + (int) Math.round(totalDistance * coeffY);
        // Pin to mMinY <= mFinalY <= mMaxY
        mFinalY = Math.min(mFinalY, mMaxY);
        mFinalY = Math.max(mFinalY, mMinY);

        return mFinalY;
    }

    private float computeDeceleration(float friction) {
        return 9.80665f   // g (m/s^2)
            * 39.37f               // inch/meter
            * mPpi                 // pixels per inch
            * friction;
    }

    private double getSplineDeceleration(float velocity) {
        return Math.log(INFLEXION * Math.abs(velocity) / (mFlingFriction * computeDeceleration(0.84f)));
    }

    private int getSplineFlingDuration(float velocity) {
        final double l = getSplineDeceleration(velocity);
        final double decelMinusOne = DECELERATION_RATE - 1.0;
        return (int) (1000.0 * Math.exp(l / decelMinusOne));
    }


    static class ViscousFluidInterpolator implements Interpolator {
        /** Controls the viscous fluid effect (how much of it). */
        private static final float VISCOUS_FLUID_SCALE = 8.0f;

        private static final float VISCOUS_FLUID_NORMALIZE;
        private static final float VISCOUS_FLUID_OFFSET;

        static {

            // must be set to 1.0 (used in viscousFluid())
            VISCOUS_FLUID_NORMALIZE = 1.0f / viscousFluid(1.0f);
            // account for very small floating-point error
            VISCOUS_FLUID_OFFSET = 1.0f - VISCOUS_FLUID_NORMALIZE * viscousFluid(1.0f);
        }

        private static float viscousFluid(float x) {
            x *= VISCOUS_FLUID_SCALE;
            if (x < 1.0f) {
                x -= (1.0f - (float)Math.exp(-x));
            } else {
                float start = 0.36787944117f;   // 1/e == exp(-1)
                x = 1.0f - (float)Math.exp(1.0f - x);
                x = start + x * (1.0f - start);
            }
            return x;
        }

        @Override
        public float getInterpolation(float input) {
            final float interpolated = VISCOUS_FLUID_NORMALIZE * viscousFluid(input);
            if (interpolated > 0) {
                return interpolated + VISCOUS_FLUID_OFFSET;
            }
            return interpolated;
        }
    }


    public interface OnWheelChangeListener {
        /**
         * <p>
         * Invoke when WheelPicker scroll stopped
         * WheelPicker will return a distance offset which between current scroll position and
         * initial position, this offset is a positive or a negative, positive means WheelPicker is
         * scrolling from bottom to top, negative means WheelPicker is scrolling from top to bottom
         *
         * @param offset <p>
         * Distance offset which between current scroll position and initial position
         */
        void onWheelScrolled(int offset);

        /**
         * <p>
         * Invoke when WheelPicker scroll stopped
         * This method will be called when WheelPicker stop and return current selected item data's
         * position in list
         *
         * @param position <p>
         * Current selected item data's position in list
         */
        void onWheelSelected(int position);

        /**
         * <p>
         * Invoke when WheelPicker's scroll state changed
         * The state of WheelPicker always between idle, dragging, and scrolling, this method will
         * be called when they switch
         *
         * @param state {@link WheelPicker#SCROLL_STATE_IDLE}
         * {@link WheelPicker#SCROLL_STATE_DRAGGING}
         * {@link WheelPicker#SCROLL_STATE_SCROLLING}
         * <p>
         * State of WheelPicker, only one of the following
         * {@link WheelPicker#SCROLL_STATE_IDLE}
         * Express WheelPicker in state of idle
         * {@link WheelPicker#SCROLL_STATE_DRAGGING}
         * Express WheelPicker in state of dragging
         * {@link WheelPicker#SCROLL_STATE_SCROLLING}
         * Express WheelPicker in state of scrolling
         */
        void onWheelScrollStateChanged(int state);
    }

    public void setOnWheelChangeListener(OnWheelChangeListener listener) {
        onWheelChangeListener = listener;
    }

    @Override
    public void setLayoutRefreshedListener(LayoutRefreshedListener listener) {
        super.setLayoutRefreshedListener(listener);
    }

    private int computeDistanceToEndPoint1(int remainder) {
        if (Math.abs(remainder) > mHalfItemHeight) {
            if (scrollOffsetY < 0) {
                return -mItemHeight - remainder;
            } else {
                return mItemHeight - remainder;
            }
        } else {
            return -remainder;
        }
    }

    private int computeDistanceToEndPoint(int remainder) {
        if (Math.abs(remainder) > mHalfItemHeight) {
            if (scrollOffsetY < 0) {
                return -mItemHeight - remainder;
            } else {
                return mItemHeight - remainder;
            }
        } else {
            return -remainder;
        }
    }

    public void setDefault(V defaultValue) {
        this.defaultValue = defaultValue;
        updateDefault();
    }

    protected void updateDefault() {
        setSelectedItemPosition(getDefaultItemPosition());
    }

    public void setSelectedItemPosition(int position) {
        position = Math.min(position, adapter.getItemCount() - 1);
        position = Math.max(position, 0);
        selectedItemPosition = position;
        setCurrentItemPosition(position);
        scrollOffsetY = 0;
        computeFlingLimitY();
        postLayout();
        invalidate();
    }


    private void computeFlingLimitY() {
        int currentItemOffset = selectedItemPosition * mItemHeight;
        minFlingY = isCyclic ? Integer.MIN_VALUE
            : -mItemHeight * (adapter.getItemCount() - 1) + currentItemOffset;
        maxFlingY = isCyclic ? Integer.MAX_VALUE : currentItemOffset;
    }

    public int getDefaultItemPosition() {
        return adapter.getData().indexOf(defaultValue);
    }

    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;

        updateItemTextAlign();

        computeTextSize();

        notifyDatasetChanged();
    }

    private void updateItemTextAlign() {
        switch (mItemAlign) {
            case ALIGN_LEFT:
                paint.setTextAlign(TextAlignment.LEFT);
                break;
            case ALIGN_RIGHT:
                paint.setTextAlign(TextAlignment.RIGHT);
                break;
            default:
                paint.setTextAlign(TextAlignment.CENTER);
                break;
        }
    }

    protected String getFormattedValue(Object value) {
        return String.valueOf(value);
    }

    /**
     * TODO: {@link Adapter#data} could contain 'Data' class objects. 'Data' could be composed of
     * a String: displayedValue (the value to be displayed in the wheel) and
     * a Date/Calendar: comparisonDate (a reference date/calendar that will help to find the index).
     * This could clean this method and {@link #getFormattedValue(Object)}.
     * <p>
     * Finds the index in the wheel for a date
     *
     * @param date the targeted date
     * @return the index closed to {@code date}. Returns 0 if not found.
     */
    public int findIndexOfDate(Date date) {
        String formatItem = getFormattedValue(date);

        if (this instanceof WheelDayOfMonthPicker) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeZone(dateHelper.getTimeZone());
            calendar.setTime(date);
            return calendar.get(Calendar.DAY_OF_MONTH) - 1;
        }

        if (this instanceof WheelDayPicker) {
            String today = getFormattedValue(new Date());
            if (today.equals(formatItem)) {
                return getTodayItemPosition() - 1;
            }
        }
        if (this instanceof WheelMonthPicker) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeZone(dateHelper.getTimeZone());
            calendar.setTime(date);
            return calendar.get(Calendar.MONTH) - 1;
        }

        if (this instanceof WheelYearPicker) {
            WheelYearPicker yearPick = (WheelYearPicker) this;
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeZone(dateHelper.getTimeZone());
            calendar.setTime(date);
            return calendar.get(Calendar.YEAR) - yearPick.minYear;
        }

        int formatItemInt = Integer.MIN_VALUE;
        try {
            formatItemInt = Integer.parseInt(formatItem);
        } catch (NumberFormatException e) {
        }

        final int itemCount = adapter.getItemCount();
        int index = 0;
        for (int i = 0; i < itemCount; ++i) {
            final String object = adapter.getItemText(i);

            if (formatItemInt != Integer.MIN_VALUE) {
                // displayed values are Integers
                int objectInt = Integer.parseInt(object);
                if (this instanceof WheelHourPicker && ((WheelHourPicker) this).isAmPm) {
                    // In case of hours and AM/PM mode, apply modulo 12
                    objectInt = objectInt % 12;
                }
                if (objectInt <= formatItemInt) {
                    index = i;
                }
            } else if (formatItem.equals(object)) {
                return i;
            }
        }
        return index;
    }

    public int getTodayItemPosition() {
        List<V> list = adapter.getData();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) instanceof DateWithLabel) {
                DateWithLabel dwl = (DateWithLabel) list.get(i);
                String todayStr = LocaleHelper.getString(mContext, getCurrentLocale(), ResourceTable.String_picker_today);
                if (dwl.mLabel.equals(todayStr)) {
                    return i + 1;
                }
            }
        }
        return 0;
    }

    private void computeTextSize() {
        mTextMaxWidth = mTextMaxHeight = 0;
        if (hasSameWidth) {
            mTextMaxWidth = (int) paint.measureText(adapter.getItemText(0));
        } else if (isPosInRang(textMaxWidthPosition)) {
            mTextMaxWidth = (int) paint.measureText(adapter.getItemText(textMaxWidthPosition));
        } else if (!Utils.isEmptyStr(maxWidthText)) {
            mTextMaxWidth = (int) paint.measureText(maxWidthText);
        } else {
            final int itemCount = adapter.getItemCount();
            for (int i = 0; i < itemCount; ++i) {
                String text = adapter.getItemText(i);
                int width = (int) paint.measureText(text);
                mTextMaxWidth = Math.max(mTextMaxWidth, width);
            }
        }
        final Paint.FontMetrics metrics = paint.getFontMetrics();
        mTextMaxHeight = (int) (metrics.bottom - metrics.top);
    }

    public void notifyDatasetChanged() {
        if (selectedItemPosition > adapter.getItemCount() - 1
            || currentItemPosition > adapter.getItemCount() - 1) {
            setCurrentItemPosition(adapter.getItemCount() - 1);
            selectedItemPosition = currentItemPosition;
        } else {
            selectedItemPosition = currentItemPosition;
        }
        scrollOffsetY = 0;
        computeTextSize();
        computeFlingLimitY();
        postLayout();
        invalidate();
    }

    private int measureSize(int mode, int sizeExpect, int sizeActual) {
        int realSize;
        if (mode == MeasureSpec.PRECISE) {
            realSize = sizeExpect;
        } else {
            realSize = sizeActual;
            if (mode == MeasureSpec.NOT_EXCEED) realSize = Math.min(realSize, sizeExpect);
        }
        return realSize;
    }


    private boolean isPosInRang(int position) {
        return position >= 0 && position < adapter.getItemCount();
    }

    public static class Adapter<V> implements BaseAdapter {
        private List<V> data;

        public Adapter() {
            this(new ArrayList<V>());
        }

        public Adapter(List<V> data) {
            this.data = new ArrayList<V>();
            this.data.addAll(data);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        @Override
        public V getItem(int position) {
            final int itemCount = getItemCount();
            return itemCount == 0 ? null : data.get((position + itemCount) % itemCount);
        }

        @Override
        public String getItemText(int position) {
            try {
                String dataStr = String.valueOf(data.get(position));
                return dataStr;
            } catch (Throwable t) {
                return "";
            }
        }

        public List<V> getData() {
            return data;
        }

        public void setData(List<V> data) {
            this.data.clear();
            this.data.addAll(data);
        }

        public void addData(List<V> data) {
            this.data.addAll(data);
        }

        public int getItemPosition(V value) {
            int position = -1;
            if (data != null) {
                return data.indexOf(value);
            }
            return position;
        }
    }

    protected interface Listener<PICKER extends WheelPicker, V> {
        void onSelected(PICKER picker, int position, V value);

        void onCurrentScrolled(PICKER picker, int position, V value);
    }

    public interface BaseAdapter<V> {

        int getItemCount();

        V getItem(int position);

        String getItemText(int position);
    }

    private float sinDegree(float degree) {
        return (float) Math.sin(Math.toRadians(degree));
    }

    protected abstract List<V> generateAdapterValues(boolean showOnlyFutureDates);

    protected abstract void init();

    protected abstract V initDefault();

}
