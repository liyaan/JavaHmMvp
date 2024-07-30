package com.liyaan.selectpicker.Utils;

import ohos.app.Context;
import ohos.global.resource.RawFileEntry;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class jsonUtils {
  /**
   * 获取去最原始的数据信息
   *
   * @param context c
   * @param name    name
   * @return json data
   */
  public static String getOriginalFundData(Context context, String name) {
    InputStream input = null;
    try {
      // json文件名称
      RawFileEntry entry = context.getResourceManager().getRawFileEntry("resources/rawfile/" + name);
      input = entry.openRawFile();

      String json = convertStreamToString(input);
      return json;
    } catch (Exception e) {
    }
    ;
    return null;
  }

  /**
   * input 流转换为字符串
   *
   * @param is is
   * @return String
   */
  private static String convertStreamToString(InputStream is) {
    String s = null;
    try {
      // 格式转换
      Scanner scanner = new Scanner(is, "UTF-8").useDelimiter("\\A");
      if (scanner.hasNext()) {
        s = scanner.next();
      }
      is.close();
    } catch (IOException e) {
    }
    return s;
  }
}
