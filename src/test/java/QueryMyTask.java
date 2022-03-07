import com.binli.easytask.core.PendingJobPool;
import com.binli.easytask.model.TaskResult;
import com.binli.easytask.utils.SleepTools;
import java.util.List;

/**
 * @author yongen
 * @description: 查询任务进度
 * @date 2022/1/8 3:28 PM
 */
public class QueryMyTask implements Runnable{

  private final PendingJobPool pool;

  private final String jobId;

  public QueryMyTask(PendingJobPool pool, String jobId) {
    super();
    this.pool = pool;
    this.jobId = jobId;
  }

  @Override
  public void run() {
      // 查询次数
    int queryCount = 0;
    while (queryCount < 400) {
      List<TaskResult<Integer>> taskResults = pool.getTaskDetailList(jobId);
      if (null != taskResults) {
        System.out.println(pool.getTaskTotalProcessor(jobId));
        System.out.println(taskResults);
      }
      SleepTools.ms(100);
      queryCount++;
    }
  }
}
