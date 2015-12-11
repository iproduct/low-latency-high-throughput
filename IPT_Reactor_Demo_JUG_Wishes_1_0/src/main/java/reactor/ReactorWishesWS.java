package reactor;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import reactor.fn.timer.Timer;
import reactor.io.codec.StandardCodecs;
import reactor.io.net.NetStreams;
import reactor.io.net.ReactorChannelHandler;
import reactor.io.net.http.HttpChannel;
import reactor.io.net.http.HttpServer;
import reactor.rx.Streams;
import reactor.rx.broadcast.Broadcaster;

public class ReactorWishesWS {
	private HttpServer<String, String> httpServer;
	private Environment env;
	private Timer timer;
	private Broadcaster<String> channelBroadcaster;
//	private EventBus serverReactor;
	
	private final Map<String,String> cache = new HashMap<>();
	private final Map<String,Integer> wishes = new ConcurrentHashMap<>();
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
	private static final Charset UTF_8 = Charset.forName("utf-8");
	private static final String GUID = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";

	public ReactorWishesWS() {
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
		setupServer();
		
	}

	private void setupServer() throws InterruptedException {
		httpServer = NetStreams.<String, String>httpServer(server -> server
				.codec(StandardCodecs.STRING_CODEC)
				.listen("localhost", 80)
				.dispatcher(Environment.sharedDispatcher()));
		httpServer.get("/", getHandler());
		httpServer.get("/index.html", getHandler());
		httpServer.get("/app/**", getHandler());
		httpServer.get("/node_modules/**", getHandler());
		httpServer.ws("/ws", wsHandler());

		httpServer.start().awaitSuccess();
	}

	public void teardown() throws InterruptedException {
		httpServer.shutdown().await();
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		loggerContext.stop();
	}

	private ReactorChannelHandler<String, String, HttpChannel<String, String>> getHandler() {
		return channel -> {
//			channel.headers()
//					.entries()
//					.forEach(
//							entry1 -> System.out.println(String.format(
//									"header [%s=>%s]", entry1.getKey(),
//									entry1.getValue())));
//			System.out.println(channel.uri());
			String uri = channel.uri();
			if (uri.equals("/")) 
				uri = "/index.html";
			String path = "src/main/webapp" + uri;	
			
			String response;
			try {
				response = getStaticResource(path);
			} catch (IOException e) {
				e.printStackTrace();
				response = e.getMessage();
			}
		
			return channel.writeWith(Streams.just(response
					.toString()));
		};
	}

	private ReactorChannelHandler<String, String, HttpChannel<String, String>> wsHandler() {
		return channel -> {
			System.out.println("Connected a websocket client: " + channel.remoteAddress());
//			channel.headers()
//					.entries()
//					.forEach(
//							entry1 -> System.out.println(String.format(
//									"header [%s=>%s]", entry1.getKey(),
//									entry1.getValue())));				
			channel.consume(		
				s -> {
				System.out.printf("%s greeting = %s%n", Thread.currentThread(), s);
				channelBroadcaster.onNext(s);
				String[] parts = s.split(":");
				if(parts.length == 2) {
					switch(parts[0]) {    //Command part
						case "+1" : wishPlusOne(parts[1]);  //Data part
					}	
				}
			});
			
			String key = channel.headers().get("Sec-WebSocket-Key").trim();
			byte[] sha1 = DigestUtils.sha1(key + GUID);
//			byte[] sha1 = DigestUtils.sha1("x3JJHMbDL1EzLkh9GBhXDw==" + GUID);
			String base64 = Base64.encodeBase64String(sha1);
//			System.out.println("KEY:" + key);
//			System.out.println("HEX_CODE:" + sha1);
//			System.out.println("BASE_64:" + base64);				
			
//			channel.responseStatus(Status.SWITCHING_PROTOCOLS);
			channel.responseHeaders()
				.add("Sec-WebSocket-Accept", base64)
				.add("Upgrade", "websocket")
				.add("Connection", "Upgrade")
				.add("Access-Control-Allow-Origin", channel.headers().get("Origin"));
			
			//signal to send initial data to client
			timer.submit(time -> {System.out.println("Timeout"); channelBroadcaster.onNext("");}, 100, TimeUnit.MILLISECONDS);

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

	private String getStaticResource(String fileName) throws IOException{
		String result;
//		if((result = cache.get(fileName)) == null) {
			Path filePath = Paths.get(fileName);
			result = new String(Files.readAllBytes(filePath), UTF_8);
//			cache.put(fileName, result);
//		}
		return result;
	}

	public static void main(String... args) throws InterruptedException,
			IOException {
		ReactorWishesWS demoHttp = new ReactorWishesWS();
		// Must wait for tasks in other threads to complete
		Scanner sc = new Scanner(System.in);
		System.out.println("Hit <Enter> to stop the server ...");
		sc.nextLine();
		System.out.println("Stopping the HTTP server ...");
		demoHttp.teardown();
		System.out.println("HTTP server stopped successfully.");
		sc.close();
	}
}