import com.binli.easytask.core.ITaskProcesser;
import com.binli.easytask.core.PendingJobPool;
import java.util.Random;

/**
 * @author yongen
 * @description: 测试方法
 * @date 2022/1/8 3:30 PM
 */
public class MyTaskTest {

  // 工作任务id
  private final static String JOB_ID = "jobId:001【判断随机数区间返回状态】";
  // 工作长度（次数）
  private final static int JOB_LENGHT = 1000;


  public static void main(String[] args) {
    ITaskProcesser<Integer, Integer> myTask = new MyTask();
    // 拿到框架的实例
    PendingJobPool pool =  PendingJobPool.getInstance();
    // 注册任务
    pool.registerJob(JOB_ID, JOB_LENGHT, myTask, 10000);
    // 添加工作任务
    Random random = new Random();
    for (int i = 0; i < JOB_LENGHT; i++) {
      pool.putTask(JOB_ID, random.nextInt(10));
    }
    // 启动查询线程
    new Thread(new QueryMyTask(pool, JOB_ID)).start();
  }

}
