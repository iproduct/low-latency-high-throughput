package event;

import com.lmax.disruptor.EventFactory;

public final class ValueEvent
{
    private long value;

    public long getValue()
    {
        return value;
    }

    public void setValue(final long value)
    {
        this.value = value;
    }

    public final static EventFactory<ValueEvent> EVENT_FACTORY = new EventFactory<ValueEvent>()
    {
        public ValueEvent newInstance()
        {
            return new ValueEvent();
        }
    };

	@Override
	public String toString() {
		return "ValueEvent [value=" + value + "]";
	}
    
}