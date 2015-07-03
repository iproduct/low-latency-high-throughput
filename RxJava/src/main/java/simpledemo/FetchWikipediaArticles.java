package simpledemo;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import rx.Observable;
import rx.schedulers.Schedulers;

public class FetchWikipediaArticles {
	/**
	 * Fetch a list of Wikipedia articles asynchronously.
	 */
	public static Observable<String> fetchWikipediaArticleAsynchronously(String... articleNames) {
		return Observable.<String>create( aSubscriber -> {
			Observable.from(articleNames).forEach(articleName -> {
	            if (aSubscriber.isUnsubscribed()) {
	                return;
	            }
	            CloseableHttpClient httpclient = HttpClients.createDefault();
	            HttpGet httpGet = new HttpGet("http://en.wikipedia.org/wiki/"+ articleName);
	            CloseableHttpResponse response1;
				try {
					response1 = httpclient.execute(httpGet);
		            try {
//		                System.out.println(response1.getStatusLine());
		                HttpEntity entity1 = response1.getEntity();
		                // do something useful with the response body
		                // and ensure it is fully consumed
		                aSubscriber.onNext(EntityUtils.toString(entity1, "utf-8"));
		            } finally {
		                response1.close();
		            }
				} catch (Exception e) {
					e.printStackTrace();
				}
	        });
	        // after sending all values we complete the sequence
	        if (!aSubscriber.isUnsubscribed()) {
	            aSubscriber.onCompleted();
	        }
	    }).subscribeOn(Schedulers.io());
	}

	
	public static void main(String[] args) throws InterruptedException {
		Pattern pattern = Pattern.compile("<title>(.*)</title>");
		// Call fetchWikipediaArticleAsynchronously()
		System.out.println("\nWiki Articles:");
		fetchWikipediaArticleAsynchronously("Tiger", "Elephant", "Lion")
			.flatMap(page -> Observable.from(page.split("\n")).limit(10).map(String::trim))
			.filter(line ->  pattern.matcher(line).matches())
			.map(line -> { Matcher m = pattern.matcher(line); m.matches(); return m.group(1);} )
			.subscribe(System.out::println, 
				ex -> ex.printStackTrace(), () -> System.out.println("Completed"));
		
		Thread.sleep(10000); //wait async observable to complete

	}
}
