package simpletest;

public class TwoThreadsSynchronizedCounter {
	static long counter = 0;
	static Object monitor = new Object();
	
	public static void main(String[] args) {

		//Second thread
		Thread second = new Thread(() -> {
			long value = 0;
			while (value < 500000000) {
				synchronized (monitor) {
					value = counter++;
				}
			}
		});

		long start = System.nanoTime();
		
		second.start();
		long value = 0;
		while (value < 500000000) {
			synchronized (monitor) {
				value = counter++;
			}
		}
		
		long end = System.nanoTime();

		System.out.println((end - start) / 1000000d + " ms"); // around 21000ms
																// on my laptop
																//  quad core i7@2.2GHz
	}

}
