package reactor.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class WhatsTheProblem {

	// What are the problems with this code?
	
	public static <T> List<T> execiteMyTask(String sql) {
		ExecutorService  threadPool = Executors.newFixedThreadPool(8);

		final List<T> batches = new ArrayList<T>();

		Callable<T> t = new Callable<T>() { 

		    public T call() {
	            T result = callDatabase(sql); 
		        synchronized(batches) { 
		            batches.add(result);    
		        }
		        return result;
		    }

			private T callDatabase(String sql) {
				// TODO Auto-generated method stub
				return null;
			}
		};

		Future<T> f = threadPool.submit(t); 
		try {
			T result = f.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return batches;
	}
}
