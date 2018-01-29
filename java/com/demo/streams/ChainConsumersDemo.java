package com.demo.streams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class ChainConsumersDemo {

	public static void main(String[] args) {
		List<String> numbers = Arrays.asList("one", "two", "three","four","five");
		
		//Consumer<String> c1 = s -> System.out.println(s);
		Consumer<String> c1 = System.out::println;
		
		List<String> newList= new ArrayList<>();
		
		//Consumer<String> c2 = s -> newList.add(s);
		Consumer<String> c2 = newList::add;
		
		numbers.forEach(c1.andThen(c2));
		
		System.out.println("new List size " + newList.size());
		
		newList.forEach(System.out::println);
		
	}

}
