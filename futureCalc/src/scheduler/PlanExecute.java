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
 * @date  2018��10��21��
 * @version
 * @description  ִ�ж�ʱ����
 */
public class PlanExecute {
	public static void main(String[] args) throws Exception {
		// ����job
		JobDetail detail = new JobDetail("myJob", "myGroup", PlanJob.class);

		// ����Ĵ������ǹ涨��ʱ�����������£����Ǻܳ���
		// //����������
		// SimpleTrigger trigger=new
		// SimpleTrigger("myTrigger",SimpleTrigger.REPEAT_INDEFINITELY, 3000);
		// //���ÿ�ʼִ��ʱ��
		// trigger.setStartTime(new Date(System.currentTimeMillis()+1000));

		// ����������������1.������������2.����������3.ʱ����ʽ��
		CronTrigger trigger = new CronTrigger("myCron", "myGroup", "10 25 21 * * ?");

		// �������ȹ���
		SchedulerFactory factory = new StdSchedulerFactory();
		// ����������
		Scheduler scheduler = factory.getScheduler();
		// ��job �ʹ�����
		scheduler.scheduleJob(detail, trigger);
		// ����
		scheduler.start();
		// ��������ʱ��
		// Thread.sleep(10000);
		// ֹͣ��ʱ����
		// scheduler.shutdown();
	}
}
