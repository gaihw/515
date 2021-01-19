package scheduler;

import java.io.IOException;
import java.sql.SQLException;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class PlanJob implements Job {
	private PlanService ps = new PlanService();

	@Override
	public void execute(JobExecutionContext jec) throws JobExecutionException {

		try {
			ps.taskExecute();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
