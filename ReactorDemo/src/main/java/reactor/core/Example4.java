package reactor.core;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import reactor.fn.tuple.Tuple;
import reactor.fn.tuple.Tuple2;

public class Example4 {

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

		// BiConsumer using tuples
		Consumer<Tuple2<Consumer<String>, String>> biConsumer = tuple -> {
			for (int i = 0; i < 10; i++) {
				// Correct typing, compiler happy
				tuple.getT1().accept(tuple.getT2());
			}
		};

		biConsumer.accept(Tuple.of(consumer,
				transformation.apply(supplier.get())));
	}
}
