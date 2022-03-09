package com.binli.easytask.model;

import com.binli.easytask.core.CheckJobProcessor;
import com.binli.easytask.core.ITaskProcessor;
import com.binli.easytask.enums.TaskResultType;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.LongAdder;

/**
 * 提交给框架执行的工作实体类, 工作：表示本批次需要处理的同性质任务(Task)的一个集合
 *
 * @author yongen
 * @date 2022/1/8 3:05 PM
 */
public class JobInfo<R> {

  /**
   * 工作id： 用于区分唯一的工作标识
   */
  private final String jobId;

  /**
   * 工作任务数量
   */
  private final int jobLength;

  /**
   * 工作任务处理器
   */
  private final ITaskProcessor<?, ?> taskProcessor;

  /**
   * 任务处理成功数量
   */
  private final LongAdder successCount;

  /**
   * 已处理的任务数
   */
  private final LongAdder taskProcessorCount;

  /**
   * 结果队列：从头拿 从尾部放
   */
  private final LinkedBlockingDeque<TaskResult<R>> taskDetailDeque;

  /**
   * 工作完成的保存时间，超过这个时间的任务从队列中删除
   */
  private final long expireTime;

  public JobInfo(String jobId, int jobLength, ITaskProcessor<?, ?> taskProcessor, long expireTime) {
    super();
    this.jobId = jobId;
    this.jobLength = jobLength;
    this.taskProcessor = taskProcessor;
    // 初始为0
    this.successCount = new LongAdder();
    // 初始为0
    this.taskProcessorCount = new LongAdder();
    //阻塞队列不应该由调用者传入，应该内部生成，长度为工作的任务个数
    this.taskDetailDeque = new LinkedBlockingDeque<>(jobLength);
    this.expireTime = expireTime;
  }

  public ITaskProcessor<?, ?> getTaskProcessor() {
    return taskProcessor;
  }

  /**
   * 提供获取任务处理成功数量
   */
  public int getSuccessCount() {
    return successCount.intValue();
  }

  /**
   * 提供获取已处理的任务数量
   */
  public int getTaskProcessorCount() {
    return taskProcessorCount.intValue();
  }

  /**
   * 提供获取工作中失败的次数
   */
  public int getFailCount() {
    return taskProcessorCount.intValue() - successCount.intValue();
  }

  /**
   * 提供查询任务进度的方法
   */
  public String getTotalProcess() {
    String formatter = "JobId [%s] task detail : Success[%s] Current[%s] Fail[%s] Total[%s]";
    return String.format(formatter, jobId, getSuccessCount(), getTaskProcessorCount(), getFailCount(), jobLength);
  }

  /**
   * 提供获取工作中每个任务的处理结果详情
   *
   * @return 任务执行结果详情集合
   */
  public List<TaskResult<R>> getTaskDetail() {
    List<TaskResult<R>> list = new LinkedList<>();
    TaskResult<R> taskResult;
    //从阻塞队列中拿任务结果
    // 反复取，一直取到null为止，说明目前队列中的最新的任务结果已经取完，可以不取了
    while ((taskResult = taskDetailDeque.pollFirst()) != null) {
      list.add(taskResult);
    }
    return list;
  }

  /**
   * 提供放任务的结果的方法，从业务角度来说，保证最终一致性即可，不需要加锁
   */
  public void addResult(TaskResult<R> taskResult, CheckJobProcessor checkJobProcessor) {
    if (TaskResultType.SUCCESS.equals(taskResult.getResultType())) {
      successCount.increment();
    }
    taskDetailDeque.addLast(taskResult);
    taskProcessorCount.increment();
    // 当工作长度等于已处理的任务数时，代表任务执行完毕，此时讲任务放入过期缓存队列中等待处理
    if (jobLength == getTaskProcessorCount()) {
      checkJobProcessor.putJob(jobId, expireTime);
    }
  }

}
