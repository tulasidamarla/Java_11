package com.demo.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.demo.domain.Person;
import com.demo.util.PersonUtil;

public class BiMapsDemo {

	public static void main(String[] args) {
		List<Person> persons=PersonUtil.getPersons();
		
		Map<Integer, List<Person>> map = persons.stream().collect(Collectors.groupingBy(Person::getAge));
		map.forEach((age,list) -> System.out.println(age + " -> " + list));
		
		Map<Integer, Map<String,List<Person>>> biMap = new HashMap<>();
		
		persons.forEach(person -> 
						biMap.computeIfAbsent(person.getAge(), 
												HashMap::new
												).merge(person.getGender(), 
														new ArrayList<>(Arrays.asList(person)), 
														(l1,l2) -> { l1.addAll(l2);
																		return l1;}));
		
		System.out.println("BiMap ....");
		biMap.forEach((age,m) -> System.out.println(age + " -> " + m));
		
		}
}
