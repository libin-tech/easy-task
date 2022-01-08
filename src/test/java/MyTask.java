import com.binli.easytask.core.ITaskProcesser;
import com.binli.easytask.enums.TaskResultType;
import com.binli.easytask.model.TaskResult;
import com.binli.easytask.utils.SleepTools;
import java.util.Random;

/**
 * @author yongen
 * @description: 自定义任务实现: 休眠随机时间，根据随机数判断返回状态
 * @date 2022/1/8 3:26 PM
 */
public class MyTask implements ITaskProcesser<Integer, Integer> {

  @Override
  public TaskResult<Integer> taskExecute(Integer data) {
    TaskResult<Integer> taskResult;
    Random random = new Random();
    // 生成一千以内随机数
    int number = random.nextInt(1000);
    // 让线程休眠
    SleepTools.ms(number);
    // 判断生成的随机 在200以内是正常， 400-500 是失败， 其他数字是异常
    if (number <= 200) {
      int returnValue = number * data;
      taskResult = new TaskResult<Integer>(TaskResultType.SUCCESS, returnValue, "任务处理成功！");
    } else if (number >= 400 && number <= 500) {
      taskResult = new TaskResult<Integer>(TaskResultType.FAILURE, -1, "任务处理失败！");
    } else {
      taskResult = new TaskResult<Integer>(TaskResultType.EXCEPTION, -2, "任务处理异常！");
    }
    return taskResult;
  }
}
