package com.demo.collections;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import com.demo.domain.Person;
import com.demo.streams.CollectorsReductionDemo;

/**
 * New methods on Comparator
 * ------------------------
 * comparing() method is added to Comparator in java 8, which takes a regular Function object called as 
 * keyExtractor(an instance of the object the comparator used for comparing) and returns Comparator. 
 * Consider the below example.
 * 
 * Comparator<Person> comparator = Comparator.comparing(Person::getLastName);
 * 
 * thenComparing() method is added to Comparator which is used to chain comparators. For ex,
 * 
 * Comparator<Person> comparator = Comparator.comparing(Person::getLastName)
 * 											 .thenComparing(Person::getFirstName);
 * 
 * reversed() method takes a comparator as an argument returns a Comparator which is reverse of the original.
 * naturalOrder() method which will compare Comparable objects in their natural order.
 * 
 * nullsFirst() method which takes a comparator as an argument. It considers null values lesser than non-null values. For ex,
 *  
 *  Comparator<Person> comparator = Comparator.nullsFirst(Comparator.naturalOrder());
 * 
 * Note: nullsLast() method is also available which does the opposite of nullsFirst() method.
 *  
 * */
public class ComparatorsDemo {

	public static void main(String[] args) {
		List<Person> persons = getPersons();
		Comparator<Person> comparator = Comparator.comparing(Person::getName).thenComparing(Person::getAge);
		persons.sort(comparator);
		System.out.println("Persons sorted by name : ////////////////");
		persons.forEach(System.out::println);
		
	}

	public static List<Person> getPersons(){
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
		
		return persons;
	}
}
