package eventhandler;

import java.util.BitSet;

import com.lmax.disruptor.EventHandler;

import demo.Demo1PMCSequence;
import event.ValueEvent;

public class ValueEventHandler1PMCSequenceFirst implements EventHandler<ValueEvent> {
	private BitSet valuesReceived = new BitSet(Demo1PMCSequence.SAMPLES_SIZE);
	private long start;
	
	public ValueEventHandler1PMCSequenceFirst(long start){
		this.start = start;
	}
	
	public long getValuesCardinality() {
		return valuesReceived.cardinality();
	}
	
	public void onEvent(final ValueEvent event, final long sequence,
			final boolean endOfBatch) throws Exception {
		valuesReceived.set((int)(event.getValue()));
		long value = event.getValue();
		event.setValue(value + 1000);
		if(value == Demo1PMCSequence.SAMPLES_SIZE - 1) { 
			long end = System.nanoTime();
			System.out.println("First: Number samples received: " + getValuesCardinality());	
			System.out.println((end - start) / 1000000  + " ms"); //around 4100ms on my laptop i7@2.2GHz
		}
	}
}
