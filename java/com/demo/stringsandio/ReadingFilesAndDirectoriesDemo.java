package com.demo.stringsandio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import com.demo.streams.CollectorsReductionDemo;

public class ReadingFilesAndDirectoriesDemo {

	public static void main(String[] args) {
		//Reading files
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
		//Files.lines() method returns Stream of String using which we can read lines of text like below.
		try(Stream<String> stream = Files.lines(path)){
			stream.filter(line -> !line.isEmpty())
				  .findFirst()
				  .ifPresent(System.out::println);
		}catch(IOException ie) {
			System.out.println(ie);
		}
		
		//Listing directories using Files.list() method 
		//which returns stream of path
		Path dirPath= Paths.get("c:","Users","damart1","Desktop","tech","binaries");
		try(Stream<Path> stream= Files.list(dirPath)){
			stream.filter(p -> p.toFile().isDirectory())
				  .forEach(System.out::println);
		}catch(IOException ioe) {
			System.out.println(ioe);
		}
		
		//Files.list() only returns first level entires
		//Files.walk() can be used if we want to read whole subtree
		System.out.println(" Reading whole sub tree for the directory location " + dirPath.toString());
		try(Stream<Path> stream= Files.walk(dirPath)){
			stream.filter(p -> p.toFile().isDirectory())
				  .forEach(System.out::println);
		}catch(IOException ioe) {
			System.out.println(ioe);
		}
		//Files.walk() takes an int argument which limits the depth of exploration of directories.
		//For ex, if we want to explore two levels inside a directory
		//consider the below code snippet
		System.out.println(" Reading whole sub tree for the directory location " + dirPath.toString() + "with depth 3");
		try(Stream<Path> stream= Files.walk(dirPath,3)){
			stream.filter(p -> p.toFile().isDirectory())
				  .forEach(System.out::println);
		}catch(IOException ioe) {
			System.out.println(ioe);
		}
	}

}
