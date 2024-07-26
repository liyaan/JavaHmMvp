package com.example.utils.component.timePicker;

import ohos.agp.colors.RgbColor;
import ohos.agp.components.AttrHelper;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.window.service.Display;
import ohos.agp.window.service.DisplayManager;
import ohos.app.Context;

public class Utils {

    public static boolean isEmptyStr(String str) {
        if (str != null) {
            return "".equals(str) || str.length() == 0;
        } else {
            return true;
        }
    }

    public static ShapeElement getShapeElementByColor(int color) {
        int red = (color & 0xff0000) >> 16;
        int green = (color & 0x00ff00) >> 8;
        int blue = (color & 0x0000ff);
        ShapeElement backElement = new ShapeElement();
        backElement.setRgbColor(new RgbColor(red, green, blue));
        return backElement;
    }

    public static int vp2pxValue(int vpValue, Context context) {
        return AttrHelper.vp2px(vpValue, context);
    }

    /**
     * getDisplayWidthInPx 获取屏幕宽度
     *
     * @param context 上下文
     * @return 屏幕宽度
     */
    public static int getDisplayWidthInPx(Context context) {
        Display display = DisplayManager.getInstance().getDefaultDisplay(context).get();
        return display.getAttributes().width;
    }

    /**
     * getDisplayHeightInPx 获取屏幕高度，不包含状态栏的高度
     *
     * @param context 上下文
     * @return 屏幕高度，不包含状态栏的高度
     */

    public static int getDisplayHeightInPx(Context context) {
        Display display = DisplayManager.getInstance().getDefaultDisplay(context).get();
        return display.getAttributes().height;
    }

}
