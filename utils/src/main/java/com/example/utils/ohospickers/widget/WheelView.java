package com.example.utils.ohospickers.widget;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


import com.example.utils.ohospickers.adapter.WheelAdapter;
import com.example.utils.ohospickers.common.*;
import com.example.utils.ohospickers.listeners.OnItemPickListener;
import com.example.utils.ohospickers.listeners.WheelViewGestureListener;
import com.example.utils.ohospickers.model.IPickerViewData;
import com.example.utils.ohospickers.util.AttrUtils;
import com.example.utils.ohospickers.util.LogUtils;
import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.text.Font;
import ohos.agp.utils.Color;
import ohos.agp.utils.Rect;
import ohos.agp.utils.TextTool;
import ohos.agp.window.service.DisplayAttributes;
import ohos.agp.window.service.DisplayManager;
import ohos.app.Context;
import ohos.eventhandler.EventRunner;
import ohos.multimodalinput.event.TouchEvent;

import static ohos.agp.utils.LayoutAlignment.*;

/**
 * 3d滚轮控件
 *
 * @author matt
 * blog: addapp.cn
 */
public class WheelView extends Component implements Component.EstimateSizeListener, Component.TouchEventListener, Component.DrawTask {

  private WheelViewGestureListener gestureDetector;

  public enum ACTION { // 点击，滑翔(滑到尽头)，拖拽事件
    CLICK, FLING, DRAG
  }

  private LineConfig lineConfig = null;//分割线配置
  private LineConfig.DividerType dividerType;//分隔线类型

  Context context;

  public MessageHandler handler;
  //    private GestureDetector gestureDetector;//控制滑动
  private OnItemPickListener onItemPickListener;

  private boolean isOptions = false;
  //    private boolean isCenterLabel = true;//
  private boolean onlyShowCenterLabel = true;//附加单位是否仅仅只显示在选中项后面  是否只中间选中显示label   中间显示只有一个label   否则 和item个数一样

  public void setLabelSpace(int labelSpace) {
    this.labelSpace = labelSpace;
  }//label与文本的间距

  private int labelSpace = 0;

  // Timer mTimer;
  ScheduledExecutorService mExecutor = Executors.newSingleThreadScheduledExecutor();
  private ScheduledFuture<?> mFuture;

  Paint paintOuterText;
  Paint paintCenterText;
  Paint paintLine;

  WheelAdapter<String> adapter;

  private String label;//附加单位
  public int textSize;//选项的文字大小
  public int maxTextWidth;
  public int maxTextHeight;
  public float itemHeight;//每行高度

  Font typeface = Font.MONOSPACE;//字体样式，默认是等宽字体

  private int textColorOut = 0xFFa8a8a8;
  private int textColorCenter = 0xFF2a2a2a;
  private int dividerColor = 0xFFd5d5d5;
  private int dividerWidth;

  // 条目间距倍数
  float lineSpacingMultiplier = 1.6F;
  public boolean isLoop;

  // 第一条线Y坐标值
  float firstLineY;
  //第二条线Y坐标
  float secondLineY;
  //中间label绘制的Y坐标
  float centerY;

  //滚动总高度y值
  public float totalScrollY;
  //初始化默认选中项
  public int initPosition;
  //选中的Item
  private String selectedItem;
  //选中的Item是第几个
  private int selectedPosition;
  int preCurrentIndex;
  //滚动偏移值,用于记录滚动了多少个item
  int change;

  // 绘制几个条目，实际上第一项和最后一项Y轴压缩成0%了，所以可见的数目实际为9
  int itemsVisible = 11;

  int measuredHeight;// WheelView 控件高度
  int measuredWidth;// WheelView 控件宽度

  // 半圆周长
  int halfCircumference;
  // 半径
  int radius;

  private int mOffset = 0;
  private float previousY = 0;
  long startTime = 0;

  // 修改这个值可以改变滑行速度
  private static final int VELOCITY_FLING = 10;
  int widthMeasureSpec, heightMeasureSpec;

  private int mGravity = CENTER;
  private int drawCenterContentStart = 0;//中间选中文字开始绘制位置
  private int drawOutContentStart = 0;//非中间文字开始绘制位置
  private static final float SCALE_CONTENT = 0.8F;//非中间文字则用此控制高度，压扁形成3d错觉
  private float centerContentOffset;//偏移量
  private float density = 0;//屏幕密度

  public WheelView(Context context) {
    this(context, null);
  }

  public WheelView(Context context, AttrSet attrs) {
    super(context, attrs);

    textSize = 16;//默认大小

    DisplayAttributes attributes = DisplayManager.getInstance().getDefaultDisplay(context).get().getAttributes();
    density = attributes.densityPixels; // 屏幕密度（0.75/1.0/1.5/2.0/3.0）

    if (density < 1) {//根据密度不同进行适配
      centerContentOffset = 2.4F;
    } else if (1 <= density && density < 2) {
      centerContentOffset = 3.6F;
    } else if (2 <= density && density < 3) {
      centerContentOffset = 6.0F;
    } else if (density >= 3) {
      centerContentOffset = density * 2.5F;
    }
    if (attrs != null) {
      mGravity = AttrUtils.getIntFromAttr(attrs, "wheelview_gravity", CENTER);
      textColorOut = AttrUtils.getColorFromAttr(attrs, "wheelview_textColorOut", 0xFFa8a8a8);
      textColorCenter = AttrUtils.getColorFromAttr(attrs, "wheelview_textColorCenter", 0xFF2a2a2a);
      dividerColor = AttrUtils.getColorFromAttr(attrs, "wheelview_wheelview_dividerColor", 0xFFd5d5d5);
      dividerWidth = AttrUtils.getDimensionFromAttr(attrs, "wheelview_wheelview_dividerWidth", 2);
      textSize = AttrUtils.getDimensionFromAttr(attrs, "wheelview_wheelview_textSize", textSize);
      lineSpacingMultiplier = AttrUtils.getFloatFromAttr(attrs, "wheelview_wheelview_lineSpacingMultiplier", lineSpacingMultiplier);
    }
    judgeLineSpace();

    initWheelView(context);
    setEstimateSizeListener(this);
    setTouchEventListener(this);
    addDrawTask(this);
  }

  /**
   * 判断间距是否在1.0-2.0之间
   */
  private void judgeLineSpace() {
    if (lineSpacingMultiplier < 1.2f) {
      lineSpacingMultiplier = 1.2f;
    } else if (lineSpacingMultiplier > 2.0f) {
      lineSpacingMultiplier = 2.0f;
    }
  }

  private void initWheelView(Context context) {
    this.context = context;
    handler = new MessageHandler(EventRunner.create(), this);
//        gestureDetector = new GestureDetector(context, new WheelViewGestureListener(this));
//        gestureDetector.setIsLongpressEnabled(false);
    setDraggedListener(DRAG_VERTICAL, new WheelViewGestureListener(this));
    isLoop = true;

    totalScrollY = 0;
    initPosition = -1;

    initPaints();

  }

  private void initPaints() {
    paintOuterText = new Paint();
    paintOuterText.setColor(new Color(textColorOut));
    paintOuterText.setAntiAlias(true);
    paintOuterText.setFont(typeface);
    paintOuterText.setTextSize(textSize);

    paintCenterText = new Paint();
    paintCenterText.setColor(new Color(textColorCenter));
    paintCenterText.setAntiAlias(true);
    paintCenterText.setFont(typeface);
    paintCenterText.setTextSize(textSize);

    paintLine = new Paint();
    paintLine.setColor(new Color(dividerColor));
    paintLine.setAntiAlias(true);
    if (lineConfig != null) {
      paintLine.setColor(new Color(lineConfig.getColor()));
      paintLine.setAlpha(lineConfig.getAlpha());
      paintLine.setStrokeWidth(lineConfig.getThick());
    }
  }

  private void remeasure() {//重新测量
    if (adapter == null) {
      return;
    }
//        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
    measureTextWidthHeight();

    //半圆的周长 = item高度乘以item数目-1
    halfCircumference = (int) (itemHeight * (itemsVisible - 1));
    //整个圆的周长除以PI得到直径，这个直径用作控件的总高度
    measuredHeight = (int) ((halfCircumference * 2) / Math.PI);
    //求出半径
    radius = (int) (halfCircumference / Math.PI);
    //控件宽度，这里支持weight
    //控件宽度，这里支持weight
    measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
//        int diffWidth = 10;
//        measuredWidth = maxTextWidth+diffWidth;//加上diffWidth 线的长度就比文本长diffWidth
//        measuredWidth = measureSize(widthMode,widthSize,measuredWidth);
    //计算两条横线 和 选中项画笔的基线Y位置
    firstLineY = (measuredHeight - itemHeight) / 2.0F;
    secondLineY = (measuredHeight + itemHeight) / 2.0F;
    centerY = secondLineY - (itemHeight - maxTextHeight) / 2.0f - centerContentOffset;

    //初始化显示的item的position
    if (initPosition == -1) {
      if (isLoop) {
        initPosition = (adapter.getItemsCount() + 1) / 2;
      } else {
        initPosition = 0;
      }
    }
    preCurrentIndex = initPosition;
  }

  private int measureSize(int mode, int sizeExpect, int sizeActual) {
//        MeasureSpec.getMode()方法返回的结果有三种：
//        UNSPECIFIED：父节点对子节点的大小没有任何要求。
//        EXACTLY: 父节点要求其子节点的大小指定为某个确切的值。其子节点以及其他子孙节点都需要适应该大小。
//        AT MOST：父节点要求其子节点的大小不能超过某个最大值，其子节点以及其他子孙节点的大小都需要小于这个值
    int realSize;
    if (mode == EstimateSpec.PRECISE) {
      realSize = sizeExpect;
    } else {
      realSize = sizeActual;
      if (mode == EstimateSpec.NOT_EXCEED)
        realSize = Math.min(realSize, sizeExpect);
    }
    return realSize;
  }

  /**
   * 计算最大length的Text的宽高度
   */
  private void measureTextWidthHeight() {
    Rect rect;
    for (int i = 0; i < adapter.getItemsCount(); i++) {
      String s1 = getContentText(adapter.getItem(i));
      rect = paintCenterText.getTextBounds(s1);

      int textWidth = rect.getWidth();

      if (textWidth > maxTextWidth) {
        maxTextWidth = textWidth;
      }
      rect = paintCenterText.getTextBounds("\u661F\u671F"); // 星期的字符编码（以它为标准高度）

      maxTextHeight = rect.getHeight() + 2;

    }
    itemHeight = lineSpacingMultiplier * maxTextHeight;
  }

  public void smoothScroll(ACTION action) {//平滑滚动的实现
    cancelFuture();
    if (action == ACTION.FLING || action == ACTION.DRAG) {
      mOffset = (int) ((totalScrollY % itemHeight + itemHeight) % itemHeight);
      if ((float) mOffset > itemHeight / 2.0F) {//如果超过Item高度的一半，滚动到下一个Item去
        mOffset = (int) (itemHeight - (float) mOffset);
      } else {
        mOffset = -mOffset;
      }
    }
    //停止的时候，位置有偏移，不是全部都能正确停止到中间位置的，这里把文字位置挪回中间去
    mFuture = mExecutor.scheduleWithFixedDelay(new SmoothScrollTimerTask(this, mOffset), 0, 10, TimeUnit.MILLISECONDS);
  }

  public final void scrollBy(float velocityY) {//滚动惯性的实现
    cancelFuture();
    mFuture = mExecutor.scheduleWithFixedDelay(new InertiaTimerTask(this, velocityY), 0, VELOCITY_FLING, TimeUnit.MILLISECONDS);
  }

  public void cancelFuture() {
    if (mFuture != null && !mFuture.isCancelled()) {
      mFuture.cancel(true);
      mFuture = null;
    }
  }

  /**
   * 设置是否循环滚动
   *
   * @param canLoop 是否循环
   */
  public final void setCanLoop(boolean canLoop) {
    isLoop = canLoop;
  }

  public final void setTypeface(Font font) {
    typeface = font;
    paintOuterText.setFont(typeface);
    paintCenterText.setFont(typeface);
  }

  public final void setTextSize(float size) {
    if (size > 0.0F) {
      textSize = (int) (density * size);
      paintOuterText.setTextSize(textSize);
      paintCenterText.setTextSize(textSize);
    }
  }

  public final void setCurrentItem(int currentItem) {
    this.initPosition = currentItem;
    totalScrollY = 0;//回归顶部，不然重设setCurrentItem的话位置会偏移的，就会显示出不对位置的数据
    invalidate();
  }

  public final void setOnItemPickListener(OnItemPickListener onItemPickListener) {
    this.onItemPickListener = onItemPickListener;
  }

  public final void setAdapter(WheelAdapter adapter) {
    this.adapter = adapter;
    remeasure();
    invalidate();
  }

  public final WheelAdapter getAdapter() {
    return adapter;
  }

  public final int getCurrentPosition() {
    return selectedPosition;
  }

  public final String getCurrentItem() {
    selectedItem = (String) adapter.getItem(selectedPosition);
    return selectedItem;
  }

  //handler 里调用
  public final void onItemPicked() {
    if (onItemPickListener != null) {
      handler.postTask(new OnItemPickedRunnable(this, onItemPickListener), 200L);
    }
  }

  @Override
  public void onDraw(Component component, Canvas canvas) {
    if (adapter == null) {
      return;
    }
    //可见的item数组
    String[] drawItemCount = new String[itemsVisible];
    //滚动的Y值高度除去每行Item的高度，得到滚动了多少个item，即change数
    change = (int) (totalScrollY / itemHeight);
    try {
      //滚动中实际的预选中的item(即经过了中间位置的item) ＝ 滑动前的位置 ＋ 滑动相对位置
      preCurrentIndex = initPosition + change % adapter.getItemsCount();
    } catch (ArithmeticException e) {
      LogUtils.error("WheelView", "出错了！adapter.getItemsCount() == 0，联动数据不匹配");
    }
    if (!isLoop) { //不循环的情况
      if (preCurrentIndex < 0) {
        preCurrentIndex = 0;
      }
      if (preCurrentIndex > adapter.getItemsCount() - 1) {
        preCurrentIndex = adapter.getItemsCount() - 1;
      }
    } else {//循环
      if (preCurrentIndex < 0) {//举个例子：如果总数是5，preCurrentIndex ＝ －1，那么preCurrentIndex按循环来说，其实是0的上面，也就是4的位置
        preCurrentIndex = adapter.getItemsCount() + preCurrentIndex;
      }
      if (preCurrentIndex > adapter.getItemsCount() - 1) {//同理上面,自己脑补一下
        preCurrentIndex = preCurrentIndex - adapter.getItemsCount();
      }
    }
    //跟滚动流畅度有关，总滑动距离与每个item高度取余，即并不是一格格的滚动，每个item不一定滚到对应Rect里的，这个item对应格子的偏移值
    float itemHeightOffset = (totalScrollY % itemHeight);

    // 设置数组中每个元素的值
    int counter = 0;
    while (counter < itemsVisible) {
      int index = preCurrentIndex - (itemsVisible / 2 - counter);//索引值，即当前在控件中间的item看作数据源的中间，计算出相对源数据源的index值
      //判断是否循环，如果是循环数据源也使用相对循环的position获取对应的item值，如果不是循环则超出数据源范围使用""空白字符串填充，在界面上形成空白无数据的item项
      if (isLoop) {
        index = getLoopMappingIndex(index);
        drawItemCount[counter] = adapter.getItem(index);
      } else if (index < 0) {
        drawItemCount[counter] = "";
      } else if (index > adapter.getItemsCount() - 1) {
        drawItemCount[counter] = "";
      } else {
        drawItemCount[counter] = adapter.getItem(index);
      }

      counter++;

    }
    //设置线可见时绘制两条线
    if (lineConfig != null && lineConfig.isVisible()) {
//            float ratio  = lineConfig.getRatio();
      //绘制中间两条横线
      if (dividerType == LineConfig.DividerType.WRAP) {//横线长度仅包裹内容
        float startX;
        float endX;

        if (TextTool.isNullOrEmpty(label)) {//隐藏Label的情况
          startX = (measuredWidth - maxTextWidth) / 2 - 12;
        } else {
          startX = (measuredWidth - maxTextWidth) / 4 - 12;
        }

        if (startX <= 0) {//如果超过了WheelView的边缘
          startX = 10;
        }
        endX = measuredWidth - startX;
        canvas.drawLine(startX, firstLineY, endX, firstLineY, paintLine);
        canvas.drawLine(startX, secondLineY, endX, secondLineY, paintLine);
      } else {
        canvas.drawLine(0.0F, firstLineY, measuredWidth, firstLineY, paintLine);
        canvas.drawLine(0.0F, secondLineY, measuredWidth, secondLineY, paintLine);
      }
    }
    //只显示选中项Label文字的模式，并且Label文字不为空，则进行绘制
    if (onlyShowCenterLabel && !TextTool.isNullOrEmpty(label)) {
      //绘制文字，靠右并留出空隙
      int drawRightContentStart = measuredWidth - getTextWidth(paintCenterText, label);
      canvas.drawText(paintCenterText, label, drawRightContentStart - centerContentOffset, centerY);
//            canvas.drawText(label, drawCenterContentStart+maxTextWidth, centerY, paintCenterText);
    }
    //只显示选中项Label文字的模式，并且Label文字不为空，则进行绘制
//        if (!TextUtils.isEmpty(label)&& isCenterLabel) {
//            int labelStart = measureContentStart(paintCenterText,label);
////                    Log.e("width:",""+maxTextWidth);
////                    Log.e("drawCenterContentStart:",""+drawCenterContentStart);
////                    Log.e("drawOutContentStart:",""+drawOutContentStart);
////                    Log.e("labelStart:",""+labelStart);
//            //绘制文字，靠右并留出空隙
////                    int drawRightContentStart = measuredWidth - getTextWidth(paintCenterText, label);
//            canvas.drawText(label, labelStart+maxTextWidth+labelSpace, centerY, paintCenterText);
//        }
    counter = 0;
    while (counter < itemsVisible) {
      canvas.save();
      // 弧长 L = itemHeight * counter - itemHeightOffset
      // 求弧度 α = L / r  (弧长/半径) [0,π]
      double radian = ((itemHeight * counter - itemHeightOffset)) / radius;
      // 弧度转换成角度(把半圆以Y轴为轴心向右转90度，使其处于第一象限及第四象限
      // angle [-90°,90°]
      float angle = (float) (90D - (radian / Math.PI) * 180D);//item第一项,从90度开始，逐渐递减到 -90度

      // 计算取值可能有细微偏差，保证负90°到90°以外的不绘制
      if (angle >= 90F || angle <= -90F) {
        canvas.restore();
      } else {
        //获取内容文字
        String contentText;
        //如果是label每项都显示的模式，并且item内容不为空、label 也不为空
        if (!onlyShowCenterLabel && !TextTool.isNullOrEmpty(label) && !TextTool.isNullOrEmpty(getContentText(drawItemCount[counter]))) {
          //每个item 加label
          contentText = getContentText(drawItemCount[counter]) + label;
        } else {
          contentText = getContentText(drawItemCount[counter]);
        }

        reMeasureTextSize(contentText);
        //计算开始绘制的位置
//                measuredCenterContentStart(contentText);
//                measuredOutContentStart(contentText);
        drawCenterContentStart = measureContentStart(paintCenterText, contentText);
        drawOutContentStart = measureContentStart(paintOuterText, contentText);

        float translateY = (float) (radius - Math.cos(radian) * radius - (Math.sin(radian) * maxTextHeight) / 2D);
        //根据Math.sin(radian)来更改canvas坐标系原点，然后缩放画布，使得文字高度进行缩放，形成弧形3d视觉差
        canvas.translate(0.0F, translateY);
        canvas.scale(1.0F, (float) Math.sin(radian));
        if (translateY <= firstLineY && maxTextHeight + translateY >= firstLineY) {
          // 条目经过第一条线
          canvas.save();
          canvas.clipRect(0, 0, measuredWidth, firstLineY - translateY);
          canvas.scale(1.0F, (float) Math.sin(radian) * SCALE_CONTENT);
          canvas.drawText(paintOuterText, contentText, drawOutContentStart, maxTextHeight);
          canvas.restore();
          canvas.save();
          canvas.clipRect(0, firstLineY - translateY, measuredWidth, (int) (itemHeight));
          canvas.scale(1.0F, (float) Math.sin(radian) * 1.0F);
          canvas.drawText(paintCenterText, contentText, drawCenterContentStart, maxTextHeight - centerContentOffset);
          canvas.restore();
        } else if (translateY <= secondLineY && maxTextHeight + translateY >= secondLineY) {
          // 条目经过第二条线
          canvas.save();
          canvas.clipRect(0, 0, measuredWidth, secondLineY - translateY);
          canvas.scale(1.0F, (float) Math.sin(radian) * 1.0F);
          canvas.drawText(paintCenterText, contentText, drawCenterContentStart, maxTextHeight - centerContentOffset);
          canvas.restore();
          canvas.save();
          canvas.clipRect(0, secondLineY - translateY, measuredWidth, (int) (itemHeight));
          canvas.scale(1.0F, (float) Math.sin(radian) * SCALE_CONTENT);
          canvas.drawText(paintOuterText, contentText, drawOutContentStart, maxTextHeight);
          canvas.restore();
        } else if (translateY >= firstLineY && maxTextHeight + translateY <= secondLineY) {
          // 中间条目
          //canvas.clipRect(0, 0, measuredWidth,   maxTextHeight);
          //让文字居中
          float Y = maxTextHeight - centerContentOffset;//因为圆弧角换算的向下取值，导致角度稍微有点偏差，加上画笔的基线会偏上，因此需要偏移量修正一下
//                    if (onlyShowCenterLabel && !TextUtils.isEmpty(label)) {
//                        contentText += label;
//                    }
          canvas.drawText(paintCenterText, contentText, drawCenterContentStart, Y);
          selectedPosition = adapter.indexOf(drawItemCount[counter]);

        } else {
          // 其他条目
          canvas.save();
          canvas.clipRect(0, 0, measuredWidth, (int) (itemHeight));
          canvas.scale(1.0F, (float) Math.sin(radian) * SCALE_CONTENT);
          canvas.drawText(paintOuterText, contentText, drawOutContentStart, maxTextHeight);
          canvas.restore();
        }
        canvas.restore();
        paintCenterText.setTextSize(textSize);
      }
      counter++;
    }

  }

  /**
   * 根据文字的长度 重新设置文字的大小 让其能完全显示
   *
   * @param contentText
   */
  private void reMeasureTextSize(String contentText) {
    Rect rect;
    rect = paintCenterText.getTextBounds(contentText);
    int width = rect.getWidth();
    int size = textSize;
    while (width > measuredWidth) {
      size--;
      //设置2条横线中间的文字大小
      paintCenterText.setTextSize(size);
      rect = paintCenterText.getTextBounds(contentText);
      width = rect.getWidth();
    }
    //设置2条横线外面的文字大小
    paintOuterText.setTextSize(size);
  }


  //递归计算出对应的index
  private int getLoopMappingIndex(int index) {
    if (index < 0) {
      index = index + adapter.getItemsCount();
      index = getLoopMappingIndex(index);
    } else if (index > adapter.getItemsCount() - 1) {
      index = index - adapter.getItemsCount();
      index = getLoopMappingIndex(index);
    }
    return index;
  }

  /**
   * 根据传进来的对象获取getPickerViewText()方法，来获取需要显示的值
   *
   * @param item 数据源的item
   * @return 对应显示的字符串
   */
  private String getContentText(Object item) {
    if (item == null) {
      return "";
    } else if (item instanceof IPickerViewData) {
      return ((IPickerViewData) item).getPickerViewText();
    } else if (item instanceof Integer) {
      //如果为整形则最少保留两位数.
      return String.format(Locale.getDefault(), "%02d", (int) item);
    }
    return item.toString();
  }

  /**
   * 根据文本width动态画出文本的左右padding
   */
  private int measureContentStart(Paint paint, String text) {
    int baselineX = (measuredWidth - measureTextWidth(paint, text)) / 2;
    return baselineX - 4;
  }

  /**
   * 动态获取文本width
   */
  private int measureTextWidth(Paint paint, String text) {
    Rect rect = getRect(paint, text);
    int textWidth = rect.getWidth();
    if (textWidth > maxTextWidth) {
      maxTextWidth = textWidth;
      return maxTextWidth;
    }
    return textWidth;
  }

  private Rect getRect(Paint paint, String text) {
    Rect rect;
    rect = paint.getTextBounds(text);
    return rect;
  }

  private void measuredCenterContentStart(String content) {
    Rect rect;
    rect = paintCenterText.getTextBounds(content);
    switch (mGravity) {
      case CENTER://显示内容居中
        if (isOptions || label == null || label.equals("") || !onlyShowCenterLabel) {
          drawCenterContentStart = (int) ((measuredWidth - rect.getWidth()) * 0.5) - 4;
        } else {//只显示中间label时，时间选择器内容偏左一点，留出空间绘制单位标签
          drawCenterContentStart = (int) ((measuredWidth - rect.getWidth()) * 0.25) - 4;
        }
        break;
      case LEFT:
        drawCenterContentStart = 0;
        break;
      case RIGHT://添加偏移量
        drawCenterContentStart = measuredWidth - rect.getWidth() - (int) centerContentOffset;
        break;
    }
  }

  private void measuredOutContentStart(String content) {
    Rect rect;
    rect = paintOuterText.getTextBounds(content);
    switch (mGravity) {
      case CENTER:
        if (isOptions || label == null || label.equals("") || !onlyShowCenterLabel) {
          drawOutContentStart = (int) ((measuredWidth - rect.getWidth()) * 0.5);
        } else {//只显示中间label时，时间选择器内容偏左一点，留出空间绘制单位标签
          drawOutContentStart = (int) ((measuredWidth - rect.getWidth()) * 0.25);
        }
        break;
      case LEFT:
        drawOutContentStart = 0;
        break;
      case RIGHT:
        drawOutContentStart = measuredWidth - rect.getWidth() - (int) centerContentOffset;
        break;
    }
  }

  @Override
  public boolean onEstimateSize(int widthMeasureSpec, int heightMeasureSpec) {
    this.widthMeasureSpec = widthMeasureSpec;
    this.heightMeasureSpec = heightMeasureSpec;
    remeasure();
    setEstimatedSize(EstimateSpec.getChildSizeWithMode(measuredWidth, widthMeasureSpec, EstimateSpec.NOT_EXCEED),
        EstimateSpec.getChildSizeWithMode(measuredHeight, heightMeasureSpec, EstimateSpec.NOT_EXCEED));
    return true;
  }

  /**
   * MotionEvent has no getRawX(int) method; simulate it pending future API approval.
   *
   * @param event
   * @param pointerIndex
   * @return float
   */
  protected static float getRawX(TouchEvent event, int pointerIndex) {
    BigDecimal bignum1 = new BigDecimal(event.getPointerScreenPosition(event.getIndex()).getX());
    BigDecimal bignum2 = new BigDecimal(event.getPointerPosition(event.getIndex()).getX());
    float viewToRaw = bignum1.subtract(bignum2).floatValue();
    if (pointerIndex < event.getPointerCount()) {
      BigDecimal bignum3 = new BigDecimal(event.getPointerPosition(pointerIndex).getX());
      BigDecimal bignum4 = new BigDecimal(viewToRaw);
      return bignum3.add(bignum4).floatValue();
    }
    return 0f;
  }

  /**
   * MotionEvent has no getRawY(int) method; simulate it pending future API approval.
   *
   * @param event        event
   * @param pointerIndex pointerIndex
   * @return float
   */
  protected static float getRawY(TouchEvent event, int pointerIndex) {
    BigDecimal bignum1 = new BigDecimal(event.getPointerScreenPosition(event.getIndex()).getY());
    BigDecimal bignum2 = new BigDecimal(event.getPointerPosition(event.getIndex()).getY());
    float viewToRaw = bignum1.subtract(bignum2).floatValue();
    if (pointerIndex < event.getPointerCount()) {
      BigDecimal bignum3 = new BigDecimal(event.getPointerPosition(pointerIndex).getY());
      BigDecimal bignum4 = new BigDecimal(viewToRaw);
      return bignum3.add(bignum4).floatValue();
    }
    return 0f;
  }

  @Override
  public boolean onTouchEvent(Component component, TouchEvent event) {

//        boolean eventConsumed = gestureDetector.onTouchEvent(event);
    switch (event.getAction()) {
      //按下
      case TouchEvent.PRIMARY_POINT_DOWN:
        startTime = System.currentTimeMillis();
        cancelFuture();
        previousY = getRawY(event, 0);
        break;
      //滑动中
      case TouchEvent.POINT_MOVE:

        float dy = previousY - getRawY(event, 0);
        previousY = getRawY(event, 0);
        totalScrollY = totalScrollY + dy;

        // 边界处理。
        if (!isLoop) {
          float top = -initPosition * itemHeight;
          float bottom = (adapter.getItemsCount() - 1 - initPosition) * itemHeight;


          if (totalScrollY - itemHeight * 0.25 < top) {
            top = totalScrollY - dy;
          } else if (totalScrollY + itemHeight * 0.25 > bottom) {
            bottom = totalScrollY - dy;
          }

          if (totalScrollY < top) {
            totalScrollY = (int) top;
          } else if (totalScrollY > bottom) {
            totalScrollY = (int) bottom;
          }
        }
        break;
      //完成滑动，手指离开屏幕
      case TouchEvent.PRIMARY_POINT_UP:

      default:
//                if (!eventConsumed) {//未消费掉事件
//
//                    /**
//                     * TODO<关于弧长的计算>
//                     *
//                     * 弧长公式： L = α*R
//                     * 反余弦公式：arccos(cosα) = α
//                     * 由于之前是有顺时针偏移90度，
//                     * 所以实际弧度范围α2的值 ：α2 = π/2-α    （α=[0,π] α2 = [-π/2,π/2]）
//                     * 根据正弦余弦转换公式 cosα = sin(π/2-α)
//                     * 代入，得： cosα = sin(π/2-α) = sinα2 = (R - y) / R
//                     * 所以弧长 L = arccos(cosα)*R = arccos((R - y) / R)*R
//                     */
//
//                    float y = event.getY();
//                    double L = Math.acos((radius - y) / radius) * radius;
//                    //item0 有一半是在不可见区域，所以需要加上 itemHeight / 2
//                    int circlePosition = (int) ((L + itemHeight / 2) / itemHeight);
//                    float extraOffset = (totalScrollY % itemHeight + itemHeight) % itemHeight;
//                    //已滑动的弧长值
//                    mOffset = (int) ((circlePosition - itemsVisible / 2) * itemHeight - extraOffset);
//
//                    if ((System.currentTimeMillis() - startTime) > 120) {
//                        // 处理拖拽事件
//                        smoothScroll(ACTION.DRAG);
//                    } else {
//                        // 处理条目点击事件
//                        smoothScroll(ACTION.CLICK);
//                    }
//                }
        break;
    }

    invalidate();
    return true;
  }

  /**
   * 获取Item个数
   *
   * @return item个数
   */
  public int getItemsCount() {
    return adapter != null ? adapter.getItemsCount() : 0;
  }

  /**
   * 附加在右边的单位字符串
   *
   * @param label 单位
   */
  public void setLabel(String label) {
    this.label = label;
  }

  /**
   * 附加在右边的单位字符串
   */
  public final void setLabel(String label, boolean onlyShowCenterLabel) {
    this.label = label;
    this.onlyShowCenterLabel = onlyShowCenterLabel;
  }

  public void setGravity(int gravity) {
    this.mGravity = gravity;
  }

  public int getTextWidth(Paint paint, String str) {//计算文字宽度
    int iRet = 0;
    if (str != null && str.length() > 0) {
      int len = str.length();
      float[] widths = new float[len];
      paint.getAdvanceWidths(str, widths);
      for (int j = 0; j < len; j++) {
        iRet += (int) Math.ceil(widths[j]);
      }
    }
    return iRet;
  }

  //
  private void setIsOptions(boolean options) {
    isOptions = options;
  }

  public void setLineConfig(LineConfig lineConfig) {
    if (null != lineConfig) {
      paintLine.setColor(new Color(lineConfig.getColor()));
      paintLine.setAlpha(lineConfig.getAlpha());
      paintLine.setStrokeWidth(lineConfig.getThick());
      this.lineConfig = lineConfig;
    }

  }

  public void setUnSelectedTextColor(int textColorOut) {
    if (textColorOut != 0) {
      this.textColorOut = textColorOut;
      paintOuterText.setColor(new Color(this.textColorOut));
    }
  }

  public void setSelectedTextColor(int textColorCenter) {
    if (textColorCenter != 0) {
      this.textColorCenter = textColorCenter;
      paintCenterText.setColor(new Color(this.textColorCenter));
    }
  }

  public void setDividerColor(int dividerColor) {
    if (dividerColor != 0) {
      this.dividerColor = dividerColor;
      paintLine.setColor(new Color(this.dividerColor));
    }
  }

  public void setDividerType(LineConfig.DividerType dividerType) {
    this.dividerType = dividerType;
  }

  public void setLineSpacingMultiplier(float lineSpacingMultiplier) {
    if (lineSpacingMultiplier != 0) {


      this.lineSpacingMultiplier = lineSpacingMultiplier;
      judgeLineSpace();

    }
  }


}