package reactor.core.dispatchers;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.slf4j.LoggerFactory;

import reactor.Environment;
import reactor.core.Dispatcher;
import reactor.core.dispatch.SynchronousDispatcher;
import reactor.core.dispatch.ThreadPoolExecutorDispatcher;
import reactor.fn.Consumer;
import reactor.rx.Stream;
import reactor.rx.Streams;
import ch.qos.logback.classic.Logger;

public class ThreaadPoolExecutorDispatcher {
	final static Logger LOG = (Logger) LoggerFactory
			.getLogger(ThreaadPoolExecutorDispatcher.class);
	static Thread taskThread = null;


	public static void main(String[] args) throws InterruptedException {
		Environment env = Environment.initialize();
		CountDownLatch latch = new CountDownLatch(5);

		Dispatcher trreadPoolDispatcher = new ThreadPoolExecutorDispatcher(5, 128);

		Consumer<String> consumer = ev -> {
		    LOG.info("Hello " + ev + " from thread: " +Thread.currentThread() + "\n");
		    latch.countDown(); 
		};

		Consumer<Throwable> errorConsumer = error ->
		    error.printStackTrace();

		// a task is submitted to the thread pool dispatcher
		Stream<String> stream = Streams.just("One", "Two", "Three", "Four", "Five", "Six", "Seven");
		stream.dispatchOn(env).consume(ev -> {
					System.out.println(ev);
					trreadPoolDispatcher.dispatch(ev, consumer, errorConsumer);
				});

		latch.await(15, TimeUnit.SECONDS); // Wait for task to execute

	}
}
