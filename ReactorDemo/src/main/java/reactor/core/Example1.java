package reactor.core;

import reactor.Environment;
import reactor.fn.Consumer;

public class Example1 {

	public static void main(String[] args) {
		//Initialize context and get default dispatcher
		Environment.initializeIfEmpty();

		//RingBufferDispatcher with 8192 slots by default
		Dispatcher dispatcher = Environment.sharedDispatcher();

		//Create a callback
		Consumer<Integer> c = data ->
		        System.out.format("some data arrived: %s\n", data);

		//Create an error callback
		Consumer<Throwable> errorHandler = ex -> ex.printStackTrace();

		//Dispatch data asynchronously
		dispatcher.dispatch(1234, c, errorHandler);
		dispatcher.dispatch(5678, c, errorHandler);

		Environment.terminate();
	}

}
