package eventhandler;

import java.util.BitSet;
import java.util.List;
import java.util.stream.Collectors;

import com.lmax.disruptor.EventHandler;

import demo.Demo1PMC;
import event.ValueEvent;

public class ValueEventHandler1PMC implements EventHandler<ValueEvent> {
	private BitSet valuesReceived = new BitSet(Demo1PMC.SAMPLES_SIZE);
	private long start;
	private List<ValueEventHandler1PMC> handlers;
	
	public ValueEventHandler1PMC(long start, List<ValueEventHandler1PMC> handlers){
		this.start = start;
		this.handlers = handlers;
	}
	
	public long getValuesCardinality() {
		return valuesReceived.cardinality();
	}
	
	public void onEvent(final ValueEvent event, final long sequence,
			final boolean endOfBatch) throws Exception {
		valuesReceived.set((int)(event.getValue()));
		if(event.getValue() == Demo1PMC.SAMPLES_SIZE - 1) { 
			long end = System.nanoTime();
			Long sum = handlers.stream()
					.map(h -> {System.out.println(h.getValuesCardinality()); return h;})
					.collect(Collectors.summingLong(ValueEventHandler1PMC::getValuesCardinality));
			System.out.println("Number samples received: " + sum);	
			System.out.println((end - start) / 1000000  + " ms"); //around 4100ms on my laptop i7@2.2GHz
		}
	}
}
