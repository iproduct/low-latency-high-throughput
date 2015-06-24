package simpletest;

import java.util.concurrent.atomic.LongAdder;

public class TwoThreadsCASCounterLongAdder {
	static LongAdder counter = new LongAdder(); //initially 0
	
	public static void main(String[] args) {

		//Second thread
		Thread second = new Thread(() -> {
			while (counter.longValue() < 500000000) {
				counter.increment();
			}
		});

		long start = System.nanoTime();
		
		second.start();
		while (counter.longValue() < 500000000) {
			counter.increment();
		}
		
		long end = System.nanoTime();

		System.out.println((end - start) / 1000000d + " ms"); // around 12800ms
																// on my laptop
																//  quad core i7@2.2GHz
	}

}
