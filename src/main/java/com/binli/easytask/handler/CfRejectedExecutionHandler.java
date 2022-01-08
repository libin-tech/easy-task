package com.binli.easytask.handler;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author yongen
 * @description: 线程池饱和策略
 * @date 2022/1/8 3:16 PM
 */
public class CfRejectedExecutionHandler implements RejectedExecutionHandler {

  @Override
  public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
    try {
      System.out.println("任务线程：" + Thread.currentThread().getName() + " 被丢弃！正在尝试重新运行...");
      // 重新放入队列里执行
      executor.getQueue().put(r);
    } catch (InterruptedException e) {
      System.out.println("饱和策略发生异常! 线程信息：" + r);
    }
  }
}
