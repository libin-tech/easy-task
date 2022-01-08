package com.binli.easytask.core;

import com.binli.easytask.model.TaskItem;

/**
 * @author yongen
 * @description: 处理队列中到期任务的处理
 * @date 2022/1/8 3:13 PM
 */
public class FetchJob implements Runnable{

  @Override
  public void run() {
    try {
      TaskItem<String> itemVo = CheckJobProcesser.queue.take();
      String jobId = itemVo.getTaskData();
      // 从工作容器中删除当前工作任务
      PendingJobPool.getJoMap().remove(jobId);
      System.out.println("工作任务：【" + jobId + "】, 生存周期已到！已从工作容器中删除！");
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
