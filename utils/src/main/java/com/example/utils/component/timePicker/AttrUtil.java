package com.example.utils.component.timePicker;

import ohos.agp.components.AttrSet;
import ohos.agp.components.element.Element;
import ohos.agp.utils.Color;

public class AttrUtil {


    public static String getStringValue(AttrSet attrSet, String key, String defValue) {
        if (attrSet.getAttr(key).isPresent()) {
            return attrSet.getAttr(key).get().getStringValue();
        } else {
            return defValue;
        }
    }

    public static Color getColorValue(AttrSet attrSet, String key, String defValue) {
        if (attrSet.getAttr(key).isPresent()) {
            return new Color(Color.getIntColor(attrSet.getAttr(key).get().getStringValue()));
        } else {
            return new Color(Color.getIntColor(defValue));
        }
    }

    public static int getColorIntValue(AttrSet attrSet, String key, int defValue) {
        if (attrSet.getAttr(key).isPresent()) {
            return attrSet.getAttr(key).get().getColorValue().getValue();
        } else {
            return defValue;
        }
    }

    public static Boolean getBooleanValue(AttrSet attrSet, String key, boolean defValue) {
        if (attrSet.getAttr(key).isPresent()) {
            return attrSet.getAttr(key).get().getBoolValue();
        } else {
            return defValue;
        }
    }

    public static Element getElementValue(AttrSet attrSet, String key, Element defValue) {
        if (attrSet.getAttr(key).isPresent()) {
            return attrSet.getAttr(key).get().getElement();
        } else {
            return defValue;
        }
    }

    public static int getDimension(AttrSet attrSet, String key, int defDimensionValue) {
        if (attrSet.getAttr(key).isPresent()) {
            return attrSet.getAttr(key).get().getDimensionValue();
        } else {
            // 默认数据转换为vp
            return defDimensionValue;
        }
    }

    public static Integer getIntegerValue(AttrSet attrSet, String key, Integer defValue) {
        if (attrSet.getAttr(key).isPresent()) {
            return attrSet.getAttr(key).get().getIntegerValue();
        } else {
            return defValue;
        }
    }

}
