package reactor.core.processors;

import org.reactivestreams.Processor;
import org.slf4j.LoggerFactory;

import reactor.Environment;
import reactor.core.processor.RingBufferProcessor;
import reactor.core.processor.RingBufferWorkProcessor;
import reactor.rx.Stream;
import reactor.rx.Streams;
import ch.qos.logback.classic.Logger;

public class RingBufferWorkProcessorDemo {
	final static Logger LOG = (Logger) LoggerFactory
			.getLogger(RingBufferWorkProcessorDemo.class);

	public static void main(String[] args) throws InterruptedException {
		Environment env = Environment.initialize();

		Processor<String, String> p = RingBufferWorkProcessor.create("testProcessor", 32); 
		Stream<String> s1 = Streams.wrap(p); 

		s1.consume(ev -> System.out.println(Thread.currentThread() + " data=" + ev)); 
		s1.consume(ev -> System.out.println(Thread.currentThread() + " data=" + ev)); 
		s1.consume(ev -> System.out.println(Thread.currentThread() + " data=" + ev)); 
		
		p.onNext("One");
		p.onNext("Two");
		p.onNext("Three");
		p.onNext("Four");
		p.onNext("Five");
		p.onComplete();
		
		Environment.terminate();

	}
}
