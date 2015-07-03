package org.gradle;

import java.util.Arrays;

import rx.Observable;
import rx.functions.Func1;

public class CountSuccessFailure {

	public static void main(String[] args) {
		Observable<String> cases = 
				Observable.from(Arrays.asList("kewl", "leet", "speak"));
		Func1<String, Boolean> successPredicate = (s) -> s.indexOf("k") >= 0;
		cases.filter(successPredicate)
        .count()
        .subscribe(i -> System.out.println("Successfull:" + i));
		
		cases.filter((s)-> !successPredicate.call(s))
        .count()
        .subscribe(i -> System.out.println("Not Successfull:" + i));


	}

}
