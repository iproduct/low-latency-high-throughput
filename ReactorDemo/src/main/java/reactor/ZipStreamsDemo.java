package reactor;

import reactor.rx.Streams;

public class ZipStreamsDemo {
  public static void main(String... args) throws InterruptedException {
    Environment.initialize(); 

    Streams.zip(
			Streams.range(0, 3),
			Streams.from(new String[]{"Hello", "from", "Reactor", "Websocket"}),
			t2 -> t2).throttle(3000)
		.consume( t2 -> System.out.println(t2.getT1() + ": " + t2.getT2()));
	
    Thread.sleep(500); 
  }
}