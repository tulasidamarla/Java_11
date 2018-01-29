package com.demo.streams;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;
/**
 * Operations on a stream are lazy operations. For ex, the call to filter, peek etc are lazy.
 * Technically, all the methods on stream that returns another stream are lazy operations.
 * So, an operation on a stream that returns another stream is called intermediary operation.
 * */
public class StreamsIntermediaryAndFinalOperationsDemo {

	public static void main(String[] args) {
		Stream<String> stream = Stream.of("one","two","three","four","five");
		Predicate<String> p1 = Predicate.isEqual("two");
		
		Predicate<String> p2 = Predicate.isEqual("three");
		
		List<String> list = new ArrayList<>();

		//Both peek and filter returns Stream, they are intermediary operations, if we execute the below
		//code nothing should happen and list should remain empty
		stream.peek(System.out::println)
				.filter(p1.or(p2))
				.peek(list::add);
		
		System.out.println(list.size());
		
		Stream<String> stream2 = Stream.of("one","two","three","four","five");
				
		//unlike peek() method which returns another stream, forEach method is final operation, hence list should be populated with 2 elements.
		stream2.peek(System.out::println)
		.filter(p1.or(p2))
		.forEach(list::add);
		
		System.out.println(list.size());

	}

}
