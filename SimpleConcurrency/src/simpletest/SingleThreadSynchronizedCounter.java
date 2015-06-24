package simpletest;

public class SingleThreadSynchronizedCounter {

	public static void main(String[] args) {
		long start = System.nanoTime();
		Object monitor = new Object();
		long counter = 0;
		while (counter < 500000000){
			synchronized(monitor){
				counter++;
			}
		}
		long end = System.nanoTime();

		System.out.println((end - start) / 1000000d  + " ms"); //around 15000ms on my laptop  quad core i7@2.2GHz
	} 

}
