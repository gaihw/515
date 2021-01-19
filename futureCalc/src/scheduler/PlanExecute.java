package scheduler;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
//import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;

import scheduler.PlanJob;
/**
 * 
 * @author Muguozheng
 * @date  2018年10月21日
 * @version
 * @description  执行定时任务
 */
public class PlanExecute {
	public static void main(String[] args) throws Exception {
		// 创建job
		JobDetail detail = new JobDetail("myJob", "myGroup", PlanJob.class);

		// 这里的触发器是规定的时间间隔内做的事，不是很常用
		// //创建触发器
		// SimpleTrigger trigger=new
		// SimpleTrigger("myTrigger",SimpleTrigger.REPEAT_INDEFINITELY, 3000);
		// //设置开始执行时间
		// trigger.setStartTime(new Date(System.currentTimeMillis()+1000));

		// 这里有三个参数（1.触发器的名称2.触发器的组3.时间表达式）
		CronTrigger trigger = new CronTrigger("myCron", "myGroup", "10 25 21 * * ?");

		// 创建调度工厂
		SchedulerFactory factory = new StdSchedulerFactory();
		// 创建调度器
		Scheduler scheduler = factory.getScheduler();
		// 绑定job 和触发器
		scheduler.scheduleJob(detail, trigger);
		// 启动
		scheduler.start();
		// 设置休眠时间
		// Thread.sleep(10000);
		// 停止定时任务
		// scheduler.shutdown();
	}
}
