package com.github.throyer.rabbitmq.utils;

import static java.lang.String.format;

public class Time {
  public static String elapsedMilliseconds(long startTimeInNanoseconds, long endTimeInNanoseconds) {
    var elapsedTime = (endTimeInNanoseconds - startTimeInNanoseconds) / 1e6d;
    return format("%.1fms", elapsedTime);
  }

  public static String elapsedSeconds(long startTimeInNanoseconds, long endTimeInNanoseconds) {
    var elapsedTime = (endTimeInNanoseconds - startTimeInNanoseconds) / 1e+9;
    return format("%.1fs", elapsedTime);
  }
}
