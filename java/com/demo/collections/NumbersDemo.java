package com.demo.collections;

import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;

/**
 * aggregation methods like min,max,sum are added to many of the wrapper like Integer, Long etc.
 * For ex:
 * long max = Long.max(20, 30);
 * These methods can be used with streams using method references also. For ex:
 * 
 * BinaryOperator<Long> sum = Long::sum;
 * 
 * */
public class NumbersDemo {

	public static void main(String[] args) {
		long max = Long.max(20, 30);
		System.out.println("max of 20 and 30 is " + max);
		
		Stream<Long> stream = Stream.of(10L,20L,30L,40L);
		//BinaryOperator<Long> sum = (l1,l2) -> l1 + l2;
		BinaryOperator<Long> sum = Long::sum;
		
		Optional<Long> result = stream.reduce(sum);
		System.out.println("sum is " + result.get());
		
		
		
	}
	

}
