package eventhandler;

import java.util.BitSet;
import java.util.List;
import java.util.stream.Collectors;

import com.lmax.disruptor.EventHandler;

import demo.Demo1PMCSequence;
import event.ValueEvent;

public class ValueEventHandler1PMCSequenceSecond implements EventHandler<ValueEvent> {
	private BitSet valuesReceived = new BitSet(Demo1PMCSequence.SAMPLES_SIZE);
	private long start;
	
	public ValueEventHandler1PMCSequenceSecond(long start){
		this.start = start;
	}
	
	public long getValuesCardinality() {
		return valuesReceived.cardinality();
	}
	
	public void onEvent(final ValueEvent event, final long sequence,
			final boolean endOfBatch) throws Exception {
//		System.out.println(event.getValue());
		int newValue = (int)(event.getValue()) - 1000;
		valuesReceived.set(newValue);
		if(newValue == Demo1PMCSequence.SAMPLES_SIZE - 1) { 
			long end = System.nanoTime();
			System.out.println("Second: Number samples received: " + getValuesCardinality());	
			System.out.println((end - start) / 1000000  + " ms"); //around 4100ms on my laptop i7@2.2GHz
		}
	}
}
