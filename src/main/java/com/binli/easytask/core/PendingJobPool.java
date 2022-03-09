package com.binli.easytask.core;

import com.binli.easytask.handler.CfRejectedExecutionHandler;
import com.binli.easytask.model.JobInfo;
import com.binli.easytask.model.TaskResult;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 框架的主体类，也是业务调用者主要实现的类
 *
 * @author yongen
 * @date 2022/1/8 3:14 PM
 */
public class PendingJobPool {

  /**
   * 线程池线程数等于cpu线程数
   */
  private static final int CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors();
  /**
   * 线程池存放任务工作队列，最大容量为10000
   */
  private static final BlockingQueue<Runnable> TASK_QUEUE = new ArrayBlockingQueue<Runnable>(10000);

  /**
   * 自定义线程池
   */
  public static ThreadPoolExecutor taskExecutor = new ThreadPoolExecutor(
      CORE_POOL_SIZE,
      CORE_POOL_SIZE * 2,
      2,
      TimeUnit.MINUTES,
      TASK_QUEUE,
      new CfRejectedExecutionHandler());

  /**
   * 工作的存放容器
   */
  private static final ConcurrentHashMap<String, JobInfo<?>> JOB_MAP = new ConcurrentHashMap<>();

  /**
   * 获取工作容器
   */
  public static Map<String, JobInfo<?>> getJoMap() {
    return JOB_MAP;
  }

  /**
   * 将过期缓存队列加入
   */
  public static CheckJobProcessor checkJobProcessor = CheckJobProcessor.getInstance();

  private PendingJobPool() {
  }

  private static class PendingJonPoolHolder {

    public static PendingJobPool pool = new PendingJobPool();
  }

  public static PendingJobPool getInstance() {
    return PendingJonPoolHolder.pool;
  }

  /**
   * 对调用者开放：根据工作任务id获取工作任务
   */
  public <R> JobInfo<R> getJob(String jobId) {
    JobInfo<R> jobInfo = (JobInfo<R>) JOB_MAP.get(jobId);
    if (null == jobInfo) {
      throw new RuntimeException("工作任务：" + jobId + ", 非法任务！");

    } else {
      return jobInfo;
    }
  }

  /**
   * 对调用者开放：调用者注册工作
   */
  public <R> void registerJob(String jobId, int jobLength, ITaskProcessor<?, ?> taskProcesser, long expireTime) {
    JobInfo<R> jobInfo = new JobInfo<R>(jobId, jobLength, taskProcesser, expireTime);
    // 将工作添加到工作容器中
    if (JOB_MAP.putIfAbsent(jobId, jobInfo) != null) {
      throw new RuntimeException("工作任务：" + jobId + ", 已经注册了！");
    }
  }

  /**
   * 对调用者开放：提交工作中的任务
   */
  public <T, R> void putTask(String jobId, T data) {
    JobInfo<R> jobInfo = getJob(jobId);
    PendingJobTask<T, R> pendingJobTask = new PendingJobTask<>(jobInfo, data);
    // 放入线程池中执行
    taskExecutor.execute(pendingJobTask);
  }

  /**
   * 对调用者开放：提供获取工作中每个任务的处理结果详情
   */
  public <R> List<TaskResult<R>> getTaskDetailList(String jobId) {
    JobInfo<R> jobInfo = getJob(jobId);
    return jobInfo.getTaskDetail();
  }

  /**
   * 查询任务进度
   */
  public <R> String getTaskTotalProcessor(String jobId) {
    JobInfo<R> jobInfo = getJob(jobId);
    return jobInfo.getTotalProcess();
  }
}
