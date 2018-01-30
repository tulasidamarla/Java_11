package com.demo.collections;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
				
		
		
	}

}
