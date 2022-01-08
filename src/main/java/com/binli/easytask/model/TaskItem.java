package com.binli.easytask.model;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author yongen
 * @description: 存到队列里的元素,支持延时获取的元素的阻塞队列，元素必须要实现Delayed接口。根据有效时间作为队列的优先级
 * @date 2022/1/7 2:28 PM
 */
public class TaskItem<T> implements Delayed {

  /**
   *  过期时间 单位ms
   */
  private final Long activeTime;

  /**
   * 任务实体
   */
  private final T taskData;

  public TaskItem(long activeTime, T taskData) {
    super();
    this.activeTime = TimeUnit.NANOSECONDS.convert(activeTime, TimeUnit.MILLISECONDS) + System.nanoTime();
    this.taskData = taskData;
  }

  public long getActiveTime() {
    return activeTime;
  }

  public T getTaskData() {
    return taskData;
  }

  @Override
  public long getDelay(TimeUnit unit) {
    // 任务剩余时间 = 到期时间-当前系统时间，系统一般是纳秒级的，所以这里做一次转换
    return unit.convert(activeTime - System.nanoTime(), TimeUnit.NANOSECONDS);
  }

  @Override
  public int compareTo(Delayed delayed) {
    // 实际剩余时间 = 任务剩余时间 - 当前传入的时间
    long delayTime = getDelay(TimeUnit.NANOSECONDS) - delayed.getDelay(TimeUnit.NANOSECONDS);
    // 根据剩余时间判断等于0 返回1 不等于0
    // 有可能大于0 有可能小于0  大于0返回1  小于返回-1
    return (delayTime == 0) ? 0 : ((delayTime > 0) ? 1 : -1);
  }
}
