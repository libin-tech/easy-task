package com.binli.easytask.model;

import com.binli.easytask.enums.TaskResultType;

/**
 * 任务返回结果实体类
 *
 * @author yongen
 * @date 2022/1/7 2:39 PM
 */
public class TaskResult<R> {

  /**
   * 任务执行返回结果类型
   */
  private final TaskResultType resultType;
  /**
   * 任务执行返回的业务结果数据
   */
  private final R returnValue;
  /**
   * 任务执行返回的失败的原因
   */
  private final String reason;


  /**
   * 成功时构造方法
   *
   * @param resultType 任务执行返回结果类型
   * @param returnValue 任务执行返回结果数据
   */
  public TaskResult(TaskResultType resultType, R returnValue) {
    super();
    this.resultType = resultType;
    this.returnValue = returnValue;
    this.reason = TaskResultType.SUCCESS.name();
  }

  /**
   * 失败时构造方法
   *
   * @param resultType 任务执行返回结果类型
   * @param returnValue 任务执行返回结果数据
   */
  public TaskResult(TaskResultType resultType, R returnValue, String reason) {
    super();
    this.resultType = resultType;
    this.returnValue = returnValue;
    this.reason = reason;
  }

  public TaskResultType getResultType() {
    return resultType;
  }

  public R getReturnValue() {
    return returnValue;
  }

  public String getReason() {
    return reason;
  }

  @Override
  public String toString() {
    return "TaskResult [resultType=" + resultType + ", returnValue=" + returnValue + ", reason=" + reason + "]";
  }

}
