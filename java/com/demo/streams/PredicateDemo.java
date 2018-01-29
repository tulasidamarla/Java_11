package com.demo.streams;

import java.util.function.Predicate;
import java.util.stream.Stream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


public class PredicateDemo {

	public static void main(String[] args) {
		
		
		
		Stream<String> stream = Stream.of("one","two","three","four","five");
		
		//stream.forEach(System.out::println);
		
		
		Predicate<String> p1 = s -> s.length() > 2;
		
		Predicate<String> p2 = Predicate.isEqual("two");
		
		Predicate<String> p3 = Predicate.isEqual("three");
		
		System.out.println("......Filtered list......");
		
		stream.filter(p1.and(p2.or(p3)))
				.forEach(System.out::println);
	}
}
