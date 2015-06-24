package simpletest;

public class SynchronousCounter {

	public static void main(String[] args) {
		long start = System.nanoTime();
		long counter = 0;
		while (counter < 500000000){
			counter++;
		}
		long end = System.nanoTime();

		System.out.println((end - start) / 1000000d  + " ms"); //around 190ms on my laptop quad core i7@2.2GHz
	} 

}
