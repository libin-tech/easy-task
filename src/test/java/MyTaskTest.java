import com.binli.easytask.core.ITaskProcessor;
import com.binli.easytask.core.PendingJobPool;
import com.binli.easytask.core.QueryTask;
import java.util.Random;

/**
 * 主测试方法
 *
 * @author yongen
 * @date 2022/1/8 3:30 PM
 */
public class MyTaskTest {

  // 工作任务id
  private final static String JOB_ID = "jobId:001【判断随机数区间返回状态】";
  // 工作长度（次数）
  private final static int JOB_LENGTH = 1000;


  public static void main(String[] args) {
    ITaskProcessor<Integer, Integer> myTask = new MyTask();
    // 拿到框架的实例
    PendingJobPool pool = PendingJobPool.getInstance();
    // 注册任务
    pool.registerJob(JOB_ID, JOB_LENGTH, myTask, 10000);
    // 添加工作任务
    Random random = new Random();
    for (int i = 0; i < JOB_LENGTH; i++) {
      pool.putTask(JOB_ID, random.nextInt(10));
    }
    // 启动查询线程
    new Thread(new QueryTask(pool, JOB_ID, JOB_LENGTH, 1)).start();
  }

}
