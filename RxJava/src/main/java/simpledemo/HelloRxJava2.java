package simpledemo;

import java.util.Date;
import rx.Observable;

public class HelloRxJava2 {
	public static void helloLambda(String... names) {
	    Observable.from(names)
	    	.take(2).map(s -> s + " : on " + new Date())
	    	.subscribe(System.out::println);
	}
	public static void main(String[] args) {
		helloLambda("Reactive", "Extensions", "Java");
	}
}
