import java.util.Vector;

public class MemoryLeak {

	public static void main(String[] args) {

		int MAX_CONSUMERS = 10000;
		int SLEEP_BETWEEN_ALLOCS = 5;

		ConsumerContainer<MemoryConsumer> objectHolder = new ConsumerContainer<>();

		while (objectHolder.size() < MAX_CONSUMERS) {
			System.out.println("Allocating object "
					+ Integer.toString(objectHolder.size()));
			objectHolder.add(new MemoryConsumer());
			try {
				Thread.sleep(SLEEP_BETWEEN_ALLOCS);
			} catch (InterruptedException ie) {
				// Do nothing.
			}
		} // while.
	} // main.

} // End of MemoryLeak.

/** Named container class to hold object references. */
class ConsumerContainer<E> extends Vector<E> {
	private static final long serialVersionUID = 1L;
}

/** Class that consumes a fixed amount of memory. */
class MemoryConsumer {
	public static final int MEMORY_BLOCK = 1024;
	public byte[] memoryHoldingArray;

	MemoryConsumer() {
		memoryHoldingArray = new byte[MEMORY_BLOCK];
	}
} // End MemoryConsumer.