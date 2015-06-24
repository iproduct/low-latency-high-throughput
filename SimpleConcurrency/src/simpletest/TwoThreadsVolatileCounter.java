package simpletest;

public class TwoThreadsVolatileCounter {
	static volatile long counter = 0;
	
	public static void main(String[] args) {

		//Second thread
		Thread second = new Thread(() -> {
			while (counter < 500000000) {
				counter++;
			}
		});

		long start = System.nanoTime();
		
		second.start();
		while (counter < 500000000) {
			counter++;
		}
		
		long end = System.nanoTime();

		System.out.println((end - start) / 1000000d + " ms"); // around 27000ms
																// on my laptop
																//  quad core i7@2.2GHz
	}

}
