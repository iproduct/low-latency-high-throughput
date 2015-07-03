package reactor;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import reactor.io.buffer.Buffer;
import reactor.io.codec.StandardCodecs;
import reactor.io.net.NetStreams;
import reactor.io.net.ReactorChannelHandler;
import reactor.io.net.http.HttpChannel;
import reactor.io.net.http.HttpServer;
import reactor.rx.Streams;

public class ReactorNetDemo {
	private HttpServer<String, String> httpServer;
	private Environment env;

	public ReactorNetDemo() {
		try {
			setup();
			env = Environment.get();
			// get("/get/joe", httpServer.getListenAddress());
			// post("/post", URLEncoder.encode("Reactor", "UTF8"),
			// httpServer.getListenAddress());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public void setup() throws InterruptedException {
		Environment.initializeIfEmpty().assignErrorJournal();
		setupServer();
	}

	private void setupServer() throws InterruptedException {
		httpServer = NetStreams.<String, String>httpServer(server -> server
				.codec(StandardCodecs.STRING_CODEC)
				.listen("localhost", 8000)
				.dispatcher(Environment.sharedDispatcher()));
		httpServer.get("/wsdemo", getHandler());
//		httpServer.post("/post", postHandler());
		httpServer.ws("/wsdemo/ws", wsHandler());

		httpServer.start().awaitSuccess();
	}

	public void teardown() throws InterruptedException {
		httpServer.shutdown().await();
	}

	private ReactorChannelHandler<String, String, HttpChannel<String, String>> getHandler() {
		return channel -> {
			channel.headers()
					.entries()
					.forEach(
							entry1 -> System.out.println(String.format(
									"header [%s=>%s]", entry1.getKey(),
									entry1.getValue())));
//			channel.params()
//					.entrySet()
//					.forEach(
//							entry2 -> System.out.println(String.format(
//									"params [%s=>%s]", entry2.getKey(),
//									entry2.getValue())));

//			StringBuilder response = new StringBuilder().append("hello ")
//					.append(channel.params().get("name"));
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
//				return Streams.wrap(serverReactor.on(tradeExecute)).
//					map(ev -> ev.getData()).
//					cast(Trade.class).
//					window(1000).
//					flatMap(s ->
//					channel.writeWith( s.reduce(0f, (prev, trade) -> (trade.getPrice() + prev) / 2).map(Object::toString) )
//					);
				StringBuilder response = new StringBuilder().append("hello WS\n");
//						.append(channel.params().get("name"));
				System.out.println(String.format("%s from thread %s",
						response.toString(), Thread.currentThread()));
				channel.capacity(1);
				return channel.writeWith(Streams.just(response.toString()));
//			channel.params()
//					.entrySet()
//					.forEach(
//							entry2 -> System.out.println(String.format(
//									"params [%s=>%s]", entry2.getKey(),
//									entry2.getValue())));

//			StringBuilder response = new StringBuilder().append("hello ")
//					.append(channel.params().get("name"));
		};
	}

	private ReactorChannelHandler<Buffer, Buffer, HttpChannel<Buffer, Buffer>> postHandler() {
		return channel -> {

			channel.headers()
					.entries()
					.forEach(
							entry -> System.out.println(String.format(
									"header [%s=>%s]", entry.getKey(),
									entry.getValue())));

			return channel
					.writeWith(Streams
							.wrap(channel)
							.take(1)
							.log("received")
							.flatMap(
									data -> {
										final StringBuilder response = new StringBuilder()
												.append("hello ").append(
														new String(data
																.asBytes()));
										System.out.println(String.format(
												"%s from thread %s",
												response.toString(),
												Thread.currentThread()));
										return Streams.just(Buffer
												.wrap(response.toString()));
									}));
		};
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
//		System.out.println(demoHttp.getWebPage("src/main/java/webapp/ws.html"));
		// Broadcaster<String> b = Broadcaster.create(env);
		// b.dispatchOn(Environment.cachedDispatcher())
		// .map(String::toUpperCase)
		// .filter(s -> s.startsWith("HELLO"))
		// .consume(s -> System.out.printf("s=%s%n", s));
		//
		//
		//
		//
		// // Sink values into this Broadcaster
		// b.onNext("Hello World!");
		// // This won't print
		// b.onNext("Goodbye World!");
		// // This will print
		// b.onNext("Hello Reactor!");
		//
		// b.accept("Hello Trayan");

		// Must wait for tasks in other threads to complete
		Scanner sc = new Scanner(System.in);
		System.out.println("Hit <Enter> to stop the server ...");
		sc.nextLine();
		System.out.println("Stopping the HTTP server ...");
		demoHttp.teardown();
		System.out.println("HTTP server stopped successfully.");

	}
}