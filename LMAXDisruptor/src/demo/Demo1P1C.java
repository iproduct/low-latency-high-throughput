package demo;

import java.util.BitSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lmax.disruptor.BatchEventProcessor;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.SleepingWaitStrategy;

import event.ValueEvent;

public class Demo1P1C {
	public static final int RING_SIZE = 128;
	public static final int SAMPLES_SIZE = 500000;
	public static final int NUMBER_CONSUMERS = 1;
	public static long start, end;

	public static final ExecutorService EXECUTOR = Executors
			.newFixedThreadPool(NUMBER_CONSUMERS);

	public static void main(String[] args) {
//		final EventHandler<ValueEvent> handler = new EventHandler<ValueEvent>() {
//			private BitSet bset = new BitSet(SAMPLES_SIZE);
//			public void onEvent(final ValueEvent event, final long sequence,
//					final boolean endOfBatch) throws Exception {
//				bset.set((int)(event.getValue()));
//				if(event.getValue() == SAMPLES_SIZE - 1) { 
//					end = System.nanoTime();
//					System.out.println("Number samples received: " + bset.cardinality());	
//					System.out.println((end - start) / 1000000d  + " ms"); //around 4100ms on my laptop i7@2.2GHz
//				}
//			}
//		};
		BitSet bset = new BitSet(SAMPLES_SIZE);

		RingBuffer<ValueEvent> ringBuffer = RingBuffer.createSingleProducer(
				ValueEvent.EVENT_FACTORY, RING_SIZE, new SleepingWaitStrategy());

		SequenceBarrier barrier = ringBuffer.newBarrier();
		BatchEventProcessor<ValueEvent> eventProcessor = new BatchEventProcessor<ValueEvent>(
				ringBuffer,	barrier, 
				//using lambda for handling events
				(event,sequence, endOfBatch) -> { 
					bset.set((int)(event.getValue()));
					if(event.getValue() == SAMPLES_SIZE - 1) { 
						end = System.nanoTime();
						System.out.println("Number samples received: " + bset.cardinality());	
						System.out.println((end - start) / 1000000d  + " ms"); //around 4100ms on my laptop i7@2.2GHz
					}
				}
		);
		ringBuffer.addGatingSequences(eventProcessor.getSequence());

		// Each EventProcessor can run on a separate thread
		EXECUTOR.submit(eventProcessor);
		
		start = System.nanoTime();
		for(int i = 0;  i  < SAMPLES_SIZE; i++) {
			// Publishers claim events in sequence
			long sequence = ringBuffer.next();
			ValueEvent event = ringBuffer.get(sequence);

			event.setValue(i); // this could be more complex with multiple fields
			
			// make the event available to EventProcessors
			ringBuffer.publish(sequence);   
		}

	}
}
