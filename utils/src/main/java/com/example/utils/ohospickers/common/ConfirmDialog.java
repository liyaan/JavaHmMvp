package com.example.utils.ohospickers.common;


import com.example.utils.ohospickers.util.ConvertUtils;
import ohos.aafwk.ability.Ability;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.Component;
import ohos.agp.components.DependentLayout;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.Text;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.Color;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.utils.TextAlignment;
import ohos.agp.utils.TextTool;
import org.jetbrains.annotations.Range;

import static ohos.agp.components.Component.*;
import static ohos.agp.components.ComponentContainer.LayoutConfig.MATCH_PARENT;
import static ohos.agp.components.DependentLayout.LayoutConfig.*;
import static ohos.agp.utils.Color.WHITE;
import static ohos.agp.utils.LayoutAlignment.VERTICAL_CENTER;


public abstract class ConfirmDialog<V extends Component> extends BaseDialog<Component> {
  protected boolean topLineVisible = true;
  protected int topLineColor = 0xFFDDDDDD;
  protected int topLineHeight = 1;//dp
  protected int topBackgroundColor = WHITE.getValue();
  protected int topHeight = 40;//dp
  protected int topPadding = 15;//dp
  protected boolean cancelVisible = true;

  public void setActionButtonTop(boolean actionButtonTop) {
    isActionButtonTop = actionButtonTop;
  }

  protected boolean isActionButtonTop = true;//确认取消按钮位置
  protected String cancelText = "";
  protected String submitText = "";
  protected String titleText = "";
  protected Color cancelTextColor = Color.BLACK;
  protected Color submitTextColor = Color.BLACK;
  protected Color titleTextColor = Color.BLACK;
  protected int pressedTextColor = 0XFF0288CE;
  protected int cancelTextSize = 50;
  protected int submitTextSize = 50;
  protected int titleTextSize = 50;
  protected int backgroundColor = WHITE.getValue();
  private Text cancelButton, submitButton;
  private Component titleView;

  public ConfirmDialog(Ability activity) {
    super(activity);
    cancelText = "取消";
    submitText = "确定";
  }

  /**
   * 设置顶部标题栏下划线颜色
   */
  public void setTopLineColor(int topLineColor) {
    this.topLineColor = topLineColor;
  }

  /**
   * 设置顶部标题栏下划线高度，单位为dp
   */
  public void setTopLineHeight(int topLineHeight) {
    this.topLineHeight = topLineHeight;
  }

  /**
   * 设置顶部标题栏背景颜色
   */
  public void setTopBackgroundColor(int topBackgroundColor) {
    this.topBackgroundColor = topBackgroundColor;
  }

  /**
   * 设置顶部标题栏高度（单位为dp）
   */
  public void setTopHeight(@Range(from = 10, to = 80) int topHeight) {
    this.topHeight = topHeight;
  }

  /**
   * 设置顶部按钮左边及右边边距（单位为dp）
   */
  public void setTopPadding(int topPadding) {
    this.topPadding = topPadding;
  }

  /**
   * 设置顶部标题栏下划线是否显示
   */
  public void setTopLineVisible(boolean topLineVisible) {
    this.topLineVisible = topLineVisible;
  }

  /**
   * 设置顶部标题栏取消按钮是否显示
   */
  public void setCancelVisible(boolean cancelVisible) {
    if (null != cancelButton) {
      cancelButton.setVisibility(cancelVisible ? VISIBLE : Component.HIDE);
    } else {
      this.cancelVisible = cancelVisible;
    }
  }

  /**
   * 设置顶部标题栏取消按钮文字
   */
  public void setCancelText(String cancelText) {
    if (null != cancelButton) {
      cancelButton.setText(cancelText);
    } else {
      this.cancelText = cancelText;
    }
  }

  /**
   * 设置顶部标题栏取消按钮文字
   */
  public void setCancelText(int textRes) {
    setCancelText(activity.getString(textRes));
  }

  /**
   * 设置顶部标题栏确定按钮文字
   */
  public void setSubmitText(String submitText) {
    if (null != submitButton) {
      submitButton.setText(submitText);
    } else {
      this.submitText = submitText;
    }
  }

  /**
   * 设置顶部标题栏确定按钮文字
   */
  public void setSubmitText(int textRes) {
    setSubmitText(activity.getString(textRes));
  }

  /**
   * 设置顶部标题栏标题文字
   */
  public void setTitleText(String titleText) {
    if (titleView != null && titleView instanceof Text) {
      ((Text) titleView).setText(titleText);
    } else {
      this.titleText = titleText;
    }
  }

  /**
   * 设置顶部标题栏标题文字
   */
  public void setTitleText(int textRes) {
    setTitleText(activity.getString(textRes));
  }

  /**
   * 设置顶部标题栏取消按钮文字颜色
   */
  public void setCancelTextColor(Color cancelTextColor) {
    if (null != cancelButton) {
      cancelButton.setTextColor(cancelTextColor);
    } else {
      this.cancelTextColor = cancelTextColor;
    }
  }

  /**
   * 设置顶部标题栏确定按钮文字颜色
   */
  public void setSubmitTextColor(Color submitTextColor) {
    if (null != submitButton) {
      submitButton.setTextColor(submitTextColor);
    } else {
      this.submitTextColor = submitTextColor;
    }
  }

  /**
   * 设置顶部标题栏标题文字颜色
   */
  public void setTitleTextColor(Color titleTextColor) {
    if (null != titleView && titleView instanceof Text) {
      ((Text) titleView).setTextColor(titleTextColor);
    } else {
      this.titleTextColor = titleTextColor;
    }
  }

  /**
   * 设置按下时的文字颜色
   */
  public void setPressedTextColor(int pressedTextColor) {
    this.pressedTextColor = pressedTextColor;
  }

  /**
   * 设置顶部标题栏取消按钮文字大小（单位为sp）
   */
  public void setCancelTextSize(@Range(from = 10, to = 40) int cancelTextSize) {
    this.cancelTextSize = cancelTextSize;
  }

  /**
   * 设置顶部标题栏确定按钮文字大小（单位为sp）
   */
  public void setSubmitTextSize(@Range(from = 10, to = 40) int submitTextSize) {
    this.submitTextSize = submitTextSize;
  }

  /**
   * 设置顶部标题栏标题文字大小（单位为sp）
   */
  public void setTitleTextSize(@Range(from = 10, to = 40) int titleTextSize) {
    this.titleTextSize = titleTextSize;
  }

  /**
   * 设置选择器主体背景颜色
   */
  public void setBackgroundColor(Color backgroundColor) {
    this.backgroundColor = backgroundColor.getValue();
  }

  public void setTitleView(Component titleView) {
    this.titleView = titleView;
  }

  public Component getTitleView() {
    if (null == titleView) {
      throw new NullPointerException("please call show at first");
    }
    return titleView;
  }

  public Text getCancelButton() {
    if (null == cancelButton) {
      throw new NullPointerException("please call show at first");
    }
    return cancelButton;
  }

  public Text getSubmitButton() {
    if (null == submitButton) {
      throw new NullPointerException("please call show at first");
    }
    return submitButton;
  }

  /**
   * @see #makeHeaderView()
   * @see #makeCenterView()
   * @see #makeFooterView()
   */
  @Override
  protected final Component makeContentView() {
    DirectionalLayout rootLayout = new DirectionalLayout(activity);
    rootLayout.setLayoutConfig(new DirectionalLayout.LayoutConfig(MATCH_PARENT, MATCH_PARENT));
    ShapeElement shapeElement = new ShapeElement();
    shapeElement.setRgbColor(new RgbColor(backgroundColor));
    rootLayout.setBackground(shapeElement);
    rootLayout.setOrientation(VERTICAL);
    rootLayout.setAlignment(LayoutAlignment.CENTER);
    rootLayout.setPadding(0, 0, 0, 0);
    rootLayout.setClipEnabled(false);
    if (isActionButtonTop) {
      Component headerView = makeHeaderView();
      if (headerView != null) {
        rootLayout.addComponent(headerView);
      }
      if (topLineVisible) {
        Component lineView = new Component(activity);
        int height = ConvertUtils.toPx(activity, topLineHeight);
        lineView.setLayoutConfig(new DirectionalLayout.LayoutConfig(MATCH_PARENT, height));
        ShapeElement shapeElement1 = new ShapeElement();
        shapeElement1.setRgbColor(new RgbColor(topLineColor));
        lineView.setBackground(shapeElement1);
        rootLayout.addComponent(lineView);
      }
      DirectionalLayout.LayoutConfig rootParams = new DirectionalLayout.LayoutConfig(MATCH_PARENT, 0, LayoutAlignment.CENTER, 1);
      rootParams.setMargins(0, 15, 0, 15);
      rootLayout.addComponent(makeCenterView(), rootParams);
    } else {
      DirectionalLayout.LayoutConfig rootParams = new DirectionalLayout.LayoutConfig(MATCH_PARENT, 0, LayoutAlignment.CENTER, 1);
      rootParams.setMargins(0, 15, 0, 15);
      rootLayout.addComponent(makeCenterView(), rootParams);
      if (topLineVisible) {
        Component lineView = new Component(activity);
        int height = ConvertUtils.toPx(activity, topLineHeight);
        lineView.setLayoutConfig(new DirectionalLayout.LayoutConfig(MATCH_PARENT, height));
        ShapeElement shapeElement1 = new ShapeElement();
        shapeElement1.setRgbColor(new RgbColor(topLineColor));
        lineView.setBackground(shapeElement1);
        rootLayout.addComponent(lineView);
      }
      Component footerView = makeFooterView();
      if (footerView != null) {
        rootLayout.addComponent(footerView);
      }
    }
    return rootLayout;
  }

  protected Component makeHeaderView() {
    DependentLayout topButtonLayout = new DependentLayout(activity);
    int height = ConvertUtils.toPx(activity, topHeight);
    topButtonLayout.setLayoutConfig(new DependentLayout.LayoutConfig(MATCH_PARENT, height));
    ShapeElement shapeElement = new ShapeElement();
    shapeElement.setRgbColor(new RgbColor(topBackgroundColor));
    topButtonLayout.setBackground(shapeElement);
    topButtonLayout.setGravity(DependentLayout.LayoutConfig.VERTICAL_CENTER);

    cancelButton = new Text(activity);
    cancelButton.setVisibility(cancelVisible ? VISIBLE : HIDE);
    DependentLayout.LayoutConfig cancelParams = new DependentLayout.LayoutConfig(WRAP_CONTENT, MATCH_PARENT);
    cancelParams.addRule(ALIGN_PARENT_LEFT, TRUE);
    cancelParams.addRule(DependentLayout.LayoutConfig.VERTICAL_CENTER, TRUE);
    cancelButton.setLayoutConfig(cancelParams);
    ShapeElement shapeElement1 = new ShapeElement();
    shapeElement1.setRgbColor(new RgbColor(0, 0, 0, 0));
    cancelButton.setBackground(shapeElement1);
    cancelButton.setTextAlignment(TextAlignment.CENTER);
    int padding = ConvertUtils.toPx(activity, topPadding);
    cancelButton.setPadding(padding, 0, padding, 0);
    if (!TextTool.isNullOrEmpty(cancelText)) {
      cancelButton.setText(cancelText);
    }
    cancelButton.setTextColor(cancelTextColor);
    if (cancelTextSize != 0) {
      cancelButton.setTextSize(cancelTextSize);
    }
    cancelButton.setClickedListener(new ClickedListener() {
      @Override
      public void onClick(Component component) {
        dismiss();
        onCancel();
      }
    });
    topButtonLayout.addComponent(cancelButton);

    if (null == titleView) {
      Text textView = new Text(activity);
      DependentLayout.LayoutConfig titleParams = new DependentLayout.LayoutConfig(WRAP_CONTENT, WRAP_CONTENT);
      int margin = ConvertUtils.toPx(activity, topPadding);
      titleParams.setMarginLeft(margin);
      titleParams.setMarginRight(margin);
      titleParams.addRule(HORIZONTAL_CENTER, TRUE);
      titleParams.addRule(DependentLayout.LayoutConfig.VERTICAL_CENTER, TRUE);
      textView.setLayoutConfig(titleParams);
      textView.setTextAlignment(TextAlignment.CENTER);
      if (!TextTool.isNullOrEmpty(titleText)) {
        textView.setText(titleText);
      }
      textView.setTextColor(titleTextColor);
      if (titleTextSize != 0) {
        textView.setTextSize(titleTextSize);
      }
      titleView = textView;
    }
    topButtonLayout.addComponent(titleView);

    submitButton = new Text(activity);
    DependentLayout.LayoutConfig submitParams = new DependentLayout.LayoutConfig(WRAP_CONTENT, MATCH_PARENT);
    submitParams.addRule(ALIGN_PARENT_RIGHT, TRUE);
    submitParams.addRule(DependentLayout.LayoutConfig.VERTICAL_CENTER, TRUE);
    submitButton.setLayoutConfig(submitParams);
    ShapeElement shapeElement2 = new ShapeElement();
    shapeElement2.setRgbColor(new RgbColor(0, 0, 0, 0));
    submitButton.setBackground(shapeElement2);
    submitButton.setTextAlignment(TextAlignment.CENTER);
    submitButton.setPadding(padding, 0, padding, 0);
    if (!TextTool.isNullOrEmpty(submitText)) {
      submitButton.setText(submitText);
    }
    submitButton.setTextColor(submitTextColor);
    if (submitTextSize != 0) {
      submitButton.setTextSize(submitTextSize);
    }
    submitButton.setClickedListener(new ClickedListener() {
      @Override
      public void onClick(Component component) {
        dismiss();
        onSubmit();
      }
    });
    topButtonLayout.addComponent(submitButton);

    return topButtonLayout;
  }

  protected abstract V makeCenterView();

  protected Component makeFooterView() {
    DependentLayout topButtonLayout = new DependentLayout(activity);
    ShapeElement shapeElement = new ShapeElement();
    shapeElement.setRgbColor(new RgbColor(255, 255, 255, 255));
    int height = ConvertUtils.toPx(activity, topHeight);
    topButtonLayout.setLayoutConfig(new DependentLayout.LayoutConfig(MATCH_PARENT, height));
    topButtonLayout.setBackground(shapeElement);
    topButtonLayout.setGravity(VERTICAL_CENTER);

    cancelButton = new Text(activity);
    cancelButton.setVisibility(cancelVisible ? VISIBLE : HIDE);
    DependentLayout.LayoutConfig cancelParams = new DependentLayout.LayoutConfig(WRAP_CONTENT, MATCH_PARENT);
    cancelParams.addRule(ALIGN_PARENT_LEFT, TRUE);
    cancelParams.addRule(DependentLayout.LayoutConfig.VERTICAL_CENTER, TRUE);
    cancelButton.setLayoutConfig(cancelParams);
    ShapeElement shapeElement1 = new ShapeElement();
    shapeElement1.setRgbColor(new RgbColor(0, 0, 0, 0));
    cancelButton.setBackground(shapeElement1);
    cancelButton.setTextAlignment(TextAlignment.CENTER);
    int padding = ConvertUtils.toPx(activity, topPadding);
    cancelButton.setPadding(padding, 0, padding, 0);
    if (!TextTool.isNullOrEmpty(cancelText)) {
      cancelButton.setText(cancelText);
    }
    cancelButton.setTextColor(cancelTextColor);
    if (cancelTextSize != 0) {
      cancelButton.setTextSize(cancelTextSize);
    }
    cancelButton.setClickedListener(new ClickedListener() {
      @Override
      public void onClick(Component component) {
        dismiss();
        onCancel();
      }
    });
    topButtonLayout.addComponent(cancelButton);

    if (null == titleView) {
      Text textView = new Text(activity);
      DependentLayout.LayoutConfig titleParams = new DependentLayout.LayoutConfig(WRAP_CONTENT, WRAP_CONTENT);
      int margin = ConvertUtils.toPx(activity, topPadding);
      titleParams.setMarginLeft(margin);
      titleParams.setMarginRight(margin);
      titleParams.addRule(HORIZONTAL_CENTER, TRUE);
      titleParams.addRule(DependentLayout.LayoutConfig.VERTICAL_CENTER, TRUE);
      textView.setLayoutConfig(titleParams);
      textView.setTextAlignment(TextAlignment.CENTER);
      if (!TextTool.isNullOrEmpty(titleText)) {
        textView.setText(titleText);
      }
      textView.setTextColor(titleTextColor);
      if (titleTextSize != 0) {
        textView.setTextSize(titleTextSize);
      }
      titleView = textView;
    }
    topButtonLayout.addComponent(titleView);

    submitButton = new Text(activity);
    DependentLayout.LayoutConfig submitParams = new DependentLayout.LayoutConfig(WRAP_CONTENT, MATCH_PARENT);
    submitParams.addRule(ALIGN_PARENT_RIGHT, TRUE);
    submitParams.addRule(DependentLayout.LayoutConfig.VERTICAL_CENTER, TRUE);
    submitButton.setLayoutConfig(submitParams);
    submitButton.setBackground(shapeElement1);
    submitButton.setTextAlignment(TextAlignment.CENTER);
    submitButton.setPadding(padding, 0, padding, 0);
    if (!TextTool.isNullOrEmpty(submitText)) {
      submitButton.setText(submitText);
    }
    submitButton.setTextColor(submitTextColor);
    if (submitTextSize != 0) {
      submitButton.setTextSize(submitTextSize);
    }
    submitButton.setClickedListener(new ClickedListener() {
      @Override
      public void onClick(Component component) {
        dismiss();
        onSubmit();
      }
    });
    topButtonLayout.addComponent(submitButton);

    return topButtonLayout;
  }

  /*
   * 点击确定按钮的回调
   * */
  protected void onSubmit() {

  }

  /*
   * 点击取消按钮的回调
   * */
  protected void onCancel() {

  }

}
