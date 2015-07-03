package org.gradle;

import rx.Observable;


public class HelloGradle {
	
	public static void processAll(String... args){
		Observable.from(args).skip(1).take(4)
			.map(String::toUpperCase)
			.filter(ev -> ev.indexOf("JAVA") >= 0)
			.subscribe(System.out::println);
	}

	public static void main(String[] args) {
		processAll("Hello", "Reactive", "Java World", "from", "RxJAVA");

	}

}
