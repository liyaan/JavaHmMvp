package com.example.utils.ohospickers.common;

import com.example.utils.ohospickers.util.ConvertUtils;
import com.example.utils.ohospickers.util.LogUtils;
import com.example.utils.ohospickers.util.ScreenUtils;
import ohos.aafwk.ability.Ability;
import ohos.agp.animation.Animator;
import ohos.agp.animation.AnimatorProperty;
import ohos.agp.animation.AnimatorScatter;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.*;
import ohos.agp.window.dialog.CommonDialog;
import ohos.agp.window.dialog.IDialog;
import ohos.agp.window.service.DisplayAttributes;
import ohos.agp.window.service.Window;
import ohos.agp.window.service.WindowManager;
import ohos.app.Context;
import ohos.multimodalinput.event.KeyEvent;

import static ohos.agp.utils.LayoutAlignment.CENTER;

public abstract class BaseDialog<V extends Component> implements CommonDialog.DestroyedListener, IDialog.KeyboardCallback {
  public static final int MATCH_PARENT = StackLayout.LayoutConfig.MATCH_PARENT;
  public static final int WRAP_CONTENT = StackLayout.LayoutConfig.MATCH_CONTENT;
  private static final String TAG = "BaseDialog";
  protected Ability activity;
  protected int screenWidthPixels;
  protected int screenHeightPixels;
  private CommonDialog dialog;
  private StackLayout contentLayout;
  private boolean isPrepared = false;
  private boolean outCancel = false;
  private AnimatorProperty animatorProperty;

  public BaseDialog(Ability activity) {
    this.activity = activity;
    DisplayAttributes metrics = ScreenUtils.displayMetrics(activity);
    screenWidthPixels = metrics.width;
    screenHeightPixels = metrics.height;
    initDialog();
  }

  private void initDialog() {
    contentLayout = new StackLayout(activity);
    contentLayout.setLayoutConfig(new StackLayout.LayoutConfig(WRAP_CONTENT, WRAP_CONTENT));
    contentLayout.setFocusable(Component.FOCUS_ENABLE);
    contentLayout.setTouchFocusable(true);
    //contentLayout.setFitsSystemWindows(true);
    dialog = new CommonDialog(activity);
    setCanceledOnTouchOutside(outCancel);
//        dialog.setCanceledOnTouchOutside(outCancel);//触摸屏幕取消窗体
//        dialog.setCancelable(outCancel);//按返回键取消窗体
    dialog.siteKeyboardCallback(this);
    dialog.setDestroyedListener(this);
    Window window = dialog.getWindow();
    if (window != null) {
//            window.setGravity(Gravity.BOTTOM);
      ////解决宽度问题
      window.setLayoutAlignment(CENTER);
//            dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
      WindowManager.LayoutConfig params = window.getLayoutConfig().get();
      params.width = getScreenWidthPixels() - ConvertUtils.toPx(activity, 10);
      params.height = WRAP_CONTENT;
      window.setLayoutConfig(params);
      window.setBackgroundColor(new RgbColor(0, 0, 0, 0));
      //RuntimeException: requestFeature() must be called before adding content
//            window.requestFeature(Window.FEATURE_NO_TITLE);
//            window.setContentView(contentLayout);
    }
    setSize(screenWidthPixels, WRAP_CONTENT);
  }

  public int getScreenWidthPixels() {
    return screenWidthPixels;
  }

  public int getScreenHeightPixels() {
    return screenHeightPixels;
  }

  public void setCanceledOnTouchOutside(boolean canceled) {
    this.outCancel = canceled;
    dialog.siteRemovable(outCancel);
    dialog.setAutoClosable(outCancel);
  }

  /**
   * 创建弹窗的内容视图
   *
   * @return the view
   */
  protected abstract V makeContentView();

  /**
   * 固定高度为屏幕的高
   *
   * @param fillScreen true为全屏
   */
  public void setFillScreen(boolean fillScreen) {
    if (fillScreen) {
      setSize(screenWidthPixels, (int) (screenHeightPixels * 0.85f));
    }
  }

  /**
   * 固定高度为屏幕的一半
   *
   * @param halfScreen true为半屏
   */
  public void setHalfScreen(boolean halfScreen) {
    if (halfScreen) {
      setSize(screenWidthPixels, screenHeightPixels / 2);
    }
  }

  /**
   * 位于屏幕何处
   * 可以调整宽度和高度
   */
  public void setGravity(int gravity) {
    Window window = dialog.getWindow();
    if (window != null) {
      window.setLayoutAlignment(gravity);
    }
    if (gravity == CENTER) {
      //居于屏幕正中间时，宽度不允许填充屏幕
      setWidth((int) (screenWidthPixels * 0.9f));
    }
  }

  /**
   * 设置弹窗的内容视图之前执行
   */
  protected void setContentViewBefore() {
  }

  /**
   * 设置弹窗的内容视图之后执行
   *
   * @param contentView 弹窗的内容视图
   */
  protected void setContentViewAfter(V contentView) {
  }

  public void setContentView(Component view) {
    contentLayout.removeAllComponents();
    contentLayout.addComponent(view);
  }

  public void setAnimationStyle(int animContent, int animBackground) {

  }

  public void setOnDismissListener(final CommonDialog.DestroyedListener destroyedListener) {
    dialog.setDestroyedListener(new CommonDialog.DestroyedListener() {
      @Override
      public void onDestroy() {
        BaseDialog.this.onDestroy();
        destroyedListener.onDestroy();
      }
    });
    LogUtils.info(TAG, "dialog setOnDismissListener");
  }

  public void setOnKeyListener(final IDialog.KeyboardCallback keyListener) {
    dialog.siteKeyboardCallback(new IDialog.KeyboardCallback() {
      @Override
      public boolean clickKey(IDialog iDialog, KeyEvent keyEvent) {
        BaseDialog.this.clickKey(iDialog, keyEvent);
        return keyListener.clickKey(iDialog, keyEvent);
      }
    });
  }

  /**
   * 设置弹窗的宽和高
   *
   * @param width  宽
   * @param height 高
   */
  public void setSize(int width, int height) {
    if (width == MATCH_PARENT) {
      //360奇酷等手机对话框MATCH_PARENT时两边还会有边距，故强制填充屏幕宽
      width = screenWidthPixels;
    }
    if (width == 0 && height == 0) {
      width = screenWidthPixels;
      height = WRAP_CONTENT;
    } else if (width == 0) {
      width = screenWidthPixels;
    } else if (height == 0) {
      height = WRAP_CONTENT;
    }
    LogUtils.info(TAG, String.format("will set dialog width/height to: %s/%s", width, height));
    ComponentContainer.LayoutConfig params = contentLayout.getLayoutConfig();
    if (params == null) {
      params = new ComponentContainer.LayoutConfig(width, height);
    } else {
      params.width = width;
      params.height = height;
    }
    contentLayout.setLayoutConfig(params);
    dialog.setSize(width,height);
  }

  /**
   * 设置弹窗的宽
   *
   * @param width 宽
   * @see #setSize(int, int)
   */
  public void setWidth(int width) {
    setSize(width, 0);
  }

  /**
   * 设置弹窗的高
   *
   * @param height 高
   * @see #setSize(int, int)
   */
  public void setHeight(int height) {
    setSize(0, height);
  }

  /**
   * 设置是否需要重新初始化视图，可用于数据刷新
   */
  public void setPrepared(boolean prepared) {
    isPrepared = prepared;
  }

  public boolean isShowing() {
    return dialog.isShowing();
  }

  public final void show() {
    if (isPrepared) {
      dialog.show();
      showAfter();
      return;
    }
    LogUtils.info(TAG, "do something before dialog show");
    setContentViewBefore();
    V view = makeContentView();
    setContentView(view);// 设置弹出窗体的布局
    setContentViewAfter(view);
    dialog.setContentCustomComponent(contentLayout);
    isPrepared = true;
    dialog.show();
    showAfter();
  }

  protected void showAfter() {
    LogUtils.info(TAG, "dialog show");
  }

  public void dismiss() {
    dismissImmediately();
  }

  protected final void dismissImmediately() {
    dialog.hide();
    LogUtils.info(TAG, "dialog dismiss");
  }

  public boolean onBackPress() {
    dismiss();
    return false;
  }

  public Context getContext() {
    return activity;
  }

  public Window getWindow() {
    return dialog.getWindow();
  }

  /**
   * 弹框的内容视图
   */
  public Component getContentView() {
    // IllegalStateException: The specified child already has a parent.
    // You must call removeView() on the child's parent first.
    return contentLayout.getComponentAt(0);
  }

  /**
   * 弹框的根视图
   */
  public ComponentContainer getRootView() {
    return contentLayout;
  }

  @Override
  public void onDestroy() {
    dismiss();
  }

  @Override
  public boolean clickKey(IDialog iDialog, KeyEvent keyEvent) {
    if (keyEvent.isKeyDown() && keyEvent.getKeyCode() == KeyEvent.KEY_BACK) {
      onBackPress();
    }
    return false;
  }
}
