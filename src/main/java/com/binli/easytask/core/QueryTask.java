package com.binli.easytask.core;

import com.binli.easytask.model.TaskResult;
import com.binli.easytask.utils.SleepTools;
import java.util.List;

/**
 * 查询任务进度
 *
 * @author yongen
 * @date 2022/1/8 3:18 PM
 */
public class QueryTask implements Runnable {

  /**
   * 工作线程
   */
  private final PendingJobPool pool;

  /**
   * 任务ID
   */
  private final String jobId;

  /**
   * 任务长度
   */
  private final Integer jobLength;

  /**
   * 查询刷新时间(单位：秒)
   */
  private final Integer refreshTime;

  public QueryTask(PendingJobPool pool, String jobId, Integer jobLength, Integer refreshTime) {
    super();
    this.pool = pool;
    this.jobId = jobId;
    this.jobLength = jobLength;
    this.refreshTime = refreshTime;
  }


  @Override
  public void run() {
    boolean isStop = false;
    while (!isStop) {
      List<TaskResult<Integer>> taskResults = pool.getTaskDetailList(jobId);
      if (!taskResults.isEmpty()) {
        System.out.println(pool.getTaskTotalProcessor(jobId));
        System.out.println(taskResults);
        int current = pool.getJob(jobId).getTaskProcessorCount();
        if (current == jobLength) {
          isStop = true;
        }
      }
      SleepTools.second(refreshTime);
    }
  }
}
