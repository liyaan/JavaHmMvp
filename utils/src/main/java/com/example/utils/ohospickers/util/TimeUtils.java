package com.example.utils.ohospickers.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class TimeUtils {

  private static final ThreadLocal<SimpleDateFormat> SDF_THREAD_LOCAL = new ThreadLocal<>();

  private static SimpleDateFormat getDateFormat(String pattern) {
    SimpleDateFormat simpleDateFormat = SDF_THREAD_LOCAL.get();
    if (simpleDateFormat == null) {
      simpleDateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
      SDF_THREAD_LOCAL.set(simpleDateFormat);
    } else {
      simpleDateFormat.applyPattern(pattern);
    }
    return simpleDateFormat;
  }

  public static Date string2Date(final String time, final String pattern) {
    return string2Date(time, getDateFormat(pattern));
  }

  public static Date string2Date(final String time, final DateFormat format) {
    try {
      return format.parse(time);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return null;
  }
}
