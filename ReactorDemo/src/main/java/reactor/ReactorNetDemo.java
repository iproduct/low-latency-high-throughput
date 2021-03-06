package reactor;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import reactor.bus.EventBus;
import reactor.fn.timer.Timer;
import reactor.io.buffer.Buffer;
import reactor.io.codec.StandardCodecs;
import reactor.io.net.NetStreams;
import reactor.io.net.ReactorChannelHandler;
import reactor.io.net.http.HttpChannel;
import reactor.io.net.http.HttpServer;
import reactor.quickstart.TradeServer;
import reactor.rx.Stream;
import reactor.rx.Streams;
import reactor.rx.broadcast.Broadcaster;

public class ReactorNetDemo {
	private HttpServer<String, String> httpServer;
	private Environment env;
	private Timer timer;
	private Broadcaster<String> channelBroadcaster;
	TradeServer server;
	EventBus serverReactor;
	
	volatile Map<String,Integer> wishes = new ConcurrentHashMap<>();
	Wish[] sampleWishes = {
		new Wish("RxJava", 15 ),
		new Wish("Reactor", 20 ),
		new Wish("RxNext",  8 ),
		new Wish("Angular 2", 5 ),
		new Wish("React", 6 ),
		new Wish("Spring Integration", 12),
		new Wish("JavaEE 8 MVC 1.0", 3 ),
		new Wish("JSONB & JSON-P",  9),
		new Wish("CDI 2.0",  7),
		new Wish("JSR 370 - JAX-RS 2.1", 15 ),
		new Wish("Servlet 4.0 - HTTP/2", 21 )
	};
	
//	Selector tradeExecute;

	public ReactorNetDemo() {
		try {
			setup();
			env = Environment.get();
			timer = env.getTimer();
			channelBroadcaster = Broadcaster.create(env);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//Initialize wish map with sample data
		for(Wish wish :sampleWishes) {
			wishes.put(wish.getTitle(), wish.getClicks());
		}

	}

	public void setup() throws InterruptedException {
		Environment.initializeIfEmpty().assignErrorJournal();

//		server = new TradeServer();
//
//		// Use a Reactor to dispatch events using the high-speed Dispatcher
//		serverReactor = EventBus.create(Environment.get());
//
//		// Create a single key and Selector for efficiency
//		tradeExecute = Selectors.object("trade.execute");
//
//		// For each Trade event, execute that on the server and notify connected clients
//		// because each client that connects links to the serverReactor
//		serverReactor.on(tradeExecute, (Event<Trade> ev) -> {
//			server.execute(ev.getData());
//
//		});

		
		setupServer();
		
	}

	private void setupServer() throws InterruptedException {
		httpServer = NetStreams.<String, String>httpServer(server -> server
				.codec(StandardCodecs.STRING_CODEC)
				.listen("localhost", 80)
				.dispatcher(Environment.sharedDispatcher()));
		httpServer.get("/wsdemo", getHandler());
		httpServer.ws("/wsdemo/ws", wsHandler());

		httpServer.start().awaitSuccess();
	}

	public void teardown() throws InterruptedException {
		httpServer.shutdown().await();
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		loggerContext.stop();
	}

	private ReactorChannelHandler<String, String, HttpChannel<String, String>> getHandler() {
		return channel -> {
			channel.headers()
					.entries()
					.forEach(
							entry1 -> System.out.println(String.format(
									"header [%s=>%s]", entry1.getKey(),
									entry1.getValue())));
			String response;
			try {
				response = getWebPage("src/main/java/webapp/ws.html");
			} catch (IOException e) {
				e.printStackTrace();
				response = e.getMessage();
			}
			System.out.println(String.format("%s from thread %s",
					response.toString(), Thread.currentThread()));
			return channel.writeWith(Streams.just(response
					.toString()));
		};
	}

	private ReactorChannelHandler<String, String, HttpChannel<String, String>> wsHandler() {
		return channel -> {
			System.out.println("Connected a websocket client: " + channel.remoteAddress());
			channel.headers()
					.entries()
					.forEach(
							entry1 -> System.out.println(String.format(
									"header [%s=>%s]", entry1.getKey(),
									entry1.getValue())));	
					
			
			
			channel.consume(		
				s -> {
				System.out.printf("%s greeting = %s%n", Thread.currentThread(), s);
				channelBroadcaster.onNext(s);
				String[] parts = s.split(":");
//				System.out.println(Arrays.toString(parts));
				if(parts.length == 2) {
					switch(parts[0]) {    //Command part
						case "+1" : wishPlusOne(parts[1]);  //Data part
					}	
				}
			});
			
			//signal to send initial data to client
			timer.submit(time -> {System.out.println("Timeout"); channelBroadcaster.onNext("");}, 50, TimeUnit.MILLISECONDS);

			return channelBroadcaster
					.flatMap(str -> channel.writeWith(
						Streams.just(
							wishes.entrySet().stream()
								.sorted(Comparator.comparing(entry -> -entry.getValue()))	
								.map(entry -> entry.getKey() + ":" + entry.getValue())						
								.reduce("", (acum, val) -> {
									return (acum.length() > 0) ? acum + "," + val : val;
								})
						).map(x -> {System.out.println("TO BE SENT: " + x); return x;})
					));
//			return Streams.from(new String[]{"Hello", "from", "Reactor", "Websocket"})
//					.throttle(10000).flatMap(str -> channel.writeWith(
//						Streams.just(
//							wishes.entrySet().stream()
//								.map(entry -> entry.getKey() + ":" + entry.getValue())
//								.reduce("", (acum, val) -> {
//									return (acum.length() > 0) ? acum + "," + val : val;
//								})
//						).map(x -> {System.out.println("TO BE SENT: " + x); return x;})
//					));
////			return Streams.from(new String[]{"Hello", "from", "Reactor", "Websocket"})
//					.throttle(20000).flatMap(str -> channel.writeWith(Streams.just(str)));
		};
	}


	private void wishPlusOne(String wishTitle) {
		Integer wishCount = wishes.get(wishTitle);
		if (wishCount == null) {
			wishes.put(wishTitle, 1);
		} else {
			wishes.put(wishTitle, wishCount + 1);
		}
		System.out.println(wishes);
	}

	private void get(String path, SocketAddress address) {
		try {
			StringBuilder request = new StringBuilder()
					.append(String.format("GET %s HTTP/1.1\r\n", path))
					.append("Connection: Keep-Alive\r\n").append("\r\n");
			java.nio.channels.SocketChannel channel = java.nio.channels.SocketChannel
					.open(address);
			System.out.println(String.format("get: request >> [%s]",
					request.toString()));
			channel.write(Buffer.wrap(request.toString()).byteBuffer());
			ByteBuffer buf = ByteBuffer.allocate(4 * 1024);
			while (channel.read(buf) > -1)
				;
			String response = new String(buf.array());
			System.out.println(String.format("get: << Response: %s", response));
			channel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void post(String path, String data, SocketAddress address) {
		try {
			StringBuilder request = new StringBuilder().append(
					String.format("POST %s HTTP/1.1\r\n", path)).append(
					"Connection: Keep-Alive\r\n");
			request.append(
					String.format("Content-Length: %s\r\n", data.length()))
					.append("\r\n").append(data).append("\r\n");
			java.nio.channels.SocketChannel channel = java.nio.channels.SocketChannel
					.open(address);
			System.out.println(String.format("post: request >> [%s]",
					request.toString()));
			channel.write(Buffer.wrap(request.toString()).byteBuffer());
			ByteBuffer buf = ByteBuffer.allocate(4 * 1024);
			while (channel.read(buf) > -1)
				;
			String response = new String(buf.array());
			System.out
					.println(String.format("post: << Response: %s", response));
			channel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String getWebPage(String fileName) throws IOException{
		StringBuilder sb = new StringBuilder();
		Path filePath = Paths.get(fileName);
		Files.lines(filePath).forEach(line -> sb.append(line).append("\n"));
		return sb.toString();
	}

	public static void main(String... args) throws InterruptedException,
			IOException {
		ReactorNetDemo demoHttp = new ReactorNetDemo();
		// Must wait for tasks in other threads to complete
		Scanner sc = new Scanner(System.in);
		System.out.println("Hit <Enter> to stop the server ...");
		sc.nextLine();
		System.out.println("Stopping the HTTP server ...");
		demoHttp.teardown();
		System.out.println("HTTP server stopped successfully.");
		

	}
}