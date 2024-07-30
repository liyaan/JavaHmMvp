package com.example.utils.ohospickers.util;

import ohos.agp.window.service.Display;
import ohos.agp.window.service.DisplayAttributes;
import ohos.agp.window.service.DisplayManager;
import ohos.app.Context;

import java.util.Optional;

/**
 * 获取屏幕宽高等信息、全屏切换、保持屏幕常亮、截屏等
 */
public final class ScreenUtils {
  private static final String TAG = "ScreenUtils";
  private static DisplayAttributes dm = null;

  public static DisplayAttributes displayMetrics(Context context) {
    if (null != dm) {
      return dm;
    }
    Optional<Display> display = DisplayManager.getInstance().getDefaultDisplay(context);
    dm = display.get().getAttributes();
    LogUtils.info(TAG, "screen width=" + dm.width + "px, screen height=" + dm.height
        + "px");
    return dm;
  }

  public static int widthPixels(Context context) {
    return displayMetrics(context).width;
  }

  public static int heightPixels(Context context) {
    return displayMetrics(context).height;
  }

}
