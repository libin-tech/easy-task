package com.binli.easytask.core;

import com.binli.easytask.model.TaskResult;

/**
 * @author yongen
 * @description: 提任务处理器接口，要求框架使用者实现的任务接口，因为任务的性质在调用时才知道，所以传入的参数和方法的返回值均使用泛型
 * @date 2022/1/8 3:05 PM
 */
public interface ITaskProcesser<T,R> {

  /**
   * 任务执行方法
   * @param data 调用方法需要使用的业务数据
   * @return 方法执行后业务方法的结果
   */
  TaskResult<R> taskExecute(T data);

}
