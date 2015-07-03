package reactor.core;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import reactor.core.processor.RingBufferProcessor;

public class Example2 {

	public static void main(String[] args) {
		// standalone async processor
		RingBufferProcessor<Integer> processor = RingBufferProcessor.create();

		// send data, will be kept safe until a subscriber attaches to the
		// processor
		processor.onNext(1234);
		processor.onNext(5678);

		// consume integer data
		processor.subscribe(new Subscriber<Integer>() {

			public void onSubscribe(Subscription s) {
				// unbounded subscriber
				s.request(Long.MAX_VALUE);
			}

			public void onNext(Integer data) {
				System.out.println(data);
			}

			public void onError(Throwable err) {
				err.printStackTrace();
			}

			public void onComplete() {
				System.out.println("done!");
			}
		});

		// Shutdown internal thread and call complete
		processor.onComplete();
	}

}
