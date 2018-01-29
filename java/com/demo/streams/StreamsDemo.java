package com.demo.streams;

import java.util.function.Predicate;
import java.util.stream.Stream;
/**
 * A Stream is an object on which we can define operations like map, filter, reduce etc.
 * Stream does not hold any data. This is the big difference with collection, which holds data.
 * Stream doesn't change the data it processes unlike collection.
 * 
 * Stream is a new concept introduced in java 8. stream() method is added to Collection interface
 * to create a stream object. For ex: 
 * 
 * 	List<String> names = new ArrayList<String>();
 * 	Stream<String> stream = names.stream();
 * 
 * Stream takes different kinds of functional interface objects as parameters based on functionality.
 * For ex, stream.forEach() method takes a Consumer, where as stream.filter() takes a Predicate.
 *   
 * */
public class StreamsDemo {

	public static void main(String[] args) {

		Stream<String> stream = Stream.of("one","two","three","four","five");
		
		
		//stream.forEach(System.out::println);
		
		
		Predicate<String> p1 = s -> s.length() > 2;
		
		/*isEqual() method creates a new predicate by comparing object passed as parameter */
		Predicate<String> p2 = Predicate.isEqual("two");
		
		Predicate<String> p3 = Predicate.isEqual("three");
		
		System.out.println("......Filtered list......");
		
		/*Predicate has lot of boolean operations like and, or, negate etc. */
		stream.filter(p1.and(p2.or(p3)))
				.forEach(System.out::println);
		

	}

}
