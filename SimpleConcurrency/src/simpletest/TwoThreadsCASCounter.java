package simpletest;

import java.util.concurrent.atomic.AtomicLong;

public class TwoThreadsCASCounter {
	static AtomicLong counter = new AtomicLong(0);
	
	public static void main(String[] args) {

		//Second thread
		Thread second = new Thread(() -> {
			while (counter.incrementAndGet() < 500000000) {}
		});

		long start = System.nanoTime();
		
		second.start();
		while (counter.incrementAndGet() < 500000000) {}
		
		long end = System.nanoTime();

		System.out.println((end - start) / 1000000d + " ms"); // around 12000ms
																// on my laptop
																//  quad core i7@2.2GHz
	}

}
