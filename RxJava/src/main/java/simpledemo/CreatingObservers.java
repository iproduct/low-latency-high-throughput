package simpledemo;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;

public class CreatingObservers {
	public static void helloLambda(String... names) {

		Observable<String> o = Observable.just("one object");
	    Observable.from(names)
	    	.take(2).map(s -> s + " : on " + new Date())
	    	.subscribe(System.out::println);
	}
	
	/**
	 * This example shows a custom Observable that blocks 
	 * when subscribed to (does not spawn an extra thread).
	 */
	private static Observable<String> customObservableBlocking() {
	    return Observable.create( aSubscriber -> {
	        Observable.range(1,10).forEach(i -> {
	            if (!aSubscriber.isUnsubscribed()) {
	                aSubscriber.onNext("value_" + i);
	            }
	        });
	        // after sending all values we complete the sequence
	        if (!aSubscriber.isUnsubscribed()) {
	            aSubscriber.onCompleted();
	        }
	    });
	}

	/**
	 * This example shows a custom asyncronous Observable that does not block
	 * when subscribed to (spawns an extra thread).
	 */
	private static Observable<String> customObservableAsync() {
	    return Observable.<String>create( aSubscriber -> {
	        Observable.range(1,10)
	        .forEach(i -> {
	            if (aSubscriber.isUnsubscribed()) {
	                return;
	            }
	            aSubscriber.onNext("value_" + i);
	        });
	        // after sending all values we complete the sequence
	        if (!aSubscriber.isUnsubscribed()) {
	            aSubscriber.onCompleted();
	        }
	    }).subscribeOn(Schedulers.newThread());
	}

	public static void main(String[] args) throws InterruptedException {
		Observable<String> o1 = Observable.from(new String[]{"a", "b", "c"});
		o1.subscribe(System.out::println);

		List<Integer> list = Arrays.asList(new Integer[]{5, 6, 7, 8});
		Observable<Integer> o2 = Observable.from(list);
		o2.subscribe(System.out::println, 
				ex -> ex.printStackTrace(), () -> System.out.println("Completed"));
		
//		// Call customObservableBlocking()
//		System.out.println("\nBlocking:");
//		customObservableBlocking().subscribe(System.out::println, 
//				ex -> ex.printStackTrace(), () -> System.out.println("Completed"));

		// Call customObservableAsync()
		System.out.println("\nNon-Blocking:");
		Observable<String> o = customObservableAsync();
		o.subscribe(s -> {System.out.println(s.toUpperCase());},
				ex -> ex.printStackTrace(), () -> System.out.println("Completed"));
		o.subscribe(System.out::println, 
				ex -> ex.printStackTrace(), () -> System.out.println("Completed"));
		
		Thread.sleep(100); //wait async observable to complete

	}
}
