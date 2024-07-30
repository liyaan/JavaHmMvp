package com.example.utils.ohospickers.picker;


import com.example.utils.ohospickers.common.ConfirmDialog;
import com.example.utils.ohospickers.common.LineConfig;
import ohos.aafwk.ability.Ability;
import ohos.agp.components.Component;
import org.jetbrains.annotations.Range;

/**
 * 滑轮选择器
 */
public abstract class WheelPicker extends ConfirmDialog<Component> {
  protected int textSize = 16;
  protected int textColorNormal = 0XFFBBBBBB;
  protected int textColorFocus = 0XFF0288CE;
  protected int offset = 2;
  //是否是自己布局添加的label   控制分割线是否连接到一起 成一条直线   false的时候 最好分割线是填充 LineConfig.DividerType.WRAP
  protected boolean outerLabelEnable = true;
  protected boolean canLoop = true;//是否循环
  protected boolean onlyCenterLabel = false;//只有中间才显示label
  protected boolean weightEnable = false;//启用权重
  protected boolean canLinkage = false;//是否联动
  protected LineConfig lineConfig;
  private Component contentView;

  public WheelPicker(Ability activity) {
    super(activity);
  }

  public boolean isOuterLabelEnable() {
    return outerLabelEnable;
  }

  public void setOuterLabelEnable(boolean outerLabelEnable) {
    this.outerLabelEnable = outerLabelEnable;
  }

  /**
   * 设置文字大小
   */
  public void setTextSize(int textSize) {
    this.textSize = textSize;
  }

  /**
   * 设置未选中文字颜色
   */
  public void setUnSelectedTextColor(int unSelectedTextColor) {
    this.textColorNormal = unSelectedTextColor;
  }

  /**
   * 设置选中文字颜色
   */
  public void setSelectedTextColor(int selectedTextColor) {
    this.textColorFocus = selectedTextColor;
  }

  public boolean isOnlyCenterLabel() {
    return onlyCenterLabel;
  }

  public void setOnlyCenterLabel(boolean onlyCenterLabel) {
    this.onlyCenterLabel = onlyCenterLabel;
  }

  /**
   * 设置分隔线是否可见
   */
  public void setLineVisible(boolean lineVisible) {
    if (null == lineConfig) {
      lineConfig = new LineConfig();
    }
    lineConfig.setVisible(lineVisible);
  }

  /**
   * 设置分隔阴影是否可见
   * 暂时去掉此功能
   */
//    public void setShadowVisible(boolean shadowVisible) {
//        if (null == lineConfig) {
//            lineConfig = new LineConfig();
//        }
//        lineConfig.setShadowVisible(shadowVisible);
//    }

  /**
   * 设置是否自动联动
   */
  public void setCanLinkage(boolean canLinkage) {
    this.canLinkage = canLinkage;
  }

  /**
   * 设置分隔线颜色
   */
  public void setLineColor(int lineColor) {
    if (null == lineConfig) {
      lineConfig = new LineConfig();
    }
    lineConfig.setVisible(true);
    lineConfig.setColor(lineColor);
  }

  /**
   * 设置分隔线配置项，设置null将隐藏分割线及阴影
   */
  public void setLineConfig(LineConfig config) {
    if (null == config) {
      lineConfig = new LineConfig();
      lineConfig.setVisible(false);
      lineConfig.setShadowVisible(false);
    } else {
      lineConfig = config;
    }
  }

  /**
   * 设置选项偏移量，可用来要设置显示的条目数，范围为1-3。
   * 1显示3条、2显示5条、3显示7条
   */
  public void setOffset(@Range(from = 1, to = 3) int offset) {
    this.offset = offset;
  }

  /**
   * 设置是否禁用循环
   * true 循环 false 不循环
   */
  public void setCanLoop(boolean canLoop) {
    this.canLoop = canLoop;
  }

  /**
   * 线性布局设置是否启用权重
   * true 启用 false 自适应width
   */
  public void setWeightEnable(boolean weightEnable) {
    this.weightEnable = weightEnable;
  }

  /**
   * 得到选择器视图，可内嵌到其他视图容器
   */
  @Override
  public Component getContentView() {
    if (null == contentView) {
      contentView = makeCenterView();
    }
    return contentView;
  }

}
