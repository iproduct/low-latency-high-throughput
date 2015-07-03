package reactor.core;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;


public class Example3 {

	public static void main(String[] args) {
		Consumer<String> consumer = new Consumer<String>() {
			@Override
			public void accept(String value) {
				System.out.println(value);
			}
		};

		// Now in Java 8 style for brievety
		Function<Integer, String> transformation = integer -> "" + integer;

		Supplier<Integer> supplier = () -> 123;

		BiConsumer<Consumer<String>, String> biConsumer = (callback, value) -> {
			for (int i = 0; i < 10; i++) {
				// lazy evaluate the final logic to run
				callback.accept(value);
			}
		};

		// note how the execution flows from supplier to biconsumer
		biConsumer.accept(consumer, transformation.apply(supplier.get()));
	}
}
