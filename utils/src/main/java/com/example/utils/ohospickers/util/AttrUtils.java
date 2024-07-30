/*
 * Copyright (C) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.utils.ohospickers.util;

import ohos.agp.components.AttrSet;
import ohos.agp.components.element.Element;

/**
 * Attr utils
 */
public class AttrUtils {
  /**
   * Gets int from attr *
   *
   * @param attrs        attrs
   * @param name         name
   * @param defaultValue default value
   * @return the int from attr
   */
  public static int getIntFromAttr(AttrSet attrs, String name, int defaultValue) {
    int value = defaultValue;
    if (attrs.getAttr(name).isPresent() && attrs.getAttr(name).get() != null) {
      value = attrs.getAttr(name).get().getIntegerValue();
    }
    return value;
  }

  /**
   * Gets float from attr *
   *
   * @param attrs        attrs
   * @param name         name
   * @param defaultValue default value
   * @return the float from attr
   */
  public static float getFloatFromAttr(AttrSet attrs, String name, float defaultValue) {
    float value = defaultValue;
    if (attrs.getAttr(name).isPresent() && attrs.getAttr(name).get() != null) {
      value = attrs.getAttr(name).get().getFloatValue();
    }
    return value;
  }

  /**
   * Gets boolean from attr *
   *
   * @param attrs        attrs
   * @param name         name
   * @param defaultValue default value
   * @return the boolean from attr
   */
  public static boolean getBooleanFromAttr(AttrSet attrs, String name, boolean defaultValue) {
    boolean value = defaultValue;
    if (attrs.getAttr(name).isPresent() && attrs.getAttr(name).get() != null) {
      value = attrs.getAttr(name).get().getBoolValue();
    }
    return value;
  }

  /**
   * Gets long from attr *
   *
   * @param attrs        attrs
   * @param name         name
   * @param defaultValue default value
   * @return the long from attr
   */
  public static long getLongFromAttr(AttrSet attrs, String name, long defaultValue) {
    long value = defaultValue;
    if (attrs.getAttr(name).isPresent() && attrs.getAttr(name).get() != null) {
      value = attrs.getAttr(name).get().getLongValue();
    }
    return value;
  }

  /**
   * Gets color from attr *
   *
   * @param attrs        attrs
   * @param name         name
   * @param defaultValue default value
   * @return the color from attr
   */
  public static int getColorFromAttr(AttrSet attrs, String name, int defaultValue) {
    int value = defaultValue;
    if (attrs.getAttr(name).isPresent() && attrs.getAttr(name).get() != null) {
      value = attrs.getAttr(name).get().getColorValue().getValue();
    }
    return value;
  }

  /**
   * Gets dimension from attr *
   *
   * @param attrs        attrs
   * @param name         name
   * @param defaultValue default value
   * @return the dimension from attr
   */
  public static int getDimensionFromAttr(AttrSet attrs, String name, int defaultValue) {
    int value = defaultValue;
    if (attrs.getAttr(name).isPresent() && attrs.getAttr(name).get() != null) {
      value = attrs.getAttr(name).get().getDimensionValue();
    }
    return value;
  }

  /**
   * Gets string from attr *
   *
   * @param attrs        attrs
   * @param name         name
   * @param defaultValue default value
   * @return the string from attr
   */
  public static String getStringFromAttr(AttrSet attrs, String name, String defaultValue) {
    String value = defaultValue;
    if (attrs.getAttr(name).isPresent() && attrs.getAttr(name).get() != null) {
      value = attrs.getAttr(name).get().getStringValue();
    }
    return value;
  }

  /**
   * Gets element from attr *
   *
   * @param attrs        attrs
   * @param name         name
   * @param defaultValue default value
   * @return the element from attr
   */
  public static Element getElementFromAttr(AttrSet attrs, String name, Element defaultValue) {
    Element value = defaultValue;
    if (attrs.getAttr(name).isPresent() && attrs.getAttr(name).get() != null) {
      value = attrs.getAttr(name).get().getElement();
    }
    return value;
  }
}
