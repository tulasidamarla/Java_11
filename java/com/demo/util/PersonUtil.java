package com.demo.util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.demo.domain.Person;
import com.demo.streams.CollectorsReductionDemo;

public class PersonUtil {

	public static List<Person> getPersons() {
		List<Person> persons = new ArrayList<>();
		
		try(BufferedReader reader = new BufferedReader(
				new InputStreamReader(CollectorsReductionDemo.class.getResourceAsStream("persons.txt"))
				);
				
				Stream<String> stream= reader.lines();
		){
			stream.map(line -> {
				String[] s = line.split(" ");
				Person p = new Person(s[0].trim(),Integer.parseInt(s[1]));
				p.setGender(s[2].trim());
				persons.add(p);
				return p;
			}).forEach(System.out::println);;
		}catch(IOException ie) {
			System.out.println(ie);
		}
		return persons;
	}

}
