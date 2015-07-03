package simpletest;

public class SynchronousCounter {

	public static void main(String[] args) {
		long counter = 0;
		//Warming
		counter = testIncrement(counter);
		System.out.println("Warmed up counter: " + counter); 
		
		counter = 0;
		long start = System.nanoTime();
		//Real benchmark
		counter = testIncrement(counter);
		long end = System.nanoTime();

		System.out.println((end - start) / 1000000d  + " ms"); //around 190ms on my laptop quad core i7@2.2GHz
	}

	private static long testIncrement(long counter) {
		while (counter < 500000000){
			counter++;
		}
		return counter;
	} 

}
