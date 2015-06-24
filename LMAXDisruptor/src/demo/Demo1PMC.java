package demo;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lmax.disruptor.BatchEventProcessor;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.SleepingWaitStrategy;

import event.ValueEvent;
import eventhandler.ValueEventHandler1PMC;

public class Demo1PMC {
	public static final int RING_SIZE = 128;
	public static final int SAMPLES_SIZE = 500000;
	public static final int NUMBER_CONSUMERS = 8;
	public static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(NUMBER_CONSUMERS);
	public static long start;

	public static void main(String[] args) {
		
		final List<ValueEventHandler1PMC> handlers = new ArrayList<>(NUMBER_CONSUMERS);
				
		RingBuffer<ValueEvent> ringBuffer = RingBuffer.createSingleProducer(
				ValueEvent.EVENT_FACTORY, RING_SIZE, new SleepingWaitStrategy());

		start = System.nanoTime();

		//Create consumers
		for(int i = 0;  i < NUMBER_CONSUMERS; i++) {
			ValueEventHandler1PMC handler = new ValueEventHandler1PMC(start, handlers);
			handlers.add(handler);
			SequenceBarrier barrier = ringBuffer.newBarrier();
			BatchEventProcessor<ValueEvent> eventProcessor = new BatchEventProcessor<ValueEvent>(
					ringBuffer,	barrier, handler);
			ringBuffer.addGatingSequences(eventProcessor.getSequence());
	
			// Each EventProcessor can run on a separate thread
			EXECUTOR.submit(eventProcessor);
		}
		
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
