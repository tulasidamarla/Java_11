package com.demo.stringsandio;

import java.util.StringJoiner;
import java.util.stream.IntStream;



public class StringsAndStringJoinerDemo {

	public static void main(String[] args) {

		//Streams can be created on a string.
		String greeting = "Hello World";
		IntStream stream = greeting.chars();
		stream.mapToObj(letter -> (char)letter)
				.map(Character::toUpperCase)
				.forEach(System.out::print);
		System.out.println();
		
		//StringJoiner is built with or without a separator
		StringJoiner sj = new StringJoiner(", ");
		sj.add("one").add("two").add("three");
		System.out.println("Joined String is ");
		System.out.println(sj.toString());
		
		//StringJoiner can be built with a separator , a prefix and a postfix
		StringJoiner sj2 = new StringJoiner(", ","{","}");
		sj2.add("one").add("two").add("three");
		System.out.println("Joined String with prefix and postfix is ");
		System.out.println(sj2.toString());
		
		//StringJoiner can be used directly from String class using join method
		//but prefix and postfix is not possible with String class
		String s = String.join(",", "one","two","three");
		System.out.println("Joined String with String class is " + s);
		
		//join method takes var-arg, it can take any iterable as argument
		String[] numbers = {"one","two","three"};
		String joinedString = String.join(",", numbers);
		System.out.println("Joined String with iterable is " + joinedString);
		
	}

}
