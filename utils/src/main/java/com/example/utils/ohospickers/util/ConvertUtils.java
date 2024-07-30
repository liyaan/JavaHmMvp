package com.example.utils.ohospickers.util;

import java.io.*;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import ohos.agp.utils.TextTool;
import ohos.app.Context;

/**
 * 数据类型转换、单位转换
 *
 * @author matt
 * blog: addapp.cn
 */
public class ConvertUtils {
  private static final String TAG = "ConvertUtils";
  public static final long GB = 1073741824;
  public static final long MB = 1048576;
  public static final long KB = 1024;

  public static int toInt(Object obj) {
    try {
      return Integer.parseInt(obj.toString());
    } catch (NumberFormatException e) {
      return -1;
    }
  }

  public static int toInt(byte[] bytes) {
    int result = 0;
    byte abyte;
    for (int i = 0; i < bytes.length; i++) {
      abyte = bytes[i];
      result += (abyte & 0xFF) << (8 * i);
    }
    return result;
  }

  public static int toShort(byte first, byte second) {
    return (first << 8) + (second & 0xFF);
  }

  public static long toLong(Object obj) {
    try {
      return Long.parseLong(obj.toString());
    } catch (NumberFormatException e) {
      return -1L;
    }
  }

  public static float toFloat(Object obj) {
    try {
      return Float.parseFloat(obj.toString());
    } catch (NumberFormatException e) {
      return -1f;
    }
  }

  /**
   * int占4字节
   *
   * @param i the
   * @return byte [ ]
   */
  public static byte[] toByteArray(int i) {
    // byte[] bytes = new byte[4];
    // bytes[0] = (byte) (0xff & i);
    // bytes[1] = (byte) ((0xff00 & i) >> 8);
    // bytes[2] = (byte) ((0xff0000 & i) >> 16);
    // bytes[3] = (byte) ((0xff000000 & i) >> 24);
    // return bytes;
    return ByteBuffer.allocate(4).putInt(i).array();
  }

  public static byte[] toByteArray(String hexData, boolean isHex) {
    if (hexData == null || hexData.equals("")) {
      return null;
    }
    if (!isHex) {
      return hexData.getBytes();
    }
    hexData = hexData.replaceAll("\\s+", "");
    String hexDigits = "0123456789ABCDEF";
    ByteArrayOutputStream baos = new ByteArrayOutputStream(
        hexData.length() / 2);
    // 将每2位16进制整数组装成一个字节
    for (int i = 0; i < hexData.length(); i += 2) {
      baos.write((hexDigits.indexOf(hexData.charAt(i)) << 4 | hexDigits
          .indexOf(hexData.charAt(i + 1))));
    }
    byte[] bytes = baos.toByteArray();
    try {
      baos.close();
    } catch (IOException e) {
      LogUtils.warn(TAG, e + "");
    }
    return bytes;
  }

  public static String toHexString(String str) {
    if (TextTool.isNullOrEmpty(str))
      return "";
    StringBuilder builder = new StringBuilder();
    byte[] bytes = str.getBytes();
    for (byte aByte : bytes) {
      builder.append(Integer.toHexString(0xFF & aByte));
      builder.append(" ");
    }
    return builder.toString();
  }

  /**
   * To hex string string.
   *
   * @param bytes the bytes
   * @return the string
   */
  public static String toHexString(byte... bytes) {
    char[] DIGITS = {'0', '1', '2', '3', '4', '5', '6',
        '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    // 参见：http://www.oschina.net/code/snippet_116768_9019
    char[] buffer = new char[bytes.length * 2];
    for (int i = 0, j = 0; i < bytes.length; ++i) {
      int u = bytes[i] < 0 ? bytes[i] + 256 : bytes[i];//转无符号整型
      buffer[j++] = DIGITS[u >>> 4];
      buffer[j++] = DIGITS[u & 0xf];
    }
    return new String(buffer);
  }

  /**
   * To hex string string.
   *
   * @param num the num
   * @return the string
   */
  public static String toHexString(int num) {
    String hexString = Integer.toHexString(num);
    LogUtils.info(TAG, String.format(Locale.CHINA, "%d to hex string is %s", num, hexString));
    return hexString;
  }

  /**
   * To binary string string.
   *
   * @param bytes the bytes
   * @return the string
   */
  public static String toBinaryString(byte... bytes) {
    char[] DIGITS = {'0', '1', '2', '3', '4', '5', '6',
        '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    // 参见：http://www.oschina.net/code/snippet_116768_9019
    char[] buffer = new char[bytes.length * 8];
    for (int i = 0, j = 0; i < bytes.length; ++i) {
      int u = bytes[i] < 0 ? bytes[i] + 256 : bytes[i];//转无符号整型
      buffer[j++] = DIGITS[(u >>> 7) & 0x1];
      buffer[j++] = DIGITS[(u >>> 6) & 0x1];
      buffer[j++] = DIGITS[(u >>> 5) & 0x1];
      buffer[j++] = DIGITS[(u >>> 4) & 0x1];
      buffer[j++] = DIGITS[(u >>> 3) & 0x1];
      buffer[j++] = DIGITS[(u >>> 2) & 0x1];
      buffer[j++] = DIGITS[(u >>> 1) & 0x1];
      buffer[j++] = DIGITS[u & 0x1];
    }
    return new String(buffer);
  }

  /**
   * To binary string string.
   *
   * @param num the num
   * @return the string
   */
  public static String toBinaryString(int num) {
    String binaryString = Integer.toBinaryString(num);
    LogUtils.info(TAG, String.format(Locale.CHINA, "%d to binary string is %s", num, binaryString));
    return binaryString;
  }

  public static String toSlashString(String str) {
    String result = "";
    char[] chars = str.toCharArray();
    for (char chr : chars) {
      if (chr == '"' || chr == '\'' || chr == '\\') {
        result += "\\";//符合“"”“'”“\”这三个符号的前面加一个“\”
      }
      result += chr;
    }
    return result;
  }

  public static <T> T[] toArray(List<T> list) {
    //noinspection unchecked
    return (T[]) list.toArray();
  }

  public static <T> List<T> toList(T[] array) {
    return Arrays.asList(array);
  }

  public static String toString(Object[] objects) {
    return Arrays.deepToString(objects);
  }

  public static String toString(Object[] objects, String tag) {
    StringBuilder sb = new StringBuilder();
    for (Object object : objects) {
      sb.append(object);
      sb.append(tag);
    }
    return sb.toString();
  }

  public static byte[] toByteArray(InputStream is) {
    if (is == null) {
      return null;
    }
    try {
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      byte[] buff = new byte[100];
      while (true) {
        int len = is.read(buff, 0, 100);
        if (len == -1) {
          break;
        } else {
          os.write(buff, 0, len);
        }
      }
      byte[] bytes = os.toByteArray();
      os.close();
      is.close();
      return bytes;
    } catch (IOException e) {
      LogUtils.warn(TAG, e.toString());
    }
    return null;
  }

  /**
   * dp转换为px
   */
  public static int toPx(Context context, float dpValue) {
    final int pxValue = (int) (context.getResourceManager().getDeviceCapability().screenDensity / 160 * dpValue);
    LogUtils.info(TAG, dpValue + " dp == " + pxValue + " px");
    return pxValue;
  }

  public static String toFileSizeString(long fileSize) {
    DecimalFormat df = new DecimalFormat("0.00");
    String fileSizeString;
    if (fileSize < KB) {
      fileSizeString = fileSize + "B";
    } else if (fileSize < MB) {
      fileSizeString = df.format((double) fileSize / KB) + "K";
    } else if (fileSize < GB) {
      fileSizeString = df.format((double) fileSize / MB) + "M";
    } else {
      fileSizeString = df.format((double) fileSize / GB) + "G";
    }
    return fileSizeString;
  }

  public static String toString(InputStream is, String charset) {
    StringBuilder sb = new StringBuilder();
    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(is, charset));
      while (true) {
        String line = reader.readLine();
        if (line == null) {
          break;
        } else {
          sb.append(line).append("\n");
        }
      }
      reader.close();
      is.close();
    } catch (IOException e) {
      LogUtils.error(TAG, e.toString());
    }
    return sb.toString();
  }

  public static String toString(InputStream is) {
    return toString(is, "utf-8");
  }

}
