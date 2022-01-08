package com.binli.easytask.enums;

/**
 * @author yongen
 * @description: 任务返回结果枚举类 方法本身运行是否正确的结果类型
 * @date 2022/1/7 2:28 PM
 */
public enum TaskResultType {

  //方法成功执行并返回了业务人员需要的结果
  SUCCESS,
  //方法成功执行但是返回的是业务人员不需要的结果
  FAILURE,
  //方法执行抛出了Exception
  EXCEPTION;
}
