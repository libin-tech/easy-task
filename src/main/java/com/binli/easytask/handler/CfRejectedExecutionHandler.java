package com.binli.easytask.handler;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池饱和策略
 *
 * @author yongen
 * @date 2022/1/8 3:16 PM
 */
public class CfRejectedExecutionHandler implements RejectedExecutionHandler {

  @Override
  public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
    try {
      // 重新放入队列里执行
      executor.getQueue().put(r);
    } catch (InterruptedException e) {
      System.out.println("饱和策略发生异常! 线程信息：" + r);
    }
  }
}
