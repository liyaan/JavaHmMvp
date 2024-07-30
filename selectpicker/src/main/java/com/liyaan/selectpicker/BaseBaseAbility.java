package com.liyaan.selectpicker;

import com.example.utils.ohospickers.util.LogUtils;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.*;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.Color;
import ohos.agp.window.service.Window;
import ohos.app.Context;

import java.util.ArrayList;
import java.util.List;

import static ohos.agp.components.ComponentContainer.LayoutConfig.MATCH_PARENT;
import static ohos.agp.utils.TextAlignment.CENTER;

public abstract class BaseBaseAbility extends AbilitySlice {
  private static final String TAG = "BaseAbility";
  protected Context context;
  protected BaseBaseAbility activity;

  protected String className = getClass().getSimpleName();
  private List<LifeCycleListener> lifeCycleListeners = new ArrayList<LifeCycleListener>();

  protected abstract Component getContentView();

  protected abstract void setContentViewAfter(Component contentView);

  @Override
  protected void onStart(Intent intent) {
    super.onStart(intent);
    LogUtils.info(TAG, className + " onCreate");
    for (LifeCycleListener listener : lifeCycleListeners) {
      listener.onActivityCreated(this);
    }
    context = getApplicationContext();
    activity = this;
    //被系统回收后重启恢复
    if (intent != null) {
      LogUtils.info(TAG, "savedInstanceState is not null");
      onStateRestore(intent);
    }
    //显示界面布局
    Component contentView = getContentView();
    DirectionalLayout directionalLayout = new DirectionalLayout(getContext());
    directionalLayout.setWidth(ComponentContainer.LayoutConfig.MATCH_PARENT);
    directionalLayout.setHeight(ComponentContainer.LayoutConfig.MATCH_PARENT);
    if (contentView == null) {
      Text textView = new Text(this);
      ShapeElement shapeElement = new ShapeElement();
      shapeElement.setRgbColor(new RgbColor(255, 0, 0));
      textView.setBackground(shapeElement);
      textView.setLayoutConfig(new ComponentContainer.LayoutConfig(MATCH_PARENT, MATCH_PARENT));
      textView.setTextAlignment(CENTER);
      textView.setText("请先初始化内容视图");
      textView.setTextColor(Color.WHITE);
      contentView = textView;
    }
    LogUtils.info(TAG, className + " setContentView before");
    setContentViewBefore();
    directionalLayout.addComponent(contentView);
    setUIContent(directionalLayout);
    if (isTranslucentStatusBar()) {
      Window window = getWindow();
      window.setStatusBarColor(Color.BLUE.getValue());
      window.setStatusBarVisibility(Component.VISIBLE);
    }
    setContentViewAfter(contentView);
    LogUtils.info(TAG, className + " setContentView after");
  }

  protected void onStateRestore(Intent intent) {

  }

  protected void setContentViewBefore() {
    LogUtils.info(TAG, className + " setContentView before");
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    LogUtils.info(TAG, className + " onBackPressed");
  }

  @Override
  protected void onForeground(Intent intent) {
    super.onForeground(intent);
    LogUtils.info(TAG, className + " onRestart");
    for (LifeCycleListener listener : lifeCycleListeners) {
      listener.onActivityRestarted(this);
    }
  }

  @Override
  protected void onActive() {
    super.onActive();
    LogUtils.info(TAG, className + " onStart");
    for (LifeCycleListener listener : lifeCycleListeners) {
      listener.onActivityStarted(this);
    }
    for (LifeCycleListener listener : lifeCycleListeners) {
      listener.onActivityResumed(this);
    }
    //和removeActivity对应

  }

  @Override
  protected void onInactive() {
    super.onInactive();
    LogUtils.info(TAG, className + " onPause");
    for (LifeCycleListener listener : lifeCycleListeners) {
      listener.onActivityPaused(this);
    }
  }

  @Override
  public void onStop() {
    super.onStop();
    LogUtils.info(TAG, className + " onStop");
    for (LifeCycleListener listener : lifeCycleListeners) {
      listener.onActivityStopped(this);
    }
    for (LifeCycleListener listener : lifeCycleListeners) {
      listener.onActivityDestroyed(this);
    }
  }

  protected boolean isTranslucentStatusBar() {
    return true;
  }

  protected <T> T inflateView(int layoutResource) {
    LogUtils.info(TAG, className + " inflate view by layout resource");
    //noinspection unchecked
    return (T) LayoutScatter.getInstance(activity).parse(layoutResource, null, false);
  }

  protected <T> T findView(int id) {
    //noinspection unchecked
    return (T) findComponentById(id);
  }

  public void addLifeCycleListener(LifeCycleListener listener) {
    if (lifeCycleListeners.contains(listener)) {
      return;
    }
    lifeCycleListeners.add(listener);
  }

  public void removeLifeCycleListener(LifeCycleListener listener) {
    lifeCycleListeners.remove(listener);
  }

  /**
   * Activity生命周期监听
   */
  public interface LifeCycleListener {

    void onActivityCreated(BaseBaseAbility activity);

    void onActivityResumed(BaseBaseAbility activity);

    void onActivityStarted(BaseBaseAbility activity);

    void onActivityRestarted(BaseBaseAbility activity);

    void onActivityPaused(BaseBaseAbility activity);

    void onActivityStopped(BaseBaseAbility activity);

    void onActivityDestroyed(BaseBaseAbility activity);

  }

}
