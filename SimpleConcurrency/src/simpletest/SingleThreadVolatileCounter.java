package simpletest;

public class SingleThreadVolatileCounter {
	static volatile long counter = 0;

	public static void main(String[] args) {

		long start = System.nanoTime();

		while (counter < 500000000) {
			counter++;
		}

		long end = System.nanoTime();

		System.out.println((end - start) / 1000000d + " ms"); // around 4100ms
																// on my laptop														// quad core
																// i7@2.2GHz
	}
}
