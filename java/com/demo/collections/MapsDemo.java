package com.demo.collections;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.demo.domain.Person;
import com.demo.util.PersonUtil;

/**
 * New methods of Map interface
 * ---------------------------
 * 1)forEach() method which takes a BiConsumer as argument and returns void. For ex,
 * 
 * 		Map<String, Person> map = ...;
 * 		map.forEach((k,v) -> System.out.println(k+ " " + v));
 * 
 * 2)getOrDefault() method returns default value passed as a parameter if the key is not present in the map. 
 * 	 For ex:
 *  
 *  	Map<String,Person> personMap = ...;
 * 		Person defaultPerson = new Person("Tulasi",36);
 *  	Person p = personMap.getOrDefault("sachin", defaultPerson);
 * 
 * 3)putIfAbsent() method will not erase an existing object unlike put method.
 * 4)replace(key,value) method will replace existing value if there is an entry with the given key is present in the map.
 * 5)replace(key,oldValue,newValue) method will replace oldValue with newValue only when key and oldValue are matched. 
 * 6)replaceAll((key,oldValue) -> newValue) does remapping of all existing key/value pairs. The argument is a BiFunction.
 * 7)remove(key,value) removes if key/value pair is present in the map.
 * 
 * Compute and Merge methods
 * -------------------------
 * 1)compute(), computeIfPresent(), computeIfAbsent()
 * 		Map<String, Person> map = ...;
 * 		map.compute(key, person, (key,oldPerson) -> newPerson);
 * 
 * Note: compute methods take key, the given value and the BiFunction that takes a key and value as arguments 
 * and it computes new value based on those key and value. This value doesn't need to be associated with key
 * in the map and the key itself is not required to be present in the map.
 * 
 * 2)merge() method used to merge values into a map if same keys are present in the other map. For ex:
 * 		Map<Integer,List<Person>> map1= ...;
 * 		Map<Integer,List<Person>> map2= ...;
 * 		
 * 		map2.entrySet().stream().forEach(entry -> map1.merge(entry.getKey()
 * 															,entry.getValue()
 * 															,(list1,list2) -> {
 * 																list.addAll(list2);
 * 																return list1;				
 * 															}));
 * 	Note: merge method takes 3 parameters. first is the key and second is the assocaite value 
 * 	and third value is a BiFunction. This BiFunction will be used if that key is already present in case
 * 	that key already exists in map1. If key already exists in map1, then it merges the value in map2 to 
 * 	value in map1.
 * 
 * 	Note: Better examples are present here
 * 	http://www.deadcoderising.com/2017-02-14-java-8-declarative-ways-of-modifying-a-map-using-compute-merge-and-replace/
 * 
 * */
public class MapsDemo {

	public static void main(String[] args) {
		List<Person> persons = PersonUtil.getPersons();
		Map<String,Person> personMap = new HashMap<>();
		
		persons.forEach(p -> personMap.put(p.getName(), p));
		
		//Map<String, List<Person>> collect = persons.stream().collect(Collectors.groupingBy(Person::getName));
		personMap.put("test", null);
		Person defaultPerson = new Person("Tulasi",36);
		
		Person p = personMap.getOrDefault("sachin", defaultPerson);
		
		System.out.println(p.getName());
		
		System.out.println("mapByAge for first 10 entries ");
		
		Map<Integer, List<Person>> mapByAge = mapByAge(persons.subList(1, 10));
		
		mapByAge.forEach((k,v) -> System.out.println(k + " " + v));
		
		System.out.println("mapByAge for remaining entries ");
		Map<Integer, List<Person>> mapByAge2 = mapByAge(persons.subList(10, persons.size()));
		mapByAge2.forEach((k,v) -> System.out.println(k + " " + v));
		
		//Merge Demo
		mapByAge2.entrySet().stream()
							.forEach(entry -> 
									mapByAge.merge(entry.getKey(), 
													entry.getValue(),
													(l1,l2) -> {
														l1.addAll(l2);
														return l1;
													}
												));
		
		
		System.out.println("mapByAge merged ");
		mapByAge.forEach((k,v) -> System.out.println(k + " " + v));
	}
	
	private static Map<Integer, List<Person>> mapByAge(List<Person> persons) {
		return persons.stream().collect(Collectors.groupingBy(Person::getAge));
	}

}
