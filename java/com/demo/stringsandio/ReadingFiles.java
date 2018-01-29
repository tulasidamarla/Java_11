package com.demo.stringsandio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import com.demo.streams.CollectorsReductionDemo;

public class ReadingFiles {

	public static void main(String[] args) {
		//lines method is added to BufferedReader which returns a stream
		try(BufferedReader reader = new BufferedReader(
				new InputStreamReader(CollectorsReductionDemo.class.getResourceAsStream("persons.txt"))
				);
		){
			Stream<String> stream= reader.lines();
			//reading the first line which is not empty
			stream.filter(line -> !line.isEmpty())
				  .findFirst()
				  .ifPresent(System.out::println);
		}catch(IOException ie) {
			System.out.println(ie);
		}
		
		//Using Java 7 Path and try with resources the below code is much simpler
		Path path = Paths.get("C:","Users","damart1","Desktop","persons.txt");
		//Stream implements AutoCloseable so it works with try-with-resources because it automatically closes file.
		try(Stream<String> stream = Files.lines(path)){
			stream.filter(line -> !line.isEmpty())
				  .findFirst()
				  .ifPresent(System.out::println);
		}catch(IOException ie) {
			System.out.println(ie);
		}

	}

}
