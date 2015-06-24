package demo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lmax.disruptor.BatchEventProcessor;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.Sequence;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.SleepingWaitStrategy;

import event.ValueEvent;
import eventhandler.ValueEventHandler1PMC;
import eventhandler.ValueEventHandler1PMCSequenceFirst;
import eventhandler.ValueEventHandler1PMCSequenceSecond;

public class Demo1PMCSequence {
	public static final int RING_SIZE = 128;
	public static final int SAMPLES_SIZE = 5000;
	public static final int NUMBER_CONSUMERS = 2;
	public static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(NUMBER_CONSUMERS);
	public static long start;

	public static void main(String[] args) {
				
		RingBuffer<ValueEvent> ringBuffer = RingBuffer.createSingleProducer(
				ValueEvent.EVENT_FACTORY, RING_SIZE, new SleepingWaitStrategy());

		start = System.nanoTime();

		//Create first consumer
		ValueEventHandler1PMCSequenceFirst handler = new ValueEventHandler1PMCSequenceFirst(start);
		SequenceBarrier barrier;
		barrier = ringBuffer.newBarrier();
		BatchEventProcessor<ValueEvent> firstEventProcessor = new BatchEventProcessor<ValueEvent>(
				ringBuffer,	barrier, handler);
//		ringBuffer.addGatingSequences(firstEventProcessor.getSequence());
		
		// Each EventProcessor can run on a separate thread
		EXECUTOR.submit(firstEventProcessor);

		//Create second consumer
		ValueEventHandler1PMCSequenceSecond handler2 = new ValueEventHandler1PMCSequenceSecond(start);
		SequenceBarrier barrier2 = ringBuffer.newBarrier(firstEventProcessor.getSequence());
		BatchEventProcessor<ValueEvent> secondEventProcessor = new BatchEventProcessor<ValueEvent>(
				ringBuffer,	barrier2, handler2);
		ringBuffer.addGatingSequences(secondEventProcessor.getSequence());
		
		// Each EventProcessor can run on a separate thread
		EXECUTOR.submit(secondEventProcessor);

		
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
