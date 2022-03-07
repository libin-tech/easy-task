package com.binli.easytask.core;

import com.binli.easytask.model.TaskItem;
import java.util.concurrent.DelayQueue;

/**
 * 任务完成后, 在一定的时间供查询，之后为释放资源节约内存，需要定期处理过期的任务
 * @author yongen
 * @date 2022/1/8 3:07 PM
 */
public class CheckJobProcessor {

  /**
   * 存放已完成任务等待过期的队列
   */
  public static DelayQueue<TaskItem<String>> queue = new DelayQueue<>();

  private CheckJobProcessor() {
  }

  private static class CheckJobProcessesHolder {

    public static CheckJobProcessor holder = new CheckJobProcessor();
  }

  public static CheckJobProcessor getInstance() {
    return CheckJobProcessesHolder.holder;
  }

  /**
   * 处理队列中到期任务的处理
   */
  public static class FetchJob implements Runnable {

    @Override
    public void run() {
      try {
        TaskItem<String> taskItem = queue.take();
        String jobId = taskItem.getTaskData();
        // 从工作容器中删除当前工作任务
        PendingJobPool.getJoMap().remove(jobId);
        System.out.println("工作任务：【" + jobId + "】, 生存周期已到！已从工作容器中删除！");
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * 工作执行结束后，将执行返回结果存入等待过期队列中
   *
   * @param jobId 任务ID
   * @param expireTime 过期时间
   */
  public void putJob(String jobId, long expireTime) {
    TaskItem<String> taskItem = new TaskItem<>(expireTime, jobId);
    queue.offer(taskItem);
    System.out.println("工作任务：【" + jobId + "】，已经放入了过期检查缓存，过期时长：" + expireTime);
  }

  static {
    Thread thread = new Thread(new FetchJob());
    thread.setDaemon(true);
    thread.start();
    System.out.println("开启任务过期检查守护线程...............");
  }

}
