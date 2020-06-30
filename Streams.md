Streams
-------
Streams helps you specify computations at a higher conceptual level than with collections. With a stream, you specify what you want to do, not how to do it.
You leave the scheduling of operations to the implementation. For example, you want to compute the average of a certain property. You specify the source of data and the property, and the stream library can then optimize the computation, by using multiple threads for computing and combining the results.

FROM ITERATING TO STREAM OPERATIONS
-----------------------------------
For example, we want to count all long words(lenght greater than 12) in a text file.

		var contents = new String(Files.readAllBytes(Paths.get("alice.txt")), StandardCharsets.UTF_8); // Read file 
		List<String> words = List.of(contents.split("\\PL+"));	//Non letters are delimiters i.e. PL+
		int count = 0;
		for (String w : words) {
			if (w.length() > 12) count++;
		}

With streams, the same operation looks like this:

		long count = words.stream()
							.filter(w > w.length() > 12)
							.count();
						
Note: Simply changing stream into parallelStream allows the stream library to do the filtering and counting in parallel.						

		long count = words.parallelStream()
							.filter(w > w.length() > 12)
							.count();

Streams follow the “what, not how” principle. In our stream example, we describe what needs to be done: get the long words and count them. We don’t specify in which order, or in which thread, this should happen. In contrast, the loop at the beginning of this section specifies exactly how the computation should work, and thereby goes without any chances of optimization.	

A stream seems superficially similar to a collection, allowing you to transform and retrieve data. But there are significant differences:

1. A stream does not store its elements. They may be stored in an underlying collection or generated on demand.
2. Stream operations don’t mutate their source. For example, the filter method does not remove elements from a stream, but it gives a new stream in which they are not present.
3. Stream operations are lazy when possible. This means they are not executed until their result is needed. For example, if you only ask for the first five long words instead of all, the filter method will stop filtering after the fifth match. 

In Steams the typical workflows are common.
1)Creates a stream.
2)Specify intermediate operations(In our example, filter) for transforming the initial stream into others, possibly in multiple steps.
3)Apply a terminal operation(count) to produce a result. This operation forces the execution of the lazy operations before it.i.e. filter in our example.
4)Afterwards, the stream can no longer be used.

STREAM CREATION
---------------
Streams can be created in many ways.
1)We can turn any collection to a stream.
2)To create a stream from an array.

		String[] strarr = {"Tulasi", "Ram"};
		Stream.of(strarr);
			(or)
		
		Arrays.stream(strarr);
		
3)To create a stream from var args.
		Stream.of("Tulasi","Ram");
		
4)To create a stream from part of an array,
		Arrays.stream(strarr, fromIndex, toIndex);

Infinite Streams
----------------
The Stream interface has two static methods for making infinite streams. 

1)The generate method takes a function with no arguments (or, technically, an object of the Supplier<T> interface). Whenever a stream value is needed, that function is called to produce a value. You can get a stream of constant values as 
		
		Stream<String> echos = Stream.generate(() -> "Echo");
		
or a stream of random numbers as

		Stream<Double> randoms = Stream.generate(Math::random);	

If you write the below statement, it prints infinitely.

		echos.forEach(System.out::println);
		
If you want to limit the stream without indefinitely printing it,
		
		final int SIZE = 10;
		List<T> firstElements = randoms.limit(SIZE + 1).collect(Collectors.toList());	
		
2)To produce sequences such as 0 1 2 3 . . ., use the iterate method. It takes a “seed” value and a function (technically, a UnaryOperator<T>) and repeatedly applies the function to the previous result.

      Stream<BigInteger> integers = Stream.iterate(BigInteger.ONE, n -> n.add(BigInteger.ONE));

Note: 
we can also add a predicate to limit the infinite stream to a finite stream. The iterate method syntax is, Stream.iterate(T seed, Predicate hasNext, UnaryOperator next).
For ex,

		int limit = 10;
		Stream<BigInteger> finiteIntegers	= Stream.iterate(BigInteger.ZERO,	n ->	n.compareTo(limit) < 0,	n ->	n.add(BigInteger.ONE));
		finiteIntegers.forEach(System.out::println);

Java methods return streams
---------------------------
A no of java methods yield streams. For ex, the Pattern class has a method splitAsStream that splits a CharSequence by a regular expression.

		Stream<String> wordsAnotherWay = Pattern.compile("\\PL+").splitAsStream(contents);

The Scanner.tokens method yields a stream of tokens of a scanner.
		Stream<String> words = new Scanner(contents).tokens();

The static Files.lines method returns a Stream of all lines in a file:

		Stream<String> lines = Files.lines(path);

If you have an Iterable that is not a collection, you can turn it into a stream by calling,
		StreamSupport.stream(iterable.spliterator(), false);

If you have an Iterator and want a stream of its results, use
		StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED), false);

Here is the sample code:

		Iterable<Path> iterable = FileSystems.getDefault().getRootDirectories();
		Stream<Path> rootDirectories = StreamSupport.stream(iterable.spliterator(), false); --> false indicates stream is sequential. If true, returns parallel stream.

		Iterator<Path> iterator = Paths.get("/usr/share/dict/words").iterator(); 	--> returns an iterator with each name in the path.
		Stream<Path> pathComponents = StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED), false);

Note:
It is very important that you don’t modify the collection that is backing a stream while carrying out a stream operation. If you were to modify that collection, the outcome of the stream operations would be undefined. The JDK documentation refers to this requirement as noninterference.

To be exact, since intermediate stream operations are lazy, it is possible to mutate the collection up to the point when the terminal operation executes. For example, the following, while certainly not recommended, will work:

		List<String> wordList = . . .;
		Stream<String> words = wordList.stream();
		wordList.add("END");
		long n = words.distinct().count();
		
The below code won't work:
		
		Stream<String> words = wordList.stream();
		words.forEach(s -> if (s.length() < 12) {
			wordList.remove(s));		
		}

I got this java.lang.UnsupportedOperationException	when executed the above snippet.


THE FILTER, MAP, AND FLATMAP METHODS
------------------------------------
FILTER
------
The filter transformation yields a new stream with those elements that match a certain condition. For ex, in the below example longWords is a new stream created from the wordList stream.

		final Stream<String> longWords = wordList.stream().filter(w -> w.length() > 12);

Note: Argument to filter is a Predicate<T>. 

MAP
---
The map transformation also yields a new stream based on a function. It is generally called mapper function. For ex,

		final Stream<String> lowercasewords = wordList.stream().map(String::toLowerCase);

FLATMAP
-------
flatMap() is used when we have a collection of streams and we want to flatten it. For ex,

		State karnataka = new State();
		karnataka.addCity("Bangalore");
		karnataka.addCity("Mysore");

		State punjab = new State();
		punjab.addCity("Chandigarh");
		punjab.addCity("Ludhiana");

		List<State> allStates = Arrays.asList(karnataka, punjab);

		//Java Stream flatMap way
		List<String> allCities = allStates.stream().flatMap(e -> e.getCities().stream()).collect(Collectors.toList());

Note: flatMap() method takes a mapper function which produces a stream.

EXTRACTING SUBSTREAMS AND COMBINING STREAMS
-------------------------------------------
a)The call stream.limit(n) returns a new stream that ends after n elements (or when the original stream ends if it is shorter). This method is particularly useful for cutting infinite streams down to size. For ex,

		Stream<Double> randoms = Stream.generate(Math::random).limit(10);

b)Similarly, the call stream.skip(n), discards the first n elements. 
c)The stream.takeWhile(predicate) call takes all elements from the stream while the predicate is true, and then stops. 
The dropWhile() method does the opposite, dropping elements while a condition is true and yielding a stream of all elements starting with the first one for which the condition was false.
d)You can concatenate two streams with the static concat method of the Stream class.


OTHER STREAM TRANSFORMATIONS
----------------------------
a)The distinct method returns a stream that yields elements from the original stream, in the same order, except that duplicates are suppressed.

		Stream<String> uniqueWords = Stream.of("merrily", "merrily", "merrily", "gently").distinct();

b)There are two methods for sorting. First method without arguments, uses natural order for sorting. i.e. Comparable. If the objects of the stream are not comparable, it throws ClassCastException. Second method uses Comparator. For ex, if you want to sort strings by length

		List<String> sortedByLength = words.stream()
			.filter(word -> word.length() > 12)
			.sorted(Comparator.comparing(String::length))
			.collect(Collectors.toList());

Note: you can sort a collection without using streams. The sorted method is useful when the sorting process is part of a stream pipeline.

c)The peek method yields another stream with the same elements as the original, but a function is invoked every time an element is retrieved. That is handy for debugging: For ex,

		Object[] powers = Stream.iterate(1.0, p -> p * 2)
								.peek(e -> System.out.println("Fetching " + e))
								.limit(20)
								.toArray();

Note: With most IDEs, you can also set breakpoints in lambda expressions. If you just want to know what happens at a particular point in the stream pipeline, add the below code and
set a breakpoint on the second line.

		.peek(x ->{
			return; })

SIMPLE REDUCTIONS
-----------------
Reductions are terminal operations. They reduce the stream to a nonstream value that can be used in your program. 
a)the count() method that returns the number of elements of a stream. We have seen this example before.
b)Other simple reductions are max and min that return the largest or smallest value.

Note: These methods return Optional<T> value. Max/min methods take comparator as argument. For ex,

				Optional<String> max = words.stream()
					.filter(word -> word.length() > 12)
					.max(String::compareToIgnoreCase);
				System.out.println(max.get());

c)The findFirst returns the first value in a nonempty collection. It is often useful when combined with filter. For example,

				Optional<String> startsWithQ = words.stream().filter(s -> s.startsWith("Q")).findFirst();

d)If you are OK with any match, not just the first one, use the findAny method. This is effective when you parallelize the stream, since the stream can report any match that it finds instead of being constrained to the first one. For ex,

				Optional<String> startsWithQ = words.parallelStream().filter(s -> s.startsWith("Q")).findAny();

Note: If you just want to know if there is a match, there are methods that return boolean. For ex, anyMatch(), allMatch(), noneMatch() etc.


THE OPTIONAL TYPE
-----------------
An Optional<T> object is a wrapper for either an object of type T or no object. The Optional<T> type is intended as a safer alternative for a reference of type T that either refers to an object or is null. But it is only safer if you use it right.

Getting an Optional Value
-------------------------
The key to using Optional effectively is to use a method that either produces an alternative if the value is not present, or consumes the value only if it is present. For ex,

		String result = optionalString.orElse("");
		
If you want to invoke a method to get the default value incase value is not present,
		
		String result = optionalString.orElseGet(() -> method());

We can throw an exception, if no value is present. For ex,

		String result = optionalString.orElseThrow(); --> It will throw NoSuchElementException
		String result = optionalString.orElseThrow(IllegalStateException::new); --> You can provide an supplier function here.

Consuming An Optional Value
---------------------------
The ifPresent method accepts a function. If the optional value exists, it is passed to that function. Otherwise, nothing happens.		

		optionalValue.ifPresent(v -> results.add(v)); or optionalValue.ifPresent(results::add);

If you want to take one action if the Optional has a value
		optionalValue.ifPresentOrElse(v -> System.out.println("Found" + v), () -> logger.warning("No match"));

Pipelining Optional Values
--------------------------
You can transform the value inside an Optional by using the map method:	
		Optional<String> transformed = optionalString.map(String::toUpperCase);

Note: If optionalString is empty, then transformed is also empty.

***
Note: This map method is the analog of the map method of the Stream interface. Simply imagine an optional value as a stream of size zero or one. The result again has size zero or one. For ex, 

		Optional<String> transformed = optionalString.filter(s -> s.length() >= 8)
													 .map(String::toUpperCase);

The above map method returns an optional with size 1, if the filter condition is met, otherwise it returns an optional of size 0.

Note: We can give custom optional incase of an empty optional. For ex,

		Optional<String> result = optionalString.or(() -> method()); // Supply an Optional

How Not to Work with Optional Values
------------------------------------
If you don’t use Optional values correctly, you have no benefit over the “something or null” approach of the past. The get method gets the wrapped element of an Optional value if it exists, or throws a NoSuchElementException if it doesn’t. For ex,
		
		Optional<T> optionalValue = . . .;
		optionalValue.get().someMethod() 
		
		is no safer than, 
		
		T value = . . .;
		value.someMethod();

The isPresent method reports whether an Optional<T> object has a value.

		if (optionalValue.isPresent()) optionalValue.get().someMethod();
		
		is no easier than
		
		if (value != null) value.someMethod();
		
Here are a few more tips for the proper use of the Optional type:
-----------------------------------------------------------------
• A variable of type Optional should never be null.
• Don’t use fields of type Optional. The cost is an additional object. Inside a class, using null for an absent field is manageable.
• Don’t put Optional objects in a set, and don’t use them as keys for a map. Collect the values instead.

Creating Optional Values
------------------------
If you want to write a method that creates an Optional object, there are several static methods for that purpose, including Optional.of(result) and Optional.empty(). For example,

		public static Optional<Double> inverse(Double x) {
			return x == 0 ? Optional.empty() : Optional.of(1 / x);
		}


Note: The ofNullable method is intended as a bridge from possibly null values to optional values. Optional.ofNullable(obj) returns Optional.of(obj) if obj is not null and Optional.empty() otherwise.

Composing Optional Value Functions with flatMap
-----------------------------------------------
Suppose you have a method f yielding an Optional<T>, and the target type T has a method g yielding an Optional<U>. If they were normal methods, you could compose them by calling s.f().g(). But that composition doesn’t work since s.f() has type Optional<T>, not T. Instead, call

		Optional<U> result = s.f().flatMap(T::g);
		
Note: If s.f() is present, then g is applied to it. Otherwise, an empty Optional<U> is returned.		

For example, consider the safe inverse method and we also have a safe square root:


		class Demo{
			public static Optional<Double> inverse(Double x) {
				return x == 0 ? Optional.empty() : Optional.of(1 / x);
			}

			public static Optional<Double> squareRoot(Double x) {
				return x < 0 ? Optional.empty() : Optional.of(Math.sqrt(x));
			}
		}

To compute the square root of inverse,

		Optional<Double> result = inverse(x).flatMap(MyMath::squareRoot);

Or you can call,
		
		Optional.of(4.0).flatMap(Demo::inverse).flatMap(Demo::squareRoot);
		
Turning an Optional Into a Stream
---------------------------------
The stream method turns an Optional<T> into a Stream<T> with zero or one elements. Suppose, you have a stream of user IDs and a method

		Optional<User> lookup(String id);

How do you get a stream of users, skipping those IDs that are invalid?

		Stream<String> ids = . . .;
		Stream<User> users = ids.map(Users::lookup)
		.filter(Optional::isPresent)
		.map(Optional::get);
		
The more elegant way to do the above is,

		Stream<User> users = ids.map(Users::lookup)
								.flatMap(Optional::stream);

Note: Each call to stream returns a stream with 0 or 1 elements. The flatMap method combines them all. That means the nonexistent users are simply dropped.

Note: These days, many methods return null when there is no valid result. Suppose Users.classicLookup(id) returns a User object or null, not an Optional<User>. Then you can of course
filter out the null values:

		Stream<User> users = ids.map(Users::classicLookup).filter(Objects::nonNull);
		
But if you prefer the flatMap approach, you can use

		Stream<User> users = ids.flatMap(id -> Stream.ofNullable(Users.classicLookup(id)));
		
		OR 
		
		Stream<User> users = ids.map(Users::classicLookup).flatMap(Stream::ofNullable);

Note: The call Stream.ofNullable(obj) yields an empty stream if obj is null or a stream just containing obj otherwise.		


COLLECTING RESULTS
------------------
For collecting stream elements to another target, there is a convenient collect method that takes an instance of the Collector interface. A collector is an object that accumulates elements and produces a result. The Collectors class provides a large number of factory methods for common collectors. To collect the stream elements into a list, use the collector produced by Collectors.toList():

		List<String> result = stream.collect(Collectors.toList());
	
Similarly, here is how you can collect stream elements into a set:

		Set<String> result = stream.collect(Collectors.toSet());
		
If you want to control which kind of set you get, use the following call instead:

		TreeSet<String> result = stream.collect(Collectors.toCollection(TreeSet::new));

Suppose you want to collect all strings in a stream by concatenating them. You can call

		String result = stream.collect(Collectors.joining());
		String result = stream.collect(Collectors.joining(", ")); --> delimiter is ","
		String result = stream.map(Object::toString).collect(Collectors.joining(",")); --> If the object is a different type other than string, first convert it to string.

Collecting statistics
---------------------
If you want to reduce the stream results to a sum, count, average, maximum, or minimum, use one of the summarizing(Int|Long|Double) methods. These methods take a function that maps the stream objects to numbers and yield a result of type (Int|Long|Double)SummaryStatistics, simultaneously computing the sum, count, average, maximum, and minimum. For ex,

		IntSummaryStatistics summary = stream.collect(Collectors.summarizingInt(String::length));
		
		double averageWordLength = summary.getAverage();
		double maxWordLength = summary.getMax();		

COLLECTING INTO MAPS
--------------------
Suppose you have a Stream<Person> and want to collect the elements into a map so that later you can look up people by their ID. The Collectors.toMap method has two function arguments that produce the map’s keys and values. For example,

		List<Person> people = ...;
		Map<Integer, String> idToName = people.collect(Collectors.toMap(Person::getId, Person::getName));

In the common case when the values should be the actual elements, use Function.identity() for the second function.

		Map<Integer, Person> idToPerson = people.collect(Collectors.toMap(Person::getId, Function.identity()));
		
Note: In the above idToPerson, The Person object in the stream we are collecting into the result map as value. In that case we can use Function.identity().

If there is more than one element with the same key, there is a conflict, and the collector will throw an IllegalStateException. You can override that behavior by supplying a third function argument that resolves the conflict and determines the value for the key, given the existing and the new value. Your function could return the existing value, the new value, or a combination of them. 

For ex, we construct a map that contains, for each language in the available locales, as key its name in your default locale (such as "German"), and as value its localized name
(such as "Deutsch").

		Stream<Locale> locales = Stream.of(Locale.getAvailableLocales());
		Map<String, String> languageNames = locales.collect(
		Collectors.toMap(Locale::getDisplayLanguage, loc ->	loc.getDisplayLanguage(loc), (existingValue, newValue) -> existingValue));

Note: We don’t care that the same language might occur twice (for example, German in Germany and in Switzerland), so we just keep the first entry.		

Now suppose we want to know all languages in a given country. Then we need a Map<String, Set<String>>. For example, the value for "Switzerland" is the set [French, German, Italian]. At first, we store a singleton set for each language. Whenever a new language is found for a given country, we form the union of the existing and the new set.

		Map<String, Set<String>> countryLanguageSets = locales.collect(
			Collectors.toMap(Locale::getDisplayCountry,	l -> Collections.singleton(l.getDisplayLanguage()), (a, b) -> { // Union of a and b
																		var union = new HashSet<String>(a);
																		union.addAll(b);
																		return union; 
																		}));

If you want a TreeMap, supply the constructor as the fourth argument. You must provide a merge function as third argument(In the below example it throws exception).

		Map<Integer, Person> idToPerson = people.collect(
			Collectors.toMap(Person::getId, Function.identity(), (existingValue, newValue) -> { throw new IllegalStateException();},TreeMap::new));

Note: For each of the toMap methods, there is an equivalent toConcurrentMap method that yields a concurrent map. It's better to use concurrentMap with parallelStreams, because a shared map is more efficient than merging maps.

GROUPING AND PARTITIONING
-------------------------
Grouping
--------
In the preceding section, you saw how to collect all languages in a given country. But the process was a bit tedious. You had to generate a singleton set for each map value and then specify how to merge the existing and new values. Forming groups of values with the same characteristic is very common, and the groupingBy method supports it directly.

Let’s look at the problem of grouping locales by country. For ex, switzerland has multiple locales like de_CH, fr_CH, it_CH etc. 

		Stream<Locale> locales = Stream.of(Locale.getAvailableLocales()); --> using java.util.Locale
		Map<String, List<Locale>> countryToLocales = locales.collect(Collectors.groupingBy(Locale::getCountry));

You can now look up all locales for a given country code, for example

		List<Locale> swissLocales = countryToLocales.get("CH");

Partitioning
------------
When the classifier function is a predicate function (that is, a function returning a boolean value), the stream elements are partitioned into two lists: those where the function returns true and the complement. In this case, it is more efficient to use partitioningBy instead of groupingBy. For example, here we split all locales into
those that use English and all others:

		Map<Boolean, List<Locale>> englishAndOtherLocales = locales.collect(Collectors.partitioningBy(l -> l.getLanguage().equals("en")));
		List<Locale> englishLocales = englishAndOtherLocales.get(true);

Note: If you call the groupingByConcurrent method, you get a concurrent map that, when used with a parallel stream, is concurrently populated. This is entirely analogous to the toConcurrentMap method.

DOWNSTREAM COLLECTORS
---------------------
The groupingBy method yields a map whose values are lists. If you want to process those lists in some way, supply a downstream collector. For ex, if you want sets instead of lists, you can use the Collectors.toSet().

		Map<String, Set<Locale>> countryToLocaleSet = locales.collect(Collectors.groupingBy(Locale::getCountry, Collectors.toSet()));

Several collectors are provided for reducing collected elements to numbers: For example,

		Map<String, Long> countryToLocaleCounts = locales.collect(Collectors.groupingBy(Locale::getCountry, Collectors.counting()));

summing(Int|Long|Double) takes a function argument, applies the function to the downstream elements, and produces their sum. For ex,

		Map<String, Integer> stateToCityPopulation = cities.collect(Collectors.groupingBy(City::getState, Collectors.summingInt(City::getPopulation)));

maxBy and minBy take a comparator and produce maximum and minimum of the downstream elements. For example,

		Map<String, Optional<City>> stateToLargestCity = Arrays.stream(Locale.getAvailableLocales()).
																collect(Collectors.groupingBy(City::getState, 
																Collectors.maxBy(Comparator.comparing(City::getPopulation))));
		
The collectingAndThen collector adds a final processing step behind a collector. For example, if you want to know how many distinct results there are, collect them into a set and then compute the size:

		Map<String, Set<String>> countryToLanguages = Arrays.stream(Locale.getAvailableLocales()).
															collect(Collectors.groupingBy(Locale::getCountry, 
															Collectors.mapping(Locale::getDisplayLanguage, Collectors.toSet())));

Note: There is a flatMapping method as well, for use with functions that return streams. For ex,

		Map<String, Set<LineItem>> itemsByCustomerName  = orders.stream()
																.collect(Collectors.groupingBy(Order::getCustomerName,
																Collectors.flatMapping(order -> order.getLineItems().stream(), toSet()))); 

If the grouping or mapping function has return type int, long, or double, you can collect elements into a summary statistics object. For ex,

			Map<String, IntSummaryStatistics> stateToCityPopulationSummary = cities.groupingBy(City::getState, Collectors.summarizingInt(City::getPopulation)));

The filtering collector applies a filter to each group, for example:

			Map<String, Set<City>> largeCitiesByState = cities.collect(groupingBy(City::getState, 
															  filtering(c -> c.getPopulation() > 500000, toSet())));


REDUCTION OPERATIONS
--------------------
The reduce method is a general mechanism for computing a value from a stream. The simplest form takes a binary function and keeps applying it, starting with the first two elements. It’s easy to explain this if the function is the sum:

			List<Integer> values = . . .;
			Optional<Integer> sum = values.stream().reduce((x, y) -> x + y);
			
			or 

			Optional<Integer> sum = values.stream().reduce(Integer::sum);
			
Note: the reduce method computes v0 + v1 + v2 + . . ., where the v are the stream elements. The method returns an Optional because there is no valid result if the stream is empty.

Note: Often, there is an identity e such that e op x = x. i.e. if no elements are present in the stream, the operation returns identity element. For ex, 0 is the identity element for addition. It is writtern like this.

			Integer sum = values.stream().reduce(0, Integer::sum);

Note: The identity value is returned if the stream is empty, and you no longer need to deal with the Optional class.

Now suppose you have a stream of objects and want to form the sum of some property, such as all lengths in a stream of strings. The stream elements have type String, and the accumulated result is an integer. There is a form of reduce that can deal with this situation. 

First, you supply an “accumulator” function (total, word) -> total + word.length(). That function is called repeatedly, forming the cumulative total. But when the computation is parallelized, there will be multiple computations of this kind, and you need to combine their results. You supply a second function for that purpose. For ex,

			int result = words.reduce(0, (total, word) -> total + word.length(), (total1, total2) -> total1 + total2);

Note: In practice, you probably won’t use the reduce method a lot. It is usually easier to map to a stream of numbers and use one of its methods to compute sum, max, or min.

PRIMITIVE TYPE STREAMS
----------------------
The stream library has specialized types IntStream, LongStream, and DoubleStream that store primitive values directly, without using wrappers. If you want to store short, char, byte, and boolean, use an IntStream, and for float, use a DoubleStream.

To create an IntStream, call the IntStream.of and Arrays.stream methods:

		IntStream stream = IntStream.of(1, 1, 2, 3, 5);
		stream = Arrays.stream(values, from, to); // values is an int[] array, from is fromIndex and to is toIndex inside values array.
		
you can also use the static generate and iterate methods. For ex,

		IntStream intStream = IntStream.iterate(1, n -> n+1);
		intStream.forEach(System.out::println); //This will print infinitely. use limit() method to control the iterations.
		
IntStream and LongStream have static methods range and rangeClosed that generate integer ranges with step size one:	For ex,

		IntStream zeroToNinetyNine = IntStream.range(0, 100); 
		IntStream zeroToHundred = IntStream.rangeClosed(0, 100); 

The CharSequence interface has methods codePoints and chars that yield an IntStream of the Unicode codes of the characters or of the code units in the UTF16 encoding. For ex,

		String sentence = "\uD835\uDD46 is the set of octonions.";
		IntStream codes = sentence.codePoints();
		
When you have a stream of objects, you can transform it to a primitive type stream with the mapToInt, mapToLong, or mapToDouble methods. For example, if you have a
stream of strings and want to process their lengths as integers, you might as well do it in an IntStream:

		Stream<String> words = . . .;
		IntStream lengths = words.mapToInt(String::length);

To convert a primitive type stream to an object stream, use the boxed method:

		Stream<Integer> integers = IntStream.range(0, 100).boxed();

Here are few differences of Primitive streams compared to object streams.

• The toArray methods return primitive type arrays.
• Methods that yield an optional result return an OptionalInt, OptionalLong, or OptionalDouble. These classes are analogous to the Optional class, but they have methods getAsInt, getAsLong, and getAsDouble instead of the get method.
• There are methods sum, average, max, and min that return the sum, average, maximum, and minimum. These methods are not defined for object streams. For ex,

		IntStream intStream = IntStream.iterate(1, n -> n+1).limit(1000);
		System.out.println(intStream.max().getAsInt());

• The summaryStatistics method yields an object of type IntSummaryStatistics, LongSummaryStatistics, or DoubleSummaryStatistics that can simultaneously report the sum, count, average,
maximum, and minimum of the stream.


The Random class has methods ints, longs, and doubles that return primitive type streams(IntStream, DoubleStream etc) of random numbers. If you need random numbers in parallel streams, use the SplittableRandom class instead.

PARALLEL STREAMS
----------------
Streams make it easy to parallelize bulk operations. The process is mostly automatic, but you need to follow a few rules. First of all, you must have a parallel stream. You can
get a parallel stream from any collection with the Collection.parallelStream() method: For ex,

		Stream<String> parallelWords = words.parallelStream();
		
The parallel method converts any sequential stream into a parallel one.
		
		Stream<String> parallelWords = Stream.of(wordArray).parallel();
		
As long as the stream is in parallel mode, all intermediate stream operations will be parallelized.

Note: When stream operations run in parallel, the intent is that the same result is returned as if they had run serially. It is important that the operations are stateless and can be
executed in an arbitrary order.

Here is an example of something you cannot do. Suppose you want to count all short words in a stream of strings:

		var shortWords = new int[12];
		words.parallelStream().forEach(s ->{ 
			if (s.length() < 12) 
				shortWords[s.length()]++; 
			});
		// ERROR—race condition!
		System.out.println(Arrays.toString(shortWords));

The function passed to forEach runs concurrently in multiple threads, each updating a shared array. That's a classic race condition.

It is your responsibility to ensure that any functions you pass to parallel stream operations are safe to execute in parallel. The best way to do that is to stay away from mutable state. In this example, you can safely parallelize the computation if you group strings by length and count them.

		Map<Integer, Long> shortWordCounts = words.parallelStream()
													.filter(s -> s.length() < 12)
													.collect(Collectors.groupingBy(String::length,Collectors.counting()));

Some operations can be more effectively parallelized when the ordering requirement is dropped. By calling the Stream.unordered method, you indicate that you are not
interested in ordering. One operation that can benefit from this is Stream.distinct. On an ordered stream, distinct retains the first of all equal elements. But, in parallel processing a segment can’t know which elements to discard until the preceding segment has been processed. If it is acceptable to retain any of the unique elements, all segments can be processed concurrently.

If you just want any n elements from a stream and you don’t care which ones you get, call:

		Stream<String> sample = words.parallelStream().unordered().limit(n);

Merging maps is expensive. For that reason, the Collectors.groupingByConcurrent method uses a shared concurrent map. To benefit from parallelism, the order of the map values will
not be the same as the stream order.

		Map<Integer, List<String>> result = words.parallelStream()
												.collect(Collectors.groupingByConcurrent(String::length));	// Values aren’t collected in stream order

Don’t turn all your streams into parallel streams with the hope of speeding up their operations. Keep these issues in mind:

• There is a substantial overhead to parallelization that will only pay off for very large data sets.
• Parallelizing a stream is only a win if the underlying data source can be effectively split into multiple parts.
• The thread pool that is used by parallel streams can be starved by blocking operations such as file I/O or network access.

Note: Parallel streams work best with huge inmemory collections of data and computationally intensive processing.

Note:
Prior to Java 9, parallelizing the stream returned by the Files.lines method made no sense. The data was not splittable—you had to read the first half of the file before the second half. Now the method uses a memorymapped file, and splitting is effective. If you process the lines of a huge file, parallelizing the stream may improve performance.

By default, parallel streams use the global forkjoin pool returned by ForkJoinPool.commonPool. That is fine if your operations don’t block and you don’t share the pool with other tasks. There is a trick to substitute a different pool. Place your operations inside the submit method of a custom pool:

		ForkJoinPool customPool = . . .;
		result = customPool.submit(() -> stream.parallel().map(. . .).collect(. . .)).get();

or asynchronously you can use CompletableFuture. For ex,
		
		CompletableFuture.supplyAsync(() -> stream.parallel().map(. . .).collect(. . .), customPool).thenAccept(result -> . . .);

Note:
If you want to parallelize stream computations based random numbers, don’t start out with a stream obtained from the Random.ints, Random.longs, or Random.doubles methods. Those streams don’t split. Instead, use the ints, longs, or doubles methods of the SplittableRandom class.

