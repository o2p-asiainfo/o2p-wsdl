package test.com.asiainfo.o2p.task;

import static org.junit.Assert.assertNotNull;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;

import com.asiainfo.o2p.task.FileClearJob;

/**
 * The class <code>FileClearJobTest</code> contains tests for the class <code>{@link FileClearJob}</code>.
 *
 * @generatedBy CodePro at 15-5-13 上午11:17
 * @author zhongming
 * @version $Revision: 1.0 $
 */
public class FileClearJobTest {
	/**
	 * Run the FileClearJob() constructor test.
	 *
	 * @generatedBy CodePro at 15-5-13 上午11:17
	 */
	@Test
	public void testFileClearJob_1()
		throws Exception {
		FileClearJob result = new FileClearJob();
		assertNotNull(result);
		// add additional test code here
	}

	/**
	 * Run the void execute(JobExecutionContext) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 15-5-13 上午11:17
	 */
	@Test
	public void testExecute_1()
		throws Exception {
		FileClearJob fixture = EasyMock.createMock(FileClearJob.class);
		JobExecutionContext context = EasyMock.createMock(JobExecutionContext.class);//new JobExecutionContext(new StdScheduler(new QuartzScheduler(new QuartzSchedulerResources(), new SchedulingContext(), 1L, 1L), new SchedulingContext()), new TriggerFiredBundle(new JobDetail(), new CronTrigger(), new AnnualCalendar(), true, new Date(), new Date(), new Date(), new Date()), new EJBInvokerJob());
		
		fixture.execute();
		EasyMock.expectLastCall();
		EasyMock.replay(fixture);
		
		fixture.execute();
		EasyMock.verify(fixture);
		
		
//		fixture.execute(context);

		// add additional test code here
		// An unexpected exception was thrown in user code while executing this test:
		//    java.lang.ExceptionInInitializerError
		//       at org.apache.log4j.Logger.getLogger(Logger.java:107)
		//       at com.asiainfo.o2p.task.FileClearJob.<clinit>(FileClearJob.java:35)
	}

	/**
	 * Perform pre-test initialization.
	 *
	 * @throws Exception
	 *         if the initialization fails for some reason
	 *
	 * @generatedBy CodePro at 15-5-13 上午11:17
	 */
	@Before
	public void setUp()
		throws Exception {
		// add additional set up code here
	}
}