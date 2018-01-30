package com.demo.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * New Methods on Collection
 * -------------------------
 * stream() and parallelstream() methods are added to java.util.Collection interface.
 * spliterator() method also added. Iterator is for Collections where as 
 * Spliterator is for streams. So a stream uses spliterator of the collection 
 * for parallelizing the process of that collection.
 * 
 * removeIf() method is added to Collection interface which takes a predicate
 * 
 * List<String> strings = Arrays.asList("one","two","three");
 * Collection<String> list = new ArrayList<>(strings);
 * boolean b = list.removeIf(s -> s.length() > 4);
 * System.out.println(list.stream().collect(Collectors.joining(","));
 * 
 * Note: The above code will not work if list is unmodifiable
 * 
 * New Methods on Iterable
 * -----------------------
 * Method forEach() is defined in Iterable interface. forEach() takes a consumer.
 * 
 * List<String> strings = Arrays.asList("one","two","three");
 * strings.forEach(System.out::println);
 * 
 * Note: forEach() is not available for arrays.
 * 
 * New Methods on List
 * ------------------- 
 * replaceAll() method is added to List interface, which takes UnaryOperator as parameter
 * and returns void.
 * 
 * sort() method is added to List interface, which takes a comparator and returns void.
 * 
 * */
public class CollectionsDemo {

	public static void main(String[] args) {
		
		
		List<String> strings = Arrays.asList("one", "two", "three","four");

		//List.removeIf() method demo
		Collection<String> list = new ArrayList<>(strings);

		// removes if length of any element in the list is greater than 4
		list.removeIf(s -> s.length() > 4);

		// In this example three is removed
		System.out.println(list.stream().collect(Collectors.joining(",")));
		
		//List.replaceAll demo
		((List<String>)list).replaceAll(String::toUpperCase);
		System.out.println(list.stream().collect(Collectors.joining(",")));
		
		//List.sort() demo
		((List<String>)list).sort(Comparator.naturalOrder());
		System.out.println(list.stream().collect(Collectors.joining(",")));
		
	}

}
