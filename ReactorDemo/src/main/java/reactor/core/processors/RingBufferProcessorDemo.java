package reactor.core.processors;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.reactivestreams.Processor;
import org.slf4j.LoggerFactory;

import reactor.Environment;
import reactor.core.processor.RingBufferProcessor;
import reactor.core.reactivestreams.PublisherFactory;
import reactor.rx.Stream;
import reactor.rx.Streams;
import reactor.rx.stream.PublisherStream;
import ch.qos.logback.classic.Logger;

public class RingBufferProcessorDemo {
	final static Logger LOG = (Logger) LoggerFactory
			.getLogger(RingBufferProcessorDemo.class);

	public static void main(String[] args) throws InterruptedException {
		Environment env = Environment.initialize();

		Processor<String, String> p = RingBufferProcessor.create("testProcessor", 32); 
		Stream<String> s1 = Streams.wrap(p); 

		s1.consume(ev -> System.out.println(Thread.currentThread() + " data=" + ev)); 
		s1.consume(ev -> System.out.println(Thread.currentThread() + " data=" + ev)); 
		s1.consume(ev -> System.out.println(Thread.currentThread() + " data=" + ev)); 
		
		p.onNext("One");
		p.onNext("Two");
		p.onComplete();
		
		Environment.terminate();

	}
}
