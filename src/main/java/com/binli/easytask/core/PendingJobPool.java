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
 * @author yongen
 * @description: 框架的主体类，也是业务调用者主要实现的类
 * @date 2022/1/8 3:14 PM
 */
public class PendingJobPool {

  /**
   * 线程池线程数等于cpu线程数
   */
  private static final int CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors();
  /**
   * 线程池存放任务工作队列，最大容量为5000
   */
  private static final BlockingQueue<Runnable> taskQueue = new ArrayBlockingQueue<Runnable>(5000);

  /**
   * 自定义线程池
   */
  public static ThreadPoolExecutor taskExecutor = new ThreadPoolExecutor(
      CORE_POOL_SIZE,
      CORE_POOL_SIZE * 2,
      2,
      TimeUnit.MINUTES,
      taskQueue,
      new CfRejectedExecutionHandler());

  /**
   * 工作的存放容器
   */
  private static ConcurrentHashMap<String, JobInfo<?>> jobMap = new ConcurrentHashMap<>();

  /**
   * 获取工作容器
   */
  public static Map<String, JobInfo<?>> getJoMap() {
    return jobMap;
  }

  /**
   * 将过期缓存队列加入
   */
  public static CheckJobProcesser checkJobProcesser = CheckJobProcesser.getInstance();

  private PendingJobPool() {
  }

  private static class PendingJonPoolHolder {

    public static PendingJobPool pool = new PendingJobPool();
  }

  public static PendingJobPool getInstance() {
    return PendingJonPoolHolder.pool;
  }

  /**
   * 根据工作任务id获取工作任务
   *
   * @param <R>
   * @param jobId
   * @return
   */
  @SuppressWarnings("unchecked")
  private <R> JobInfo<R> getJob(String jobId) {
    JobInfo<R> jobInfo = (JobInfo<R>) jobMap.get(jobId);
    if (null == jobInfo) {
      throw new RuntimeException("工作任务：" + jobId + ", 非法任务！");

    } else {
      return jobInfo;
    }
  }

  /**
   * 对调用者开放：调用者注册工作
   *
   * @param <R>
   * @param jobId
   * @param jobLength
   * @param taskProcesser
   * @param expireTime
   */
  public <R> void registerJob(String jobId, int jobLength, ITaskProcesser<?, ?> taskProcesser, long expireTime) {
    JobInfo<R> jobInfo = new JobInfo<R>(jobId, jobLength, taskProcesser, expireTime);
    // 将工作添加到工作容器中
    if (jobMap.putIfAbsent(jobId, jobInfo) != null) {
      throw new RuntimeException("工作任务：" + jobId + ", 已经注册了！");
    }
  }

  /**
   * 对调用者开放：提交工作中的任务
   *
   * @param <T>
   * @param <R>
   * @param jobId
   * @param data
   */
  public <T, R> void putTask(String jobId, T data) {
    JobInfo<R> jobInfo = getJob(jobId);
    PendingJobTask<T, R> pendingJobTask = new PendingJobTask<T, R>(jobInfo, data);
    // 放入线程池中执行
    taskExecutor.execute(pendingJobTask);
  }

  /**
   * 对调用者开放：提供获取工作中每个任务的处理结果详情
   *
   * @param <R>
   * @param jobId
   * @return
   */
  public <R> List<TaskResult<R>> getTaskDetailList(String jobId) {
    JobInfo<R> jobInfo = getJob(jobId);
    return jobInfo.getTaskDetail();
  }

  public <R> String getTaskTotalProcesser(String jobId) {
    JobInfo<R> jobInfo = getJob(jobId);
    return jobInfo.getTotalProcess();
  }
}
