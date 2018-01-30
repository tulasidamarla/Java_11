package com.demo.streams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.demo.domain.Person;

/**
 * There is another type of reduction called Collectors. This is 
 * also referred to as mutable reduction, because unlike other reductions
 * like aggregation this reduction will put the stream elements in a mutable container
 * like List, Map and other type of java objects. 
 * 
 * For ex the below sample is collecting into a string.
 * 
 * List<Person> persons = ...;
 * String result = persons.stream().filter(person -> person.getAge() > 20)
 * 									.map(Person::getLastName)
 * 									.collect(Collectors.joining(",");
 * The above result string list of all the names of the people in persons older than 20,
 * seperated by comma.
 * 
 * Collecting to a List
 * --------------------
 * List<String> result = persons.stream().filter(person -> person.getAge() > 20)
 * 										 .map(Person::getLastName)
 * 										 .collect(Collectors.toList());
 * Collecting into Map
 * -------------------
 * There is a static method groupingBy in Collectors which is used to collect elements into a map.
 * 
 * Map<Integer, List<Person>> result = persons.stream().filter(person -> person.getAge() > 20)
 * 										 .collect(Collectors.groupingBy(Person::getAge));
 * 
 * The result is a map containing persons older than 20, where key is age of persons
 * and value is list of persons with that age.
 * 
 *  Post processing the values with a downstream collector
 *  ------------------------------------------------------
 * Map<Integer, Long> result = persons.stream().filter(person -> person.getAge() > 20)
 * 										 .collect(Collectors.groupingBy(Person::getAge),
 * 													Collectors.counting());
 * Collectors.counting() is just the no of persons of each age.
 * 
 * 
 * */
public class CollectorsReductionDemo {

	public static void main(String[] args) {

		List<Person> persons = new ArrayList<>();
		
		try(BufferedReader reader = new BufferedReader(
				new InputStreamReader(CollectorsReductionDemo.class.getResourceAsStream("persons.txt"))
				);
				
				Stream<String> stream= reader.lines();
		){
			stream.map(line -> {
				String[] s = line.split(" ");
				Person p = new Person(s[0].trim(),Integer.parseInt(s[1]));
				persons.add(p);
				return p;
			}).forEach(System.out::println);;
		}catch(IOException ie) {
			System.out.println(ie);
		}
		
		
		//Age of younger person older than 20.
		Optional<Person> person=persons.stream().filter(p -> p.getAge() > 20)
						.min(Comparator.comparing(Person::getAge));
		System.out.println("Youngest person older than 20 is " + person.get().getName() + " and age is "+person.get().getAge());
		
		//Oldest person
		Optional<Person> oldestPerson=persons.stream().max(Comparator.comparing(Person::getAge));
		System.out.println("Oldest person is " + oldestPerson.get().getName() + " and age is "+oldestPerson.get().getAge());
		
		//Grouping people by Age
		Map<Integer, List<Person>> personsByAge = persons.stream().collect(
					Collectors.groupingBy(Person::getAge)
				);
		personsByAge.forEach((k,v) -> System.out.println(" People with Age " + k + " are" + v));
		
		//No of people by Age
		Map<Integer, Long> noOfPersonsByAge = persons.stream().collect(
				Collectors.groupingBy(Person::getAge,
						Collectors.counting())
			);
		noOfPersonsByAge.forEach((k,v) -> System.out.println(" No of People with Age " + k + " are" + v));
		
		//People names by Age
		Map<Integer, List<String>> personNamesByAge = persons.stream().collect(
				Collectors.groupingBy(Person::getAge,
						Collectors.mapping(Person::getName,Collectors.toList())
			));
		personNamesByAge.forEach((k,v) -> System.out.println(" Names of People with Age " + k + " are" + v));

		//People names by Age with sorted order
		Map<Integer, Set<String>> personNamesByAgeSorted = persons.stream().collect(
				Collectors.groupingBy(Person::getAge,
						Collectors.mapping(Person::getName,Collectors.toCollection(TreeSet::new))
			));
		personNamesByAgeSorted.forEach((k,v) -> System.out.println(" Names of People with Age in sorted order " + k + " are" + v));

	}

}
