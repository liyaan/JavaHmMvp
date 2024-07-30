package com.example.utils.ohospickers.picker;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import com.example.utils.ohospickers.adapter.ArrayWheelAdapter;
import com.example.utils.ohospickers.listeners.OnItemPickListener;
import com.example.utils.ohospickers.listeners.OnSingleWheelListener;
import com.example.utils.ohospickers.util.ConvertUtils;
import com.example.utils.ohospickers.widget.WheelView;
import ohos.aafwk.ability.Ability;

import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.Text;

import ohos.agp.utils.Color;
import ohos.agp.utils.TextTool;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import static ohos.agp.utils.LayoutAlignment.CENTER;

/**
 * 单项选择器
 */
public class SinglePicker<T> extends WheelPicker {
  private static final int ITEM_WIDTH_UNKNOWN = -99;
  private List<T> items = new ArrayList<>();
  private List<String> itemStrings = new ArrayList<>();
  private WheelView wheelView;
  private float weightWidth = 0.0f;
  private OnSingleWheelListener onSingleWheelListener;
  private OnItemPickListener<T> onItemPickListener;
  private int selectedItemIndex = 0;
  private String selectedItem = "";
  private String label = "";
  private int itemWidth = ITEM_WIDTH_UNKNOWN;
  private int size = 55;

  public SinglePicker(Ability activity, T[] items) {
    this(activity, Arrays.asList(items));
  }

  public SinglePicker(Ability activity, List<T> items) {
    super(activity);
    setItems(items);
  }

  /**
   * 添加数据项
   */
  public void addItem(T item) {
    items.add(item);
    itemStrings.add(formatToString(item));
  }

  /**
   * 移除数据项
   */
  public void removeItem(T item) {
    items.remove(item);
    itemStrings.remove(formatToString(item));
  }

  /**
   * 设置数据项
   */
  public void setItems(T[] items) {
    setItems(Arrays.asList(items));
  }

  /**
   * 设置数据项
   */
  public void setItems(List<T> items) {
    if (null == items || items.size() == 0) {
      return;
    }
    this.items = items;
    for (T item : items) {
      itemStrings.add(formatToString(item));
    }
    if (null != wheelView) {
      wheelView.setAdapter(new ArrayWheelAdapter<>(itemStrings));
      wheelView.setCurrentItem(selectedItemIndex);
    }

  }

  /**
   * 设置显示的单位，如身高为cm、体重为kg
   */
  public void setLabel(String label) {
    this.label = label;
  }

  /**
   * 设置默认选中的项的索引
   */
  public void setSelectedIndex(int index) {
    if (index >= 0 && index < items.size()) {
      selectedItemIndex = index;
    }
  }

  /**
   * 设置默认选中的项
   */
  public void setSelectedItem(@NotNull T item) {
    setSelectedIndex(itemStrings.indexOf(formatToString(item)));
  }

  /**
   * 设置view的权重，总权重数为1 ,weightWidth范围（0.0f-1.0f）
   */
  public void setWeightWidth(@Range(from = 0, to = 1) float weightWidth) {
    if (weightWidth < 0) {
      weightWidth = 0;
    }
    if (!TextTool.isNullOrEmpty(label)) {
      if (weightWidth >= 1) {
        weightWidth = 0.5f;
      }
    }
    this.weightWidth = weightWidth;
  }

  /**
   * 设置选项的宽(dp)
   */
  public void setItemWidth(int itemWidth) {
    if (null != wheelView) {
      int width = ConvertUtils.toPx(activity, itemWidth);
      wheelView.setLayoutConfig(new DirectionalLayout.LayoutConfig(width, wheelView.getLayoutConfig().height));
    } else {
      this.itemWidth = itemWidth;
    }
  }

  /**
   * 设置滑动监听器
   */
  public void setOnSingleWheelListener(OnSingleWheelListener onSingleWheelListener) {
    this.onSingleWheelListener = onSingleWheelListener;
  }

  public void setOnItemPickListener(OnItemPickListener<T> listener) {
    this.onItemPickListener = listener;
  }

  @Override
  protected Component makeCenterView() {
    if (items.size() == 0) {
      throw new IllegalArgumentException("please initial items at first, can't be empty");
    }

    DirectionalLayout layout = new DirectionalLayout(activity);
    layout.setLayoutConfig(new ComponentContainer.LayoutConfig(MATCH_PARENT, WRAP_CONTENT));
    layout.setOrientation(DirectionalLayout.HORIZONTAL);
    layout.setAlignment(CENTER);
    DirectionalLayout.LayoutConfig wheelParams = new DirectionalLayout.LayoutConfig(WRAP_CONTENT, WRAP_CONTENT);
    if (weightEnable) {
      //按权重分配宽度
      wheelParams = new DirectionalLayout.LayoutConfig(0, WRAP_CONTENT);
      wheelParams.weight = 1;
    }
    wheelView = new WheelView(activity);
    wheelView.setAdapter(new ArrayWheelAdapter<>(itemStrings));
    wheelView.setCurrentItem(selectedItemIndex);
    wheelView.setCanLoop(canLoop);
    wheelView.setTextSize(textSize);
    wheelView.setSelectedTextColor(textColorFocus);
    wheelView.setUnSelectedTextColor(textColorNormal);
    wheelView.setLineConfig(lineConfig);
    wheelView.setLayoutConfig(wheelParams);
    wheelView.setOnItemPickListener(new OnItemPickListener<String>() {
      @Override
      public void onItemPicked(int i, String item) {
        selectedItem = item;
        selectedItemIndex = i;
        if (onSingleWheelListener != null) {
          onSingleWheelListener.onWheeled(selectedItemIndex, selectedItem);
        }
      }
    });
    layout.addComponent(wheelView);
    if (!TextTool.isNullOrEmpty(label)) {
      if (isOuterLabelEnable()) {
        Text labelView = new Text(activity);
        labelView.setLayoutConfig(new DirectionalLayout.LayoutConfig(WRAP_CONTENT, WRAP_CONTENT));
        labelView.setTextColor(new Color(textColorFocus));
        labelView.setTextSize(size);
        labelView.setText(label);
        layout.addComponent(labelView);
      } else {
        wheelView.setLabel(label, false);
      }
    }
    if (itemWidth != ITEM_WIDTH_UNKNOWN) {
      int width = ConvertUtils.toPx(activity, itemWidth);
      wheelView.setLayoutConfig(new DirectionalLayout.LayoutConfig(width, wheelView.getLayoutConfig().height));
    }

    return layout;
  }

  protected String formatToString(T item) {
    if (item instanceof Float || item instanceof Double) {
      return new DecimalFormat("0.00").format(item);
    }
    return item.toString();
  }

  @Override
  public void onSubmit() {
    if (onItemPickListener != null) {
      onItemPickListener.onItemPicked(getSelectedIndex(), getSelectedItem());
    }
  }

  private T getSelectedItem() {
    return items.get(selectedItemIndex);
  }

  public int getSelectedIndex() {
    return selectedItemIndex;
  }

//    public View getWheelListView() {
//        return wheelListView;
//    }

}
