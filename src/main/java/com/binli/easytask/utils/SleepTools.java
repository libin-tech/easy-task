package com.binli.easytask.utils;

import java.util.concurrent.TimeUnit;

/**
 * 线程休眠辅助工具类
 *
 * @author yongen
 * @date 2022/1/7 2:36 PM
 */
public class SleepTools {

  /**
   * 按秒休眠
   *
   * @param seconds 秒数
   */
  public static void second(int seconds) {
    try {
      TimeUnit.SECONDS.sleep(seconds);
    } catch (InterruptedException ignored) {
    }
  }

  /**
   * 按毫秒数休眠
   *
   * @param seconds 毫秒数
   */
  public static void ms(int seconds) {
    try {
      TimeUnit.MILLISECONDS.sleep(seconds);
    } catch (InterruptedException ignored) {
    }
  }
}
