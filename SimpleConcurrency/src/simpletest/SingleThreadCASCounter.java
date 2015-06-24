package simpletest;

import java.util.concurrent.atomic.AtomicLong;

public class SingleThreadCASCounter {

	public static void main(String[] args) {
		AtomicLong counter = new AtomicLong(0);
		long start = System.nanoTime();
		
		while (counter.incrementAndGet() < 500000000){}
		
		long end = System.nanoTime();

		System.out.println((end - start) / 1000000d  + " ms"); //around 4100ms on my laptop i7@2.2GHz
	} 

}
