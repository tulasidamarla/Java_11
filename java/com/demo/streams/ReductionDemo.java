package com.demo.streams;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;

/**
 * Reduction is a terminal operation that is performed on a stream. 
 * There are two types of reductions.
 * 1)Aggregation operations like min,max,sum,count etc.
 * For ex:
 * 	List<Integer> ages = Arrays.asList(10,20,30,40);
 * 	Stream<Integer> stream = ages.stream();
 * 	Integer sum = stream.reduce(0, (age1,age2) -> age1 + age2);
 * 	where first argument(0) is called identity element. 
 * 	second argument is reduction operation of type BinaryOperator<T>
 * 	we know BinaryOperator is a special type of BiFunction which takes 
 * 	two objects(T,T) of same type as parameters and return another object(T) of same type. 
 * 	
 * 	Note: There are few corner cases for BinaryOperator.
 * 	a)what happens if stream contains only one element?
 * 	If stream contains only one element, then BinaryOperator returns the reduction 
 * 	of the element and identity element.
 * 	b)what happens if stream is empty
 * 	If stream is empty then it returns identity element.
 * 	2)Boolean operations like allMatch(), noneMatch(), anyMatch(). For ex,
 * 		List<Person> persons = ...;
 * 		Optional<Person> personsFiltered= persons.stream().map(person -> person.getLastName())
 * 														  .allMatch(length < 20);
 * 	
 * 	Note: Few Reductions that return optional are findFirst(), findAny() 
 * 	Optional
 * 	--------
 * 	Suppose if the reduction operation is max. For ex
 * 		BinaryOperator<Integer> max = (i1,i2) -> i1 > i2 ? i1 : i2 ;
 * 	The problem with max operation unlike sum operation is it doesn't have an idenity element.
 * 	So, the max of an empty string is undefined. What is the return type of the below?
 * 	
 * 		Stream<Integer> stream4 = Stream.of(1,2,3,4);
 * 		stream4.max(Comparator.naturalOrder());
 * 	
 * 	As we know the default value of an int is 0, but here we are using wrapper, so default value is null,
 * 	But, we certainly don't want null to be returned. It returns an Optional(java.util), which is introduced
 * 	in java 8. Optional means there might be no result. i.e.
 *  	Optional<Integer> max = stream4.max(Comparator.naturalOrder());
 *  
 *  Using Optional
 *  --------------
 *  1)  if(max.isPresent){
 *  		Integer maxAge = max.get();
 *  	} else{
 *  		...
 *  	}
 * 	2)	Integer maxAge = max.orElse(0)
 * 	3)	Integer maxAge = max.orElseThrow(MyException::new); 
 * 	Note: orElseThrow method call should be surrounded by try/catch.
 * 
 * 		
 * 
 * */
public class ReductionDemo {

	public static void main(String[] args) {
		Stream<Integer> stream = Stream.empty();
		
		//BinaryOperator<Integer> sum = (i1,i2) -> i1 + i2;
		BinaryOperator<Integer> sum = Integer::sum;
		int reduction = stream.reduce(0, sum);
		
		System.out.println("Sum reduction of empty string with identity element 0 is "+reduction);
		
		Stream<Integer> stream2 = Stream.of(1);
		int reduction2 = stream2.reduce(0, sum);
		
		System.out.println("Sum reduction of stream with single value 1 and with identity element 0 is "+reduction2);
		
		Stream<Integer> stream3 = Stream.of(1,2,3,4);
		int reduction3 = stream3.reduce(0, sum);
		
		System.out.println("Sum reduction of stream with 1,2,3,4 is "+reduction3);
		
		//BinaryOperator<Integer> max = (i1,i2) -> i1 > i2 ? i1 : i2;
		
		
		Stream<Integer> stream4 = Stream.of(1,2,3,4);
		
		//int reduction4 = stream4.reduce(0, max);
		
		Optional<Integer> max = stream4.max(Comparator.naturalOrder());
		
		
		System.out.println(max.orElse(0));
		//System.out.println(max.orElseThrow(exceptionSupplier));
		
		System.out.println("Max reduction of stream with 1,2,3,4 is "+max.get());
		//System.out.println("Max reduction of stream with 1,2,3,4 is "+reduction4);
		
		//Sum Reduction corner cases
		//Trying with a wrong identity element of 100
		Stream<Integer> stream5 = Stream.of(10);
		int sumResult = stream5.reduce(100, sum);
		System.out.println("Sum reduction with identity element of 100 and with value 10 is " + sumResult);
		//Giving an identity element of 0 for max operation produces wrong result.
		//For ex, max of -10 and -20 should return -10, but if provide identity element of 0
		//then it compares 10 and -20 with 0 and produces 0

		Stream<Integer> stream6 = Stream.of(-10,-20);
		int maxResult = stream6.reduce(0, Integer::max);
		System.out.println("Max reduction with identity element of 0 and with values -10 and -20 is " + maxResult);
		//so reduce operation for max with idenity element is not the right solution
		//lets use reduce without idenity element.
		Stream<Integer> stream7 = Stream.of(-10,-20);
		Optional<Integer> maxInt = stream7.reduce(Integer::max);
		System.out.println("Max reduction without identity element and with values -10 and -20 is " + maxInt.get());
		
		
	}

}
