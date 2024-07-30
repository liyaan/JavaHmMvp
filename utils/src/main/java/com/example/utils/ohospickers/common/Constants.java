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
package com.example.utils.ohospickers.common;

public interface Constants {
  /**
   * 公共常量
   */
  public static class Public {
    /**
     * 不显示
     */
    public static final int NONE = -1;
  }

  /**
   * 日期常量
   */
  public static class DateMode {
    /**
     * 年月日
     */
    public static final int YEAR_MONTH_DAY = 0;
    /**
     * 年月
     */
    public static final int YEAR_MONTH = 1;
    /**
     * 月日
     */
    public static final int MONTH_DAY = 2;
  }

  /**
   * 时间常量
   */
  public static class TimeMode {
    /**
     * 24小时
     */
    public static final int HOUR_24 = 3;
    /**
     * 12小时
     */
    public static final int HOUR_12 = 4;
  }
}
