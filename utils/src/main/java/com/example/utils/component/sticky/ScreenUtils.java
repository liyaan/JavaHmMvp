package com.example.utils.component.sticky;

import ohos.agp.utils.Point;
import ohos.agp.window.service.Display;
import ohos.agp.window.service.DisplayAttributes;
import ohos.agp.window.service.DisplayManager;
import ohos.app.Context;

import java.util.Optional;

/**
 * ScreenUtils
 */
public class ScreenUtils {
    /**
     * 构造函数
     */
    private ScreenUtils() {
    }

    /**
     * 获取屏幕宽度，单位为px
     *
     * @param context 上下文
     * @return 返回int类型的屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        Display display = DisplayManager.getInstance().getDefaultDisplay(context).get();
        Point point = new Point();
        display.getRealSize(point);
        return (int) point.getPointX();
    }

    /**
     * 获取屏幕高度，单位为px(不包含导航栏)
     *
     * @param context 上下文
     * @return 返回int类型的屏幕高度
     */
    public static int getAttributesHeight(Context context) {
        Optional<Display> defaultDisplay = DisplayManager.getInstance().getDefaultDisplay(context);
        if (defaultDisplay.isPresent()) {
            Display display = defaultDisplay.get();
            DisplayAttributes attributes = display.getAttributes();
            Point lPoint = new Point(attributes.width, attributes.height);
            return lPoint.getPointYToInt();
        }
        return 0;
    }

    /**
     * 获取屏幕高度，单位为px(包含状态栏)
     *
     * @param context 上下文
     * @return 返回int类型的屏幕高度
     */
    public static int getScreenHeight(Context context) {
        Display display = DisplayManager.getInstance().getDefaultDisplay(context).get();
        Point point = new Point();
        display.getRealSize(point);
        return (int) point.getPointY();
    }

    /**
     * 获取屏幕密度，单位为dpi
     *
     * @param context 上下文
     * @return 返回int类型的屏幕密度
     */
    public static int getScreenDensityDpi(Context context) {
        Optional<Display> display = DisplayManager.getInstance().getDefaultDisplay(context);
        DisplayAttributes displayAttributes = display.get().getAttributes();
        return displayAttributes.densityDpi;
    }

    /**
     * 获取缩放系数，值为 densityDpi/160
     *
     * @param context 上下文
     * @return 返回float类型的缩放系数
     */
    public static float getScreenScale(Context context) {
        Optional<Display> display = DisplayManager.getInstance().getDefaultDisplay(context);
        DisplayAttributes displayAttributes = display.get().getAttributes();
        return displayAttributes.densityPixels;
    }

    /**
     * 获取文字缩放系数，同scale
     *
     * @param context 上下文
     * @return 返回float类型的文字缩放系数
     */
    public static float getScreenFontScale(Context context) {
        Optional<Display> display = DisplayManager.getInstance().getDefaultDisplay(context);
        DisplayAttributes displayAttributes = display.get().getAttributes();
        return displayAttributes.scalDensity;
    }

    /**
     * 获得状态栏的高度
     *
     * @param context 上下文
     * @return 返回int类型的状态栏高度
     */
    public static int getStatusHeight(Context context) {
        return getRealHeight(context) - getHeight(context);
    }

    private static int getHeight(Context context) {
        Display display = DisplayManager.getInstance().getDefaultDisplay(context).get();
        Point point = new Point();
        display.getSize(point);
        return (int) point.getPointY();
    }

    private static int getRealHeight(Context context) {
        Display display = DisplayManager.getInstance().getDefaultDisplay(context).get();
        Point point = new Point();
        display.getRealSize(point);
        return (int) point.getPointY();
    }
}
