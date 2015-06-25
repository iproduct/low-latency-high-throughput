package simpledemo;

import java.util.Date;

import rx.Observable;
import rx.functions.Action1;

public class HelloRxJava {
	public static void hello(String... names) {
	    Observable.from(names).subscribe(new Action1<String>() {

	        @Override
	        public void call(String s) {
	            System.out.println("Hello " + s + "!");
	        }

	    });
	}
	public static void helloLambda(String... names) {
	    Observable.from(names).subscribe(s -> System.out.println("Hello " + s + "!"));
	}
	public static void main(String[] args) {
		helloLambda("Reactive", "Extensions");
		Observable.just("Hello, world!").subscribe(s -> System.out.println(s));
		Observable.just("Hello, world!").map(s -> s + " : on " + new Date())
			.subscribe(s -> System.out.println(s));
		Observable.from(new String[]{"Reactive", "Extensions", "Java"})
			.take(2).map(s -> s + " : on " + new Date())
			.subscribe(s -> System.out.println(s));
	}

}
