package reactor.io.demos;

import java.util.List;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import reactor.Environment;
import reactor.io.buffer.Buffer;

public class RingBufferProcessorDemo {
	final static Logger LOG =  LoggerFactory
			.getLogger(RingBufferProcessorDemo.class);

	public static void main(String[] args) throws InterruptedException {
		Environment env = Environment.initialize();

		byte delimiter = (byte) ';';
		byte innerDelimiter = (byte) ',';

		Buffer buffer = Buffer.wrap("a;b-1,b-2,;c;d;");

		List<Buffer.View> views = buffer.split(delimiter);

		int viewCount = views.size();
		Assert.assertEquals(viewCount, 4);

		for (Buffer.View view : views) {
		    System.out.println(view.get().asString()); //prints "a" then "b-1,b-2", then "c" and finally "d"

		    if(view.get().asString().indexOf(innerDelimiter) != -1){
		      for(Buffer.View innerView : view.get().split(innerDelimiter)){
		        System.out.println(innerView.get().asString()); //prints "b-1" and "b-2"
		      }
		    }
		}		
		
		Environment.terminate();
	}
}
