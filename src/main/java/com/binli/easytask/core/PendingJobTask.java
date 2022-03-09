package com.binli.easytask.core;

import com.binli.easytask.enums.TaskResultType;
import com.binli.easytask.model.JobInfo;
import com.binli.easytask.model.TaskResult;

/**
 * 对工作中的任务进行包装，提交给线程池使用，并处理任务的结果，写入阻塞队列供查询
 *
 * @author yongen
 * @date 2022/1/8 3:18 PM
 */
public class PendingJobTask<T, R> implements Runnable {

  private final JobInfo<R> jobInfo;
  private final T data;

  public PendingJobTask(JobInfo<R> jobInfo, T data) {
    super();
    this.jobInfo = jobInfo;
    this.data = data;
  }

  @Override
  public void run() {
    R r = null;
    ITaskProcessor<T, R> taskProcessor = (ITaskProcessor<T, R>) jobInfo.getTaskProcessor();
    TaskResult<R> taskResult = null;
    try {
      // 执行调用者自己实现的业务方法，返回业务执行结果
      taskResult = taskProcessor.taskExecute(data);
      // 开始检查业务执行结果的完整性
      if (null == taskResult) {
        taskResult = new TaskResult<R>(TaskResultType.EXCEPTION, r, "未返回业务执行结果！");
      }
      if (null == taskResult.getResultType()) {
        if (null == taskResult.getReason()) {
          taskResult = new TaskResult<R>(TaskResultType.EXCEPTION, r, "未返回业务执行结果的描述！");
        } else {
          taskResult = new TaskResult<R>(TaskResultType.EXCEPTION, r, "未返回业务执行结果的返回类型！");
        }
      }
    } catch (Exception e) {
      taskResult = new TaskResult<R>(TaskResultType.EXCEPTION, r, e.getMessage());
    } finally {
      assert taskResult != null;
      jobInfo.addResult(taskResult, PendingJobPool.checkJobProcessor);
    }
  }


}
