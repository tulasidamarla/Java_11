Lambda expressions
------------------
If you want to sort strings by length instead of the default dictionary order, you can pass a Comparator object to the sort method:

		class LengthComparator implements Comparator<String>
		{
			public int compare(String first, String second)
			{
				return first.length() - second.length();
			}
		}. . .
		Arrays.sort(strings, new LengthComparator());

Note:
The compare method isn’t called right away. Instead, the sort method keeps calling the compare method, rearranging the elements if they are out of order, until the array is sorted.
It means a block of code was passed to a sort method. In java 8, passing a block of code became easy with lambda expressions.

The Syntax of Lambda Expressions
--------------------------------
To write the above compare method logic using lambda,

		(String first, String second) -> first.length() - second.length()

Also, you don't need to specify the types of parameters.

		(first, second) -> first.length() - second.length()	

If a lambda expression has no parameters, you still supply empty parentheses, just as with a parameterless method:		

		() -> { for (int i = 100; i >= 0; i--) System.out.println(i); }
	
Note: If you use curly braces, statement should end with parentheses(;).

If a method has a single parameter with inferred type, you can even omit the parentheses:
		name -> System.out.println(name)

It is illegal for a lambda expression to return a value in some branches but not in others. For example, 

		(int x) -> { if (x >= 0) return 1; } --> is invalid.


Functional Interfaces
---------------------
You can supply a lambda expression whenever an object of an interface with a single abstract method is expected. Such an interface is called a functional interface.

It has always been possible for an interface to redeclare methods from the Object class such as toString or clone, and these declarations do not make the methods abstract. Check out the Comparator API for an example.

Consider the Arrays.sort method. Its second parameter requires an instance of Comparator, an interface with a single method. Simply supply a lambda:

		Arrays.sort(words, (first, second) -> first.length() - second.length());

The management of these objects and classes is completely implementationdependent, and it can be much more efficient than using traditional inner classes. It is best to think of a lambda expression as a function, not an object, and to accept that it can be passed to a functional interface.

Note:
You can’t even assign a lambda expression to a variable of type Object—Object is not a functional interface.

Types of Functional Interfaces
------------------------------
1)Function	--> Consumes value/values and returns value
2)Predicate	--> Consumes value/values and returns boolean
3)Supplier	--> Doesn't take value and returns a value
4)Consumer	--> Consumes a value and doesn't return any value

Function/BiFunction
--------------------
There are about 44 types of generic functional interface types. For ex, we have a type called BiFunction, which takes 2 input parameters and returns a value. For ex,

		BiFunction<String, String, Integer> comp 	= (first, second) -> first.length() - second.length();
		
Note: If you look at Arrays.sort(array, comparator), it takes comparator object. The above comp variable, if you pass to sort method, it won't work. For ex,
		
		Arrays.sort(words, comp); --> This will throw compilation error.

Reason: Behind the scenes, the Arrays.sort method receives an object of some class that implements Comparator<String>. It means, your lambda expression is converted into object of type Comparator.

Function has a subtype named, UnaryOperator. This takes an input of type T and returns a value of same type.
BiFunction has a subtype named, BinaryOperator. This take an two input parameters of type T and return a value of same type.

Predicate
---------
A particularly useful interface in the java.util.function package is Predicate:

public interface Predicate<T>
{
	boolean test(T t);
	// additional default and static methods
}

Usage:
-----
The ArrayList class has a removeIf method whose parameter is a Predicate. For ex,

		list.removeIf(Objects::isNull);
		
The above method removes any null objects in the list.

Supplier
--------
Another useful functional interface is Supplier<T>: A supplier has no arguments and yields a value of type T when it is called. Suppliers are used for lazy evaluation. For example, consider the call

		List<String> s2 = Objects.requireNonNullElseGet(names, () -> new ArrayList<>());
		
Method References
-----------------
Suppose you want to print list of strings. For ex,
		
		names.foreach(name -> System.out.println(name));

This can be writtern in simple way using method references. For ex,
		
		names.foreach(System.out::println);
		
The expression System.out::println is a method reference. In the above example, foreach is takes a parameter of time Consumer.

Note: There are ten overloaded println methods in the PrintStream class (of which System.out is an instance). The compiler needs to figure out which one to use, depending on context.
The println(String x) method is selected from the ten overloaded println methods since String is the best match.

Now suppose we assign the same method reference to a different functional interface:

		Runnable task = System.out::println;
		
The Runnable functional interface has a single abstract method with no parameters "void run()". In this case, the println() method with no parameters is chosen. Calling task.run() prints a blank line to System.out.

The :: operator separates the method name from the name of an object or class. There are three variants:

1. object::instanceMethod -> It is equivalent to a lambda expression whose parameters are passed to the method. For ex,
								System.out::println (Object is system.out). This is equivalent to x -> System.out.println(x).
2. Class::instanceMethod  -> The first parameter becomes the implicit parameter of the lambda, remaining parameters are passed to the method. For ex,
								String::compareToIgnoreCase . This is equivalent to (x, y) -> x.compareToIgnoreCase(y).
3. Class::staticMethod	  -> All parameters are passed to the static method. For ex,
								Math::pow is equivalent to (x, y) -> Math.pow(x, y).

Few more examples
-----------------
1)separator::equals		x -> separator.equals(x)		object::instanceMethod
2)String::trim			x -> x.trim()					Class::instanceMethod
3)String::concat		(x, y) -> x.concat(y)			Class::instanceMethod
4)Integer::valueOf		x -> Integer.valueOf(x)			Class::staticMethod
5)Integer::sum			(x, y) -> Integer.sum(x, y)		Class::staticMethod
6)Integer::new			x -> new Integer(x)				This is a constructor Reference
7)Integer[]::new		n -> new Integer[n]				This is an array constructor Reference

Note: A lambda expression can only be rewritten as a method reference if the body of the lambda expression calls a single method and doesn’t do anything else. For ex,
consider the below example.

		s -> s.length() == 0

This cannot be written as lambda expression, because it contains a method call followed by comparision. This cannot be converted to method reference.

Note:
You can capture the this parameter in a method reference. For example, this::equals is the same as x -> this.equals(x). It is also valid to use super. The method expression
super::instanceMethod. For ex,

		class Greeter {
			public void greet(ActionEvent event) {
				System.out.println("Hello, the time is " + Instant.ofEpochMilli(event.getWhen()));
			}
		}
		class RepeatedGreeter extends Greeter {
			public void greet(ActionEvent event) {
				var timer = new Timer(1000, super::greet);
				timer.start();
			}
		}

When the RepeatedGreeter.greet method starts, a Timer is constructed that executes the super::greet method on every timer tick.

Constructor References
----------------------
Constructor references are just like method references, except that the name of the method is new. For example, Person::new is a reference to a Person constructor. Which constructor? It depends on the context. Suppose you have a list of strings. Then you can turn it into an array of Person objects, by calling the constructor on each of the strings, with the following invocation:

		ArrayList<String> names = . . .;
		Stream<Person> stream = names.stream().map(Person::new);
		List<Person> people = stream.collect(Collectors.toList());

You can form constructor references with array types. For example, int[]::new is a constructor reference with one parameter: the length of the array. It is equivalent to the lambda expression x -> new int[x].

Array constructor references are useful to overcome a limitation of Java. It is not possible to construct an array of a generic type T, because T[n] is an error. For example, suppose we want to have an array of Person objects. The Stream interface has a toArray method that returns an Object array:

		Object[] people = stream.toArray();

But that is unsatisfactory. The user wants an array of references to Person, not references to Object. The stream library solves that problem with constructor references. Pass Person[]::new to the toArray method:

		Person[] people = stream.toArray(Person[]::new);
		
Variable Scope
--------------
Often, you want to be able to access variables from an enclosing method or class in a lambda expression. For ex,

		public static void repeatMessage(String text, int delay){
			ActionListener listener = event -> {
				System.out.println(text);
				Toolkit.getDefaultToolkit().beep();
			};
			new Timer(delay, listener).start();
		}

Consider a call
		
		repeatMessage("Hello", 1000);
		
Note: "text" variable is not defined in lambda. The code of the lambda expression may run long after the call to repeatMessage has returned and the parameter variables are gone.

The data structure representing the lambda expression must store the values for the free variables—in our case, the string "Hello". A lambda expression can capture the value of a variable in the enclosing scope.

Restriction
-----------
In Java, to ensure that the captured value is well-defined, there is an important restriction. In a lambda expression, you can only reference variables whose value doesn’t change.
For example, the following is illegal:

		public static void repeatMessage(int start, int delay){
			ActionListener listener = event -> {
				start--;							// Error: Can't mutate captured variable
				System.out.println(start);
			};
			new Timer(delay, listener).start();
		}

There is a reason for this restriction. Mutating variables in a lambda expression is not safe when multiple actions are executed concurrently.

It is illegal to declare a parameter or a local variable in the lambda that has the same name as a local variable.

		Path first = Path.of("/usr/bin");
		Comparator<String> comp = (first, second) -> first.length() - second.length(); // ERROR: Variable first already defined

Processing Lambda Expressions
-----------------------------
The point of using lambdas is deferred execution. There are many reasons for executing code later, such as:

1)Running the code in a separate thread
2)Running the code multiple times
3)Running the code at the right point in an algorithm (for example, the comparison operation in sorting)
4)Running the code when something happens (a button was clicked, data has arrived, and so on)
5)Running the code only when necessary

Let’s look at a simple example. Suppose you want to repeat an action n times. The action and the count are passed to a repeat method:

		repeat(10, () -> System.out.println("Hello, World!"));
		
Implementation of repeat method is:

		public static void repeat(int n, Runnable action)
		{
			for (int i = 0; i < n; i++) action.run();
		}		
		
To accept the lambda, we need to pick a functional interface. In this case, we have used the Runnable interface: There are other types of functional interfaces:

Runnable -> no parameters, no return type, method name is run(), Runs an action.
Supplier<T> -> no parameter, returns any type(T), method name is get(), Supplies a value of type T. 
Consumer<T> -> paramter type T, returns nothing, method name accept(), Consumes a value of type T.
BiConsumer<T, U> -> parameter type (T, U), return nothing, method name accept(), Consumes a value of type T, U.
Function<T, R> -> parameter type T, return type R, method name apply(), A function with argument type T
BiFunction<T, U, R> -> parameter type T and U, return type R, method name apply(), A function with argument type T and U.
UnaryOperator<T> -> Same as Function, with Parameter and return type T.
BinaryOperator<T> -> Same as BiFunction, with parameters and return type T.
Predicate<T> -> parameter type T, return type boolean, method name test(), A function with one parameter type T and return type boolean.
BiPredicate<T,U> -> parameter types T and U, return type boolean, method name test(), A function with two parameter types T and U with return type boolean.


Note: In the above example we have used Runnable, which doesn't take any argument. So, we can't exactly tell in which iteration currenly run() is executing. If We want to tell the
action in which iteration it occurs. For that, we need to pick a functional interface that has a method with an int parameter and a void return. The
standard interface for processing int values is IntConsumer. 

		public interface IntConsumer
		{
			void accept(int value);
		}

Here is the improved version of the repeat method:

		public static void repeat(int n, IntConsumer action)
		{
			for (int i = 0; i < n; i++) action.accept(i);
		}

And here is how you call it:

		repeat(10, i -> System.out.println("Countdown: " + (9 - i)));
		
For Primitive types int,long,double we have Consumers, Suppliers, Functions, BiFunction etc.

The above example with a Function. 

	//Function to be executed in each call
	public static int getInteger(int i) {
		return i+10;
	}

	// This method is called once from your main method
	public static void get(int n, IntFunction<Integer> function) {
		for (int i = 0; i< n;i++) {
			int returnedValue = function.apply(i);
			System.out.println("Returned value " + returnedValue);
		}
	}	

	//Lambda in your main method
	get(10, i -> getInteger(i));
	
The output prints values from 10 to 19.

Note:
It is a good idea to use a functional interface from the above list whenever you can. For ex, There is a legacy interface java.io.FileFilter, but it is better to use the standard
Predicate<File>. The only reason not to do so would be if you already have many useful methods producing FileFilter instances.

*****
Note:
Most of the standard functional interfaces have nonabstract methods for producing or combining functions. There are default methods and, or, negate for combining predicates

		Predicate.isEqual(a).or( Predicate.isEqual(b)) is the same as x -> a.equals(x) || b.equals(x)
		
If you design your own interface with a single abstract method, you can tag it with the @FunctionalInterface annotation. This has two advantages. The compiler gives an error message if you accidentally add another abstract method. And the javadoc page includes a statement that your interface is a functional interface.

It is not required to use the annotation. Any interface with a single abstract method is, by definition, a functional interface.

More about Comparators
----------------------
The Comparator interface has a number of convenient static methods for creating comparators. These methods are intended to be used with lambda expressions or method references.

The static comparing method takes a “key extractor” function that maps a type T to a comparable type. For ex,

		Arrays.sort(people, Comparator.comparing(Person::getName));

You can chain comparators with the thenComparing method for breaking ties. For example,		

		Arrays.sort(people, Comparator.comparing(Person::getLastName)
										.thenComparing(Person::getFirstName));

Note:		
If two people have the same last name, then the second comparator is used.		

To sort people by the length of their names,

		Person[] people = { new Person("Tulasi", "Damarla"), new Person("Kris","Rayi") };
		Arrays.sort(people, Comparator.comparing(Person::getFirstName, (first, second) -> Integer.compare(first.length(), second.length())));

Note: Both the comparing and thenComparing methods have variants that avoid boxing of int, long, or double values. An easier way of producing the preceding operation would be

		Arrays.sort(people, Comparator.comparingInt(p -> p.getFirstName().length()));
		
Note: If your key function can return null, you will like the nullsFirst and nullsLast adapters. These static methods take an existing comparator and modify it so that it doesn’t throw an exception when encountering null values but ranks them as smaller or larger than regular values. For ex,

		Person[] people = { new Person("Tulasi", "Damarla"), new Person(), new Person("Krishna","Rayi") };
		Arrays.sort(people, Comparator.comparing(Person::getFirstName,	Comparator.nullsFirst(Comparator.naturalOrder())));
		Arrays.asList(people).forEach(person -> System.out.println(person.getFirstName()));
		
output for the above code is : 
null
Krishna
Tulasi

Because it used natural ordering, Krishna came before Tulasi. If we use the below code, the output changes.

		Arrays.sort(people, Comparator.comparing(Person::getFirstName,	Comparator.nullsFirst(Comparator.comparingInt(firstName -> firstName.length()))));
		Arrays.asList(people).forEach(person -> System.out.println(person.getFirstName()));

output:
null
Tulasi
Krishna
