package org.gradle;

import reactor.Environment;
import reactor.rx.broadcast.Broadcaster;

public class ReactorHelloWorld {
  public static void main(String... args) throws InterruptedException {
    Environment.initialize(); 

    Broadcaster<String> sink = Broadcaster.create(Environment.get()); 

    sink.dispatchOn(Environment.cachedDispatcher()) 
        .map(String::toUpperCase) 
        .filter(s -> s.startsWith("HELLO"))
        .consume(s -> System.out.printf("s=%s%n", s)); 

    sink.onNext("Hello World!"); 
    sink.onNext("Goodbye World!");
    sink.onNext("Hello Reactor!");
	
    Thread.sleep(500); 
  }
}