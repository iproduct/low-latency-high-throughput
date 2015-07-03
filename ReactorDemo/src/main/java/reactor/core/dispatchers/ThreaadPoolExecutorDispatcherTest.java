package reactor.core.dispatchers;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import reactor.Environment;
import reactor.core.Dispatcher;
import reactor.core.dispatch.SynchronousDispatcher;
import reactor.core.dispatch.ThreadPoolExecutorDispatcher;
import reactor.fn.Consumer;
import ch.qos.logback.classic.Logger;

public class ThreaadPoolExecutorDispatcherTest {
	final static Logger LOG = (Logger) LoggerFactory
			.getLogger(ThreaadPoolExecutorDispatcherTest.class);
	static Environment env = Environment.initialize();
	Thread taskThread = null;
	Thread currentThread = Thread.currentThread();
	CountDownLatch latch = new CountDownLatch(1);
	Dispatcher sameThread = new SynchronousDispatcher();
	Dispatcher diffThread = new ThreadPoolExecutorDispatcher(1, 128);
	Consumer<String> consumer = ev -> taskThread = Thread.currentThread();
	Consumer<Throwable> errorConsumer = error -> error.printStackTrace();

	@Test
	public void testExecutesOnSameThread() throws InterruptedException {
	
		// a task is submitted on the same thread
		sameThread.dispatch("test", consumer, errorConsumer);

		Assert.assertTrue("the task thread should be the current thread",
				currentThread == taskThread);
	}
	
	@Test
	public void testExecutesOnAnotherThread() throws InterruptedException {

		// a task is submitted to the thread pool dispatcher
		diffThread.dispatch("test2", ev -> {
			consumer.accept(ev);
			latch.countDown();
		}, errorConsumer);

		latch.await(5, TimeUnit.SECONDS); // Wait for task to execute

		// the task thread should be different when the current thread
		Assert.assertTrue("the task thread should be the current thread",
				currentThread != taskThread);

	}
}
