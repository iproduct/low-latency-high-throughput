package reactor;

import reactor.rx.broadcast.Broadcaster;

public class EnvironmentProperties {
	static {
		// Only done once, statically, and shared across this classloader
		Environment.initialize();
	}

	public static void main(String... args) throws InterruptedException {
		Environment env = Environment.get();
		System.out.println("reactor.dispatchers.threadPoolExecutor.type = "
				+ env.getProperty("reactor.dispatchers.threadPoolExecutor.type", "not set"));
		
		Broadcaster<String> b = Broadcaster.create(env); 
		b.dispatchOn(Environment.cachedDispatcher()) 
	        .map(String::toUpperCase) 
	        .filter(s -> s.startsWith("HELLO"))
	        .consume(s -> System.out.printf("s=%s%n", s)); 
		
		
		

		// Sink values into this Broadcaster
		b.onNext("Hello World!");
		// This won't print
		b.onNext("Goodbye World!");
		// This will print
		b.onNext("Hello Reactor!");
		
		b.accept("Hello Trayan");

		// Must wait for tasks in other threads to complete
		Thread.sleep(500);
	}
}