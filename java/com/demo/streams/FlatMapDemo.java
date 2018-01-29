package com.demo.streams;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
  * mapper(or simply Map) is a functional interface which takes one object argument and returns another object.
  * 
  * Signature
  * 
  * <R> Steam<R> map(Function<T, R> mapper);
  * 
  * 
  * flatmapper(or FlatMap) takes one object as argument and returns a stream of object.
  * 
  * Signature 
  * 
  * <R> Steam<R> flatMap(Function<T, Stream<R>> flatMapper);
  * 
  * If flatMap is a regular Map, then it would return Stream<Stream<R>>, because it's a flatMap
  * it stream of streams if flattened, and becomes a stream.
  * 
  * 
  * */ 

public class FlatMapDemo {

	public static void main(String[] args) {

		List<Integer> list1= Arrays.asList(1,2,3,4,5,6,7);
		List<Integer> list2= Arrays.asList(2,4,6);
		List<Integer> list3= Arrays.asList(3,5,7);
		
		List<List<Integer>> list= Arrays.asList(list1,list2,list3);
		
		System.out.println(list);
		
		list.stream()
			.map(l -> l.size())
			.forEach(System.out::println);
		
		//Function<List<?>, Integer> size = l -> l.size();
		//Function<List<?>, Integer> size = List::size;
		/**unlike map, flatmap takes an object as an argument and returns a stream*/
		Function<List<Integer>, Stream<Integer>> mapper = l -> l.stream();
		
		//map method returns a stream and forEach method prints those streams
		list.stream()
			.map(mapper)
			.forEach(System.out::println);
		
		//flatMap method flattens all the 3 streams and resulting stream contains integers which
		//will print all integers
		list.stream()
		.flatMap(mapper)
		.forEach(System.out::println);
		
		
	}

}
