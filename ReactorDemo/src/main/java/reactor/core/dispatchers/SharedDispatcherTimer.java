package reactor.core.dispatchers;

import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.slf4j.LoggerFactory;

import reactor.Environment;
import reactor.core.Dispatcher;
import reactor.core.support.Assert;
import reactor.fn.Pausable;
import reactor.fn.timer.Timer;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;

public class SharedDispatcherTimer {
	final static Logger LOG = (Logger) LoggerFactory.getLogger(SharedDispatcherTimer.class);

	public static void main(String[] args) throws InterruptedException {
		Environment env = Environment.initialize();
		CountDownLatch latch = new CountDownLatch(1);

		//Current registered environment is the same than the one initialized
		Assert.isTrue(Environment.get() == env);

		//Find a dispatcher named "shared"
		Dispatcher d  = Environment.dispatcher("shared");

		//get the Timer bound to this environment
		Timer timer = Environment.timer();
		Pausable p = timer.schedule(time -> System.out.println(new Date(time)), 
				1, TimeUnit.SECONDS);
		
		timer.submit(time -> {LOG.info("Cancelling timer task."); p.cancel();}, 
				10, TimeUnit.SECONDS);
		
		timer.submit(time -> {
				LOG.info("Shutting down ..."); 
				latch.countDown(); 
				Environment.terminate();
			}, 
			13, TimeUnit.SECONDS);
		
		latch.await();
		
		// assume SLF4J is bound to logback-classic in the current environment
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		loggerContext.stop();
	}

}
